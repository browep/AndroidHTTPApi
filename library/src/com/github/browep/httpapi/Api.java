package com.github.browep.httpapi;

import android.content.Context;

public class Api {
    private static String TAG = Api.class.getCanonicalName();
    public static int UNKNOWN_ERROR = 600;

    protected Context context;
    protected ApiCache cache;
    protected ApiAdapter adapter;
    protected ApiAuthenticator authenticator;

    public Api(Context context, ApiCache cache, ApiAdapter adapter, ApiAuthenticator authenticator) {
        this.context = context;
        this.cache = cache;
        this.adapter = adapter;
        this.authenticator = authenticator;
    }

    public void get(ApiMethod apiMethod, ApiCallbacks apiCallbacks) {
        new ApiTask(context,
                apiCallbacks,
                adapter,
                cache,
                apiMethod, authenticator).execute((Void[]) null);

    }
}
