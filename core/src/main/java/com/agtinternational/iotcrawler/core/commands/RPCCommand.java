package com.agtinternational.iotcrawler.core.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RPCCommand {

    public String toJson() throws Exception {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(this);
        return "{ command: "+this.getClass().getSimpleName()+", args: "+json+"}";
    }
}
