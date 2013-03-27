package com.github.browep.httpapi;

import java.io.InputStream;

public interface ApiAdapter {
    public ApiModel parseToModel(InputStream inputStream);
}
