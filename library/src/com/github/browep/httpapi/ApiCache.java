package com.github.browep.httpapi;

import java.io.InputStream;

public interface ApiCache {
    public InputStream get(ApiMethod apiMethod);
}
