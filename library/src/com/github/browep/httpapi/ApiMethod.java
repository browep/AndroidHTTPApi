package com.github.browep.httpapi;

public interface ApiMethod {
    static final String TAG = ApiMethod.class.getCanonicalName();

    public static enum HttpProtocol {
        HTTP, HTTPS
    }

    public static enum HttpMethod {
        GET, POST
    }

    public HttpMethod getMethod();

    public HttpProtocol getProtocol();

    public String getHost();

    public String getPath();

    public String getCacheKey();

}
