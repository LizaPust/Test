package com.stormnet.cp.server.command;

import org.json.JSONObject;
import org.json.JSONWriter;

import javax.xml.transform.TransformerException;

public interface FirstCommand {

    void process(JSONObject requestData, JSONWriter jsonWriter) throws TransformerException;
}
