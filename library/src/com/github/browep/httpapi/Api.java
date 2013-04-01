package com.github.browep.httpapi;

import android.content.Context;

public class Api {
    private static String TAG = Api.class.getCanonicalName();
    public static int UNKNOWN_ERROR = 600;

    protected Context context;
    protected ApiCache cache;
    protected ApiAdapter adapter;

    public Api(Context context, ApiCache cache, ApiAdapter adapter) {
        this.context = context;
        this.cache = cache;
        this.adapter = adapter;
    }

    public void get(ApiMethod apiMethod, ApiCallbacks apiCallbacks) {
        new ApiTask(context,
                apiCallbacks,
                adapter,
                cache,
                apiMethod).execute((Void[]) null);

    }
}
