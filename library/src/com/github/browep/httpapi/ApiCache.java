package com.github.browep.httpapi;

import java.io.InputStream;

public interface ApiCache {
    public InputStream get(ApiMethod apiMethod);
    public boolean exists(ApiMethod apiMethod);
    public void put(ApiMethod apiMethod, InputStream inputStream);
}
