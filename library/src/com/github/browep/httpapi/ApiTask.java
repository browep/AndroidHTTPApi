package com.github.browep.httpapi;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.URI;

public class ApiTask extends AsyncTask<Void, Void, ApiModel> {
    private static String TAG = ApiTask.class.getCanonicalName();

    protected Context context;
    private HttpUriRequest method;
    private ApiCallbacks apiCallbacks;
    private ApiAdapter adapter;
    private final AndroidHttpClient client;
    private Exception exception;
    private ApiCache cache;
    private ApiMethod apiMethod;
    private ApiAuthenticator authenticator;

    public ApiTask(Context context,
                   ApiCallbacks apiCallbacks,
                   ApiAdapter adapter,
                   ApiCache cache,
                   ApiMethod apiMethod, ApiAuthenticator authenticator) {
        this.context = context;
        this.apiCallbacks = apiCallbacks;
        this.adapter = adapter;
        this.cache = cache;
        this.apiMethod = apiMethod;
        this.authenticator = authenticator;

        client = getAndroidHttpClient();
    }

    public AndroidHttpClient getAndroidHttpClient() {
        return AndroidHttpClient.newInstance("Android Client 0.0.1");
    }

    @Override protected ApiModel doInBackground(Void... voids) {
        return doInBackground();
    }

    public ApiModel doInBackground() {
        try {
            if (cache != null && cache.exists(apiMethod)) {

                return adapter.parseToModel(apiCallbacks.getClazz(), cache.get(apiMethod));

            } else {
                HttpUriRequest httpUriRequest = null;

                URI uri = new URI(apiMethod.getProtocol().toString().toLowerCase(),
                        apiMethod.getHost(),
                        apiMethod.getPath(),
                        apiMethod.getQueryString(), null);

                switch (apiMethod.getMethod()) {
                    case GET:
                        httpUriRequest = new HttpGet(uri);
                        break;
                    case POST:
                        httpUriRequest = new HttpPost(uri);
                        HttpPost httpPost = (HttpPost) httpUriRequest;
                        adapter.createRequest(httpPost, apiMethod);
                        break;
                }

                if (authenticator != null){
                    authenticator.authenticate(httpUriRequest);
                }

                httpUriRequest.addHeader("Accept","*/*");

//                HttpUtils.logHttpObject(httpUriRequest);

                HttpResponse httpResponse = client.execute(httpUriRequest);

                if (!isCancelled()) {// successful call
                    if (httpResponse != null && httpResponse.getStatusLine() != null
                            && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        if (cache != null && !TextUtils.isEmpty(apiMethod.getCacheKey())) {
                            cache.put(apiMethod, httpResponse.getEntity().getContent());
                            return adapter.parseToModel(apiCallbacks.getClazz(), cache.get(apiMethod));
                        } else {
                            return adapter.parseToModel(apiCallbacks.getClazz(), httpResponse.getEntity().getContent());
                        }
                    } else {

                        // unsuccessful call
                        if (httpResponse != null) {
                            throw new HttpResponseException(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase());
                        } else {
                            throw new Exception("unknown connection issue");
                        }

                    }
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

    @Override protected void onCancelled() {
        super.onCancelled();
        Log.d(TAG, "cancelled: " + (apiMethod != null ? apiMethod.getPath() : null));
    }


}

