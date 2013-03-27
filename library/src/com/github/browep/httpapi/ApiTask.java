package com.github.browep.httpapi;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.URI;

public class ApiTask extends AsyncTask<Void, Void, ApiModel> {
    private static String TAG = ApiTask.class.getCanonicalName();

    private Context context;
    private HttpUriRequest method;
    private ApiCallbacks apiCallbacks;
    private ApiAdapter adapter;
    private final AndroidHttpClient client;
    private Exception exception;
    private ApiCache cache;
    private ApiMethod apiMethod;

    public ApiTask(Context context,
                   ApiCallbacks apiCallbacks,
                   ApiAdapter adapter,
                   ApiCache cache, ApiMethod apiMethod) {
        this.context = context;
        this.apiCallbacks = apiCallbacks;
        this.adapter = adapter;
        this.cache = cache;
        this.apiMethod = apiMethod;

        client = AndroidHttpClient.newInstance(null, context);

    }

    @Override protected ApiModel doInBackground(Void... voids) {
        try {
            if (cache != null && cache.exists(apiMethod)) {

                return adapter.parseToModel(apiCallbacks.getClazz(), cache.get(apiMethod));

            } else {
                HttpUriRequest httpUriRequest = null;

                URI uri = new URI(apiMethod.getProtocol().toString().toLowerCase(), apiMethod.getHost(),
                        apiMethod.getPath(), null);

                switch (apiMethod.getMethod()) {
                    case GET:
                        httpUriRequest = new HttpGet(uri);
                        break;
                    case POST:
                        httpUriRequest = new HttpPost(uri);
                        break;
                }

                HttpResponse httpResponse = client.execute(httpUriRequest);
                if( cache != null && !TextUtils.isEmpty(apiMethod.getCacheKey())){
                    cache.put(apiMethod,httpResponse.getEntity().getContent());
                    return adapter.parseToModel(apiCallbacks.getClazz(), cache.get(apiMethod));
                } else {
                    return adapter.parseToModel(apiCallbacks.getClazz(), httpResponse.getEntity().getContent());
                }

            }
        } catch (Exception e) {
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
            apiCallbacks.onFailure(exception != null ? exception : new ApiException(Api.UNKNOWN_ERROR, ""));
        }
    }

}

