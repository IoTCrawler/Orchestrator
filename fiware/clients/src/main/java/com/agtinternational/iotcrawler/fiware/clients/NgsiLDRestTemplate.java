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
