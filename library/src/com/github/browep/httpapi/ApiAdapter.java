package com.github.browep.httpapi;

import org.apache.http.entity.mime.MultipartEntity;

import java.io.IOException;
import java.io.InputStream;

public interface ApiAdapter {
    public ApiModel parseToModel(Class clazz, InputStream inputStream);
    public MultipartEntity toMultipartEntity(ApiMethod apiMethod) throws IOException;
}
