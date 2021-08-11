package com.stormnet.cp.utils;

import org.json.JSONTokener;
import org.json.JSONWriter;

import java.io.*;
import java.net.Socket;

public class SocketJsonStreams {

    private BufferedReader reader;

    private BufferedWriter writer;

    private JSONWriter jsonWriter;

    private JSONTokener jsonTokener;

    public SocketJsonStreams(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        writer = new BufferedWriter(osw);
        jsonWriter = new JSONWriter(writer);

        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        reader = new BufferedReader(isr);
        jsonTokener = new JSONTokener(reader);
    }

    public void flushWriter() throws IOException {
        writer.flush();
    }

    public void closeStreams() throws IOException {
        reader.close();
        writer.close();
    }

    public JSONWriter getJsonWriter() {
        return jsonWriter;
    }

    public JSONTokener getJsonTokener() {
        return jsonTokener;
    }
}