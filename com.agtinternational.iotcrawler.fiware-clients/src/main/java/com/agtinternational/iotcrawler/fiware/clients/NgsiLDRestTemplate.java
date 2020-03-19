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

import com.agtinternational.iotcrawler.fiware.models.EntityLD;
import com.agtinternational.iotcrawler.fiware.models.Utils;
import com.sun.istack.Nullable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class NgsiLDRestTemplate extends RestTemplate implements RestOperations {

    private ResponseExtractor<HttpHeaders> headersExtractor;
    //private List<HttpMessageConverter<?>> messageConverters;

    protected ResponseExtractor<HttpHeaders> headersExtractor() {
        return headersExtractor;
    }

    public NgsiLDRestTemplate() {
        super();
        //headersExtractor = new NgsiLDRestTemplate.HeadersExtractor();
        //messageConverters = new ArrayList();

//        this.messageConverters.add(new ByteArrayHttpMessageConverter());
//        this.messageConverters.add(new StringHttpMessageConverter());
//        this.messageConverters.add(new ResourceHttpMessageConverter());
//        this.messageConverters.add(new SourceHttpMessageConverter<Source>());
//        this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
//
//
//        if (romePresent) {
//            this.messageConverters.add(new AtomFeedHttpMessageConverter());
//            this.messageConverters.add(new RssChannelHttpMessageConverter());
//        }
//        if (jackson2XmlPresent) {
//            messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
//
//        }
//        else if (jaxb2Present) {
//            this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
//        }
//        if (jackson2Present) {
//            messageConverters.add(new MappingJackson2HttpMessageConverter());
//        }
//        else if (gsonPresent) {
//            this.messageConverters.add(new GsonHttpMessageConverter());
//        }

        headersExtractor = new NgsiLDRestTemplate.HeadersExtractor();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new NgsiLDConverter());
        messageConverters.addAll(getMessageConverters());
        setMessageConverters(messageConverters);
        //headersExtractor
    }

//    @Override
//    protected <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
//        return new ResponseEntityResponseExtractor(responseType);
//    }

//    /**
//     * Return a {@code ResponseExtractor} that prepares a {@link ResponseEntity}.
//     */
//    protected <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
//        return new ResponseEntityResponseExtractor<>(responseType);
//    }

    protected <T> RequestCallback httpEntityCallback(Object requestBody) {
        return new HttpEntityRequestCallback(requestBody);
    }

    protected <T> RequestCallback httpEntityCallback(Object requestBody, Type responseType) {
        return new HttpEntityRequestCallback(requestBody, responseType);
    }

