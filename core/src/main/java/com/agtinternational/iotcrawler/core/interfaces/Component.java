package com.agtinternational.iotcrawler.core.interfaces;

import java.io.Closeable;

public interface Component extends Closeable {

    public void init() throws Exception;
    public void run() throws Exception;
}
