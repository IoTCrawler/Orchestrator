package com.agtinternational.iotcrawler.fiware.clients;

/*-
 * #%L
 * fiware-clients
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

import org.springframework.http.HttpMethod;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.*;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class CustomAsyncRestTemplate extends AsyncRestTemplate {

    public CustomAsyncRestTemplate(AsyncClientHttpRequestFactory requestFactory, RestTemplate restTemplate) {
        super(requestFactory, restTemplate);
    }

    public <T> ListenableFuture<T> execute(String url, HttpMethod method, AsyncRequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... urlVariables) throws RestClientException {
        URI expanded=null;
        try {
            expanded = URI.create(url.replace("#","%23"));
        }
        catch (Exception e){
            URI aaaurl = new UriTemplate(url).expand(urlVariables);
            String query = aaaurl.getQuery()+(aaaurl.getFragment()!=null?"#"+aaaurl.getFragment():"");
            query = query.replace("%25","%");
            query = query.replace("%22","\"");
            //query = query.replace("#","%23");

            try {
                expanded = new URI(aaaurl.getScheme(),
                        aaaurl.getUserInfo(),
                        aaaurl.getHost(),
                        aaaurl.getPort(),
                        aaaurl.getPath(),
                        query,
                        null);
            } catch (URISyntaxException e2) {
                e2.printStackTrace();
            }


        }
        return this.doExecute(expanded, method, requestCallback, responseExtractor);
    }

    public <T> ListenableFuture<T> execute(String url, HttpMethod method, AsyncRequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> urlVariables) throws RestClientException {
        URI expanded = URI.create(url); //= (new UriTemplate(url)).expand(urlVariables);
        return this.doExecute(expanded, method, requestCallback, responseExtractor);
    }


}
