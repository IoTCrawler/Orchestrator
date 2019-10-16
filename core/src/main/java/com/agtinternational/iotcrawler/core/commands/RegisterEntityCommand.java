package com.agtinternational.iotcrawler.core.commands;

import com.agtinternational.iotcrawler.core.models.RDFModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RegisterEntityCommand extends RPCCommand {

    RDFModel model;

    public RegisterEntityCommand(RDFModel model) {
        this.model = model;
    }



    public RDFModel getModel() {
        return model;
    }

    public String toJson() {
        String json = "{'model': '"+model.toJsonLDString()+"'}";
        return "{ command: "+this.getClass().getSimpleName()+", args: "+json+"}";
    }

    public static RegisterEntityCommand fromJson(String json) {
        JsonParser parser = new JsonParser();
        JsonObject messageObj = (JsonObject)parser.parse(new String(json));
        JsonObject jsonObject = (JsonObject) messageObj.get("args");

        String modelStr = jsonObject.get("model").getAsString();
        RDFModel model = RDFModel.fromJson(modelStr.getBytes());
        return new RegisterEntityCommand(model);
    }

}
