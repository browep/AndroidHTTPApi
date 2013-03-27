package com.github.browep.httpapi;

public interface ApiMethod {
    static final String TAG = ApiMethod.class.getCanonicalName();

    public static enum HttpProtocol {
        HTTP, HTTPS
    }

    public HttpProtocol getProtocol();

    public String getHost();

    public String getMethod();

    public String getCacheKey();

}
