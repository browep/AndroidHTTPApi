package com.github.browep.httpapi;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

public class ApiTask extends AsyncTask<Void, Void, ApiModel> {
    private static String TAG = ApiTask.class.getCanonicalName();

    private Context context;
    private HttpUriRequest method;
    String url;
    private ApiCallbacks apiCallbacks;
    private ApiAdapter adapter;
    private final AndroidHttpClient client;
    private IOException exception;
    private ApiCache cache;

    public ApiTask(Context context,
                   HttpUriRequest method,
                   ApiCallbacks apiCallbacks,
                   ApiAdapter adapter,
                   ApiCache cache) {
        this.context = context;
        this.method = method;
        this.apiCallbacks = apiCallbacks;
        this.adapter = adapter;
        this.cache = cache;
        client = AndroidHttpClient.newInstance(null, context);

    }

    @Override protected ApiModel doInBackground(Void... voids) {
        try {
            HttpResponse httpResponse = client.execute(method);
            return adapter.parseToModel(apiCallbacks.getClazz(),httpResponse.getEntity().getContent());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            exception = e;
        } finally {
            if (client != null)
                client.close();
        }

        return null;
    }

    @Override protected void onPostExecute(ApiModel apiModel) {
        if (apiModel != null) {
            apiCallbacks.onSuccess(apiModel);
        } else {
            apiCallbacks.onFailure(exception != null ? exception : new ApiException(Api.UNKNOWN_ERROR,""));
        }
    }
}

