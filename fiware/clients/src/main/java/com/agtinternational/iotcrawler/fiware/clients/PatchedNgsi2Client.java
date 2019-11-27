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
