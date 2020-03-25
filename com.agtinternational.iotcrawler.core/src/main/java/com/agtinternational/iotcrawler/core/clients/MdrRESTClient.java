package com.agtinternational.iotcrawler.core.clients;

import com.agtinternational.iotcrawler.fiware.clients.NgsiLDClient;
import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.concurrent.Semaphore;

public class MdrRESTClient extends NgsiLDClient {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public MdrRESTClient(String brokerURL){
        super(brokerURL);
    }

    public boolean registerEntity(EntityLD entity){
        final Boolean[] success = {false};

        ListenableFuture<Void> req = addEntity(entity);
        Semaphore reqFinished = new Semaphore(0);
        req.addCallback(new ListenableFutureCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success[0] = true;
                reqFinished.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                success[0] = false;
                throwable.printStackTrace();
                reqFinished.release();
            }

        });
        try {
            reqFinished.acquire();
        } catch (InterruptedException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        return success[0];
    }
}
