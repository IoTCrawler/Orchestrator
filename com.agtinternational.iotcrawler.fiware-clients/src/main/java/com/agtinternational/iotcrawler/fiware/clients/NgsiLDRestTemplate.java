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

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.*;
import org.springframework.web.client.*;

import java.util.*;

public class NgsiLDRestTemplate extends RestTemplate implements RestOperations {

    private ResponseExtractor<HttpHeaders> headersExtractor;
    private List<HttpMessageConverter<?>> messageConverters;

    protected ResponseExtractor<HttpHeaders> headersExtractor() {
        return headersExtractor;
    }

    public NgsiLDRestTemplate() {
        //super();
        headersExtractor = new NgsiLDRestTemplate.HeadersExtractor();
        messageConverters = new ArrayList();

//        this.messageConverters.add(new ByteArrayHttpMessageConverter());
//        this.messageConverters.add(new StringHttpMessageConverter());
//        this.messageConverters.add(new ResourceHttpMessageConverter());
//        this.messageConverters.add(new SourceHttpMessageConverter<Source>());
//        this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());


        //if (romePresent) {
            //this.messageConverters.add(new AtomFeedHttpMessageConverter());
            //this.messageConverters.add(new RssChannelHttpMessageConverter());
        //}
        //if (jackson2XmlPresent) {
            //messageConverters.add(new MappingJackson2XmlHttpMessageConverter());

        //}
        //else if (jaxb2Present) {
            //this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
        //}
        //if (jackson2Present) {
            //messageConverters.add(new MappingJackson2HttpMessageConverter());
        //}
        //else if (gsonPresent) {
            //this.messageConverters.add(new GsonHttpMessageConverter());
//        }

        messageConverters.add(new NgsiLDConverter());
    }

    public NgsiLDRestTemplate(ClientHttpRequestFactory syncRequestFactory) {
        this();
    }

    @Override
    public List<HttpMessageConverter<?>> getMessageConverters() {
        return this.messageConverters;
    }

    // GET

    private static class HeadersExtractor implements ResponseExtractor<HttpHeaders> {
        private HeadersExtractor() {
        }

        public HttpHeaders extractData(ClientHttpResponse response) {
            return response.getHeaders();
        }
    }

}
