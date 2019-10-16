package com.agtinternational.iotcrawler.fiware.clients;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.*;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.Map;

public class PatchedAsyncRestTemplate extends AsyncRestTemplate {

    public PatchedAsyncRestTemplate(AsyncClientHttpRequestFactory requestFactory, RestTemplate restTemplate) {
        super(requestFactory, restTemplate);
    }

    public <T> ListenableFuture<T> execute(String url, HttpMethod method, AsyncRequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... urlVariables) throws RestClientException {
        URI expanded = URI.create(url);//(new UriTemplate(url)).expand(urlVariables);
        return this.doExecute(expanded, method, requestCallback, responseExtractor);
    }

    public <T> ListenableFuture<T> execute(String url, HttpMethod method, AsyncRequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> urlVariables) throws RestClientException {
        URI expanded = URI.create(url); //= (new UriTemplate(url)).expand(urlVariables);
        return this.doExecute(expanded, method, requestCallback, responseExtractor);
    }

}
