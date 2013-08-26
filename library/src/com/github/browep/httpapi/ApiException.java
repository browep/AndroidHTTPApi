package com.github.browep.httpapi;

import org.apache.http.client.HttpResponseException;

public class ApiException extends HttpResponseException {
    private static String TAG = ApiException.class.getCanonicalName();

    public ApiException(int statusCode, String s) {
        super(statusCode, s);
    }

    public static class NoNetworkException extends ApiException {

        public NoNetworkException() {
            super(600, "No Network Available");
        }
    }

}
