package com.stormnet.cp.server.command.imp;

import com.stormnet.cp.server.command.FirstCommand;
import org.json.JSONObject;
import org.json.JSONWriter;

public class NotFoundCommand implements FirstCommand {
    @Override
    public void process(JSONObject requestData, JSONWriter jsonWriter) {

    }
}