//    private class ResponseEntityResponseExtractor<T> implements ResponseExtractor<ResponseEntity<T>> {
//
//        private final HttpMessageConverterExtractor<T> delegate;
//
//        public ResponseEntityResponseExtractor(Type responseType) {
//            if (responseType != null && !Void.class.equals(responseType)) {
//                this.delegate = null;
//                this.delegate = new HttpMessageConverterExtractor<T>(responseType, getMessageConverters(), logger);
//            }
//            else {
//                this.delegate = null;
//            }
//        }
//
//        @Override
//        public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
//            if (this.delegate != null) {
//                T body = this.delegate.extractData(response);
//                return new ResponseEntity<T>(body, response.getHeaders(), response.getStatusCode());
//            }
//            else {
//                return new ResponseEntity<T>(response.getHeaders(), response.getStatusCode());
//            }
//        }
//    }


    private static class HeadersExtractor implements ResponseExtractor<HttpHeaders> {
    private HeadersExtractor() {
    }

    public HttpHeaders extractData(ClientHttpResponse response) {
        return response.getHeaders();
    }
    }



    /**
     * Request callback implementation that prepares the request's accept headers.
     */
    private class AcceptHeaderRequestCallback implements RequestCallback {

        private final Type responseType;

        private AcceptHeaderRequestCallback(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            if (this.responseType != null) {
                Class<?> responseClass = null;
                if (this.responseType instanceof Class) {
                    responseClass = (Class<?>) this.responseType;
                }
                List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
                for (HttpMessageConverter<?> converter : getMessageConverters()) {
                    if(responseClass.getCanonicalName().contains(EntityLD.class.getCanonicalName()))
                    {
                        if(converter instanceof NgsiLDConverter) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                            return;
                        }
                    }else if (responseClass != null) {
                        if (converter.canRead(responseClass, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    }
                    else if (converter instanceof GenericHttpMessageConverter) {
                        GenericHttpMessageConverter<?> genericConverter = (GenericHttpMessageConverter<?>) converter;
                        if (genericConverter.canRead(this.responseType, null, null)) {
                            allSupportedMediaTypes.addAll(getSupportedMediaTypes(converter));
                        }
                    }
                }
                if (!allSupportedMediaTypes.isEmpty()) {
                    MediaType.sortBySpecificity(allSupportedMediaTypes);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Setting request Accept header to " +
                                allSupportedMediaTypes);
                    }
                    request.getHeaders().setAccept(allSupportedMediaTypes);
                }
            }
        }

        private List<MediaType> getSupportedMediaTypes(HttpMessageConverter<?> messageConverter) {
            List<MediaType> supportedMediaTypes = messageConverter.getSupportedMediaTypes();
            List<MediaType> result = new ArrayList<MediaType>(supportedMediaTypes.size());
            for (MediaType supportedMediaType : supportedMediaTypes) {
                if (supportedMediaType.getCharSet() != null) {
                    supportedMediaType =
                            new MediaType(supportedMediaType.getType(), supportedMediaType.getSubtype());
                }
                result.add(supportedMediaType);
            }
            return result;
        }
    }


    /**
     * Request callback implementation that writes the given object to the request stream.
     */
    private class HttpEntityRequestCallback extends AcceptHeaderRequestCallback {

        private final HttpEntity<?> requestEntity;

        private HttpEntityRequestCallback(Object requestBody) {
            this(requestBody, null);
        }

        private HttpEntityRequestCallback(Object requestBody, Type responseType) {
            super(responseType);
            if (requestBody instanceof HttpEntity) {
                this.requestEntity = (HttpEntity<?>) requestBody;
            }
            else if (requestBody != null) {
                this.requestEntity = new HttpEntity<Object>(requestBody);
            }
            else {
                this.requestEntity = HttpEntity.EMPTY;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public void doWithRequest(ClientHttpRequest httpRequest) throws IOException {
            super.doWithRequest(httpRequest);
            if (!this.requestEntity.hasBody()) {
                HttpHeaders httpHeaders = httpRequest.getHeaders();
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                if (!requestHeaders.isEmpty()) {
                    httpHeaders.putAll(requestHeaders);
                }
                if (httpHeaders.getContentLength() == -1) {
                    httpHeaders.setContentLength(0L);
                }
            }
            else {
                Object requestBody = this.requestEntity.getBody();
                Class<?> requestType = requestBody.getClass();
                HttpHeaders requestHeaders = this.requestEntity.getHeaders();
                MediaType requestContentType = requestHeaders.getContentType();

                for (HttpMessageConverter<?> messageConverter : getMessageConverters() ) {
                    if (/*(requestBody instanceof EntityLD && messageConverter instanceof NgsiLDConverter) ||*/ messageConverter.canWrite(requestType, requestContentType)){
                        if (!requestHeaders.isEmpty()) {
                            httpRequest.getHeaders().putAll(requestHeaders);
                        }
                        if (logger.isDebugEnabled()) {
                            if (requestContentType != null) {
                                logger.debug("Writing [" + requestBody + "] as \"" + requestContentType +
                                        "\" using [" + messageConverter + "]");
                            }
                            else {
                                logger.debug("Writing [" + requestBody + "] using [" + messageConverter + "]");
                            }

                        }
                        ((HttpMessageConverter<Object>) messageConverter).write(
                                requestBody, requestContentType, httpRequest);

                        String jsonStr = httpRequest.getBody().toString();

                        logger.info(jsonStr);
                        return;
                    }
                }


                String message = "Could not write request: no suitable HttpMessageConverter found for request type [" +
                        requestType.getName() + "]";
                if (requestContentType != null) {
                    message += " and content type [" + requestContentType + "]";
                }
                throw new RestClientException(message);
            }
        }
    }

}
