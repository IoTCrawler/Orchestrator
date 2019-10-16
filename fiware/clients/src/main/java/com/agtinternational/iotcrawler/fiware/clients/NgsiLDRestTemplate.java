package com.agtinternational.iotcrawler.fiware.clients;

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