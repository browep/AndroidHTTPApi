package com.github.browep.httpapi;

import org.apache.http.client.methods.HttpPost;

import java.io.InputStream;

public interface ApiAdapter {
    public ApiModel parseToModel(Class clazz, InputStream inputStream);

    void createRequest(HttpPost httpPost, ApiMethod apiMethod);
}
