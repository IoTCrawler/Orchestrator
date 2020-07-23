package com.agtinternational.iotcrawler.core.clients;

/*-
 * #%L
 * core
 * %%
 * Copyright (C) 2019 - 2020 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
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

import com.agtinternational.iotcrawler.core.models.StreamObservation;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Function;

public class RabbitClient {
    private Logger LOGGER = LoggerFactory.getLogger(RabbitClient.class);
    Connection connection;
    Channel channel;
    String rabbitHost;
    ConnectionFactory factory;

    public RabbitClient(String rabbitHost){
        this.rabbitHost = rabbitHost;
        factory = new ConnectionFactory();
        String[] splitted = rabbitHost.split(":");
        factory.setHost(splitted[0]);
        if(splitted.length>1)
            factory.setPort(Integer.parseInt(splitted[1]));
    }

    public void init() throws Exception {
        getConnection();
    }

    private Connection getConnection() throws Exception {
        if(connection==null || !connection.isOpen()){
            channel=null;
            int attempt = 0;
            while (attempt<12){
                LOGGER.debug("Trying connect to Rabbit at {}", rabbitHost);
                attempt++;
                try {
                    connection = factory.newConnection();
                    LOGGER.debug("Connection to Rabbit {} established", rabbitHost);
                    break;
                } catch (Exception e) {
                    LOGGER.error("Failed to init rabbit connection. Trying another attempt ({} or {})", attempt, 12);
                    e.printStackTrace();
                }
                if(connection==null)
                    Thread.sleep(5000);
            }

            if(connection==null)
                throw new Exception("No connection to Rabbit established in "+attempt+" attempts");
        }

        return connection;
    }

    public void declareExchange(String exchangeName) throws Exception {
        LOGGER.debug("Declaring exchange {}", exchangeName);
        Channel channel = getChannel();
        channel.exchangeDeclare(exchangeName, "fanout");

    }

    public String declareBoundQueue(String exchangeName) throws Exception {
        channel = getChannel();
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, "");
        return queueName;
    }

    private Channel getChannel() throws Exception {
        if(connection==null || !connection.isOpen())
            connection = getConnection();

        if(channel==null)
            channel = connection.createChannel();

        return channel;
    }

    public void initRabbitMQListener(String exchangeName, Function<StreamObservation, Void> onChange){
        try {
            LOGGER.debug("Initing {} queue listener", exchangeName);
            channel = getChannel();
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        LOGGER.debug("Notification received");
                        onChange.apply(null);
                        //handleCmd(body, properties.getReplyTo());
                    } catch (Exception e) {
                        LOGGER.error("Exception while trying to handle incoming command.", e);
                    }
                }
            };
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, exchangeName, "");
            channel.basicConsume(queueName, true, consumer);
        }
        catch (Exception e){
            LOGGER.error("Failed to init consumer");
            e.printStackTrace();
        }
    }

    public void publish(String queueName, String content){
        AMQP.BasicProperties properties = new AMQP.BasicProperties
                .Builder()
                .correlationId(queueName)
                .build();
        Boolean published = false;
        try {
            Channel channel = getChannel();
            //channel.basicPublish("", properties.getReplyTo(), properties, theString.getBytes("UTF-8"));
            channel.basicPublish(queueName, "", properties, content.getBytes("UTF-8"));
            published = true;
            LOGGER.debug("Notification published to {}", queueName);
        } catch (Exception e) {
            LOGGER.error("Failed to publish to {}: {}", queueName, e.getLocalizedMessage());
            //e.printStackTrace();
        }
    }

    public void close(){
        if(connection!=null)
            try {
                connection.close();
            }
            catch (Exception e){
                LOGGER.error("Failed to close: {}", e.getLocalizedMessage());
                e.printStackTrace();
            }
    }
}
