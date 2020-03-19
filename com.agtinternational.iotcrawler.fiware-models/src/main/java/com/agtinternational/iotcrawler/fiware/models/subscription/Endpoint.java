package com.agtinternational.iotcrawler.fiware.models.subscription;

import org.apache.http.entity.ContentType;

import java.net.URI;
import java.net.URL;

public class Endpoint {
    URL uri;
    String accept;

    public Endpoint(URL uri, ContentType accept) {
        this.uri = uri;
        this.accept = accept.getMimeType();
    }

    public URL getUri() {
        return uri;
    }

    public void setUri(URL uri) {
        this.uri = uri;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }
}
