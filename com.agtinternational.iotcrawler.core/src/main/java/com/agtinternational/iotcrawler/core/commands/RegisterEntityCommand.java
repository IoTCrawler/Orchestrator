package com.agtinternational.iotcrawler.core.commands;

/*-
 * #%L
 * core
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
