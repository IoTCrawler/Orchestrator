package com.agtinternational.iotcrawler.core;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractFuture;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


public class RabbitMQRpcClient implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQRpcClient.class);

    Channel responseChannel;
    Channel requestChannel;
    String responseQueueName;
    String requestQueueName;

    Semaphore requestMapMutex = new Semaphore(1);

    Map<String, RabbitMQRpcClient.RabbitRpcRequest> currentRequests = new HashMap<String, RabbitMQRpcClient.RabbitRpcRequest>();

    long maxWaitingTime = 600000;
    int maxWaitingAttempts = 10;


    public static RabbitMQRpcClient create(Connection connection, String requestQueueName) throws IOException {
        RabbitMQRpcClient client = new RabbitMQRpcClient();
        try {
            client.init(connection, requestQueueName);
            return client;
        } catch (Exception e) {
            client.close();
            throw e;
        }
    }


    protected RabbitMQRpcClient() {
    }

    /**
     * Queue used for the request.
     */

    protected void init(Connection connection, String requestQueueName) throws IOException {
        this.requestQueueName = requestQueueName;

        requestChannel = connection.createChannel();
        requestChannel.queueDeclare(requestQueueName, false, false, true, null);

        responseChannel = connection.createChannel();
        responseQueueName = responseChannel.queueDeclare().getQueue();
        //responseQueue = new RabbitQueue(tempChannel, tempChannel.queueDeclare().getQueue());

        responseChannel.basicQos(1);
        RabbitMQRpcClient.RabbitRpcClientConsumer consumer = new RabbitMQRpcClient.RabbitRpcClientConsumer(responseChannel, this);
        responseChannel.basicConsume(responseQueueName, true, consumer);
    }


    public byte[] request(byte[] data) {
        return request(null, data);
    }

    public byte[] request(String exchangeName, byte[] data) {
        byte[] response = null;
        RabbitMQRpcClient.RabbitRpcRequest request = new RabbitMQRpcClient.RabbitRpcRequest();


        int attempt=0;
        while (response==null && attempt<maxWaitingAttempts){

            try {
                String corrId = java.util.UUID.randomUUID().toString();

                BasicProperties props = new BasicProperties.Builder().correlationId(corrId).deliveryMode(2).replyTo(responseQueueName).build();
                requestMapMutex.acquire();
                currentRequests.put(corrId, request);
                requestMapMutex.release();

                if(exchangeName!=null)
                    requestChannel.basicPublish(exchangeName, "", props, data);
                else
                    requestChannel.basicPublish("", requestQueueName, props, data);
            } catch (Exception e) {
                LOGGER.error("Failed to publish query to queue.", e);
            }

            try {
                response = request.get(maxWaitingTime, TimeUnit.MILLISECONDS);
            }catch (TimeoutException e) {
                attempt++;
                LOGGER.debug("Waiting timeout expired. Trying another attempt ({} of {})", attempt, 10);
            }
            catch (Exception e) {
                LOGGER.error("Exception while sending query", e);
                e.printStackTrace();
                break;
            }
        }
        return response;
    }


    protected void processResponseForRequest(String corrId, byte[] body) {
        if (currentRequests.containsKey(corrId)) {
            try {
                requestMapMutex.acquire();
                currentRequests.get(corrId).setResponse(body);
                currentRequests.remove(corrId);
                requestMapMutex.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public long getMaxWaitingTime() {
        return maxWaitingTime;
    }

    /**
     * Sets the maximum amount of time the client is waiting for a response.
     *
     * @param maxWaitingTime
     *            the maximum waiting time in milliseconds
     */
    public void setMaxWaitingTime(long maxWaitingTime) {
        this.maxWaitingTime = maxWaitingTime;
    }

    @Override
    public void close() throws IOException {
        try {
            requestChannel.close();
        } catch (TimeoutException e) {
            LOGGER.error("Failed to close request channel: {}",e.getLocalizedMessage());
        }
        try {
            responseChannel.close();
        } catch (TimeoutException e) {
            LOGGER.error("Failed to close response channel: {}",e.getLocalizedMessage());
        }
    }

    /**
     * Internal implementation of a Consumer that receives messages on the reply
     * queue and calls
     * {@link RabbitMQRpcClient#processResponseForRequest(String, byte[])} of its
     * {@link #client}.
     *
     * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
     *
     */
    protected static class RabbitRpcClientConsumer extends DefaultConsumer {

        /**
         * The client for which this instance is acting as consumer.
         */
        private RabbitMQRpcClient client;

        /**
         * Constructor.
         *
         * @param channel
         *            channel from which the messages are received
         * @param client
         *            the client for which this instance is acting as consumer
         */
        public RabbitRpcClientConsumer(Channel channel, RabbitMQRpcClient client) {
            super(channel);
            this.client = client;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                throws IOException {
            try {
                String corrId = properties.getCorrelationId();
                if (corrId != null) {
                    client.processResponseForRequest(corrId, body);
                }
            } catch (Exception e) {
                LOGGER.error("Exception while processing response.", e);
            }
        }
    }

    /**
     * Simple extension of the {@link AbstractFuture} class that waits for the
     * response which is set by the {@link #setResponse(byte[] response)}.
     *
     * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
     *
     */
    protected static class RabbitRpcRequest extends AbstractFuture<byte[]> implements Future<byte[]> {

        /**
         * Calls the internal set method of the {@link AbstractFuture} class.
         *
         * @param response
         *            the response this request is waiting for
         */
        public void setResponse(byte[] response) {
            set(response);
        }
    }
}
