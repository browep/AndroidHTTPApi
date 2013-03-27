package com.github.browep.httpapi;

import android.content.Context;
import android.util.Log;
import org.apache.http.client.methods.HttpGet;

import java.net.URI;
import java.net.URISyntaxException;

public class Api {
    private static String TAG = Api.class.getCanonicalName();
    public static int UNKNOWN_ERROR = 600;

    private Context context;
    private ApiCache cache;
    private ApiAdapter adapter;

    public Api(Context context, ApiCache cache, ApiAdapter adapter) {
        this.context = context;
        this.cache = cache;
        this.adapter = adapter;
    }

    public void get(ApiMethod apiMethod, ApiCallbacks apiCallbacks) {
        try {
            HttpGet method = new HttpGet(new URI(apiMethod.getProtocol().toString().toLowerCase(), apiMethod.getHost(),
                    apiMethod.getPath(), null));
            new ApiTask(context, method, apiCallbacks, adapter, cache).execute(null);

        } catch (URISyntaxException e) {
            Log.e(TAG, apiMethod.toString(), e);
        }
    }
}
