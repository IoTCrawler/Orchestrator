package com.agtinternational.iotcrawler.fiware.clients;

import com.orange.ngsi2.client.Ngsi2Client;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.Iterator;

public class PatchedNgsi2Client extends Ngsi2Client {

    public PatchedNgsi2Client(AsyncRestTemplate asyncRestTemplate, String baseURL) {
        super(asyncRestTemplate, baseURL);
    }

    @Override
    protected <T, U> ListenableFuture<ResponseEntity<T>> request(HttpMethod method, String uri, U body, Class<T> responseType) {
        return this.request(method, uri, this.getHttpHeaders(method), body, responseType);
    }


    private HttpHeaders getHttpHeaders(HttpMethod method) {
        HttpHeaders cloned = cloneHttpHeaders();
        if(method.equals(HttpMethod.GET))
            cloned.remove("Content-Type");
        return cloned;
    }

    private HttpHeaders cloneHttpHeaders() {
        HttpHeaders httpHeaders = this.getHttpHeaders();
        HttpHeaders clone = new HttpHeaders();
        Iterator var3 = httpHeaders.keySet().iterator();

        while(var3.hasNext()) {
            String entry = (String)var3.next();
            clone.put(entry, httpHeaders.get(entry));
        }

        return clone;
    }
}
