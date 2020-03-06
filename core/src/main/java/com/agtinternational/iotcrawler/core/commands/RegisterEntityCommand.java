package com.agtinternational.iotcrawler.core.commands;

/*-
 * #%L
 * core
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
