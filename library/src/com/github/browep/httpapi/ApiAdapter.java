package com.github.browep.httpapi;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.io.InputStream;

public interface ApiAdapter {
    public ApiModel parseToModel(Class clazz, String string);

    public ApiModel parseToModel(Class clazz, InputStream inputStream);

    void createRequest(HttpPost httpPost, ApiMethod apiMethod);

    void createRequest(HttpPut httpPut, ApiMethod apiMethod);

}
