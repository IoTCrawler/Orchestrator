package com.agtinternational.iotcrawler.fiware.clients;

/*-
 * #%L
 * fiware-clients
 * %%
 * Copyright (C) 2019 AGT International. Author Pavel Smirnov (psmirnov@agtinternational.com)
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
