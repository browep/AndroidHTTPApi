package com.github.browep.api.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.github.browep.httpapi.Api;
import com.github.browep.httpapi.ApiAdapter;
import com.github.browep.httpapi.ApiCache;
import com.github.browep.httpapi.ApiCallbacks;
import com.github.browep.httpapi.ApiMethod;
import com.github.browep.httpapi.ApiModel;

import org.apache.http.client.methods.HttpPost;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ExampleApiActivity extends Activity {
    private static String TAG = ExampleApiActivity.class.getCanonicalName();
    private Api api;
    private TextView textView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textView = new TextView(this);
        textView.setMovementMethod(new ScrollingMovementMethod());
        setContentView(textView);

        api = new Api(this, new ExampleApiCacheImpl(getCacheDir()), new GsonApiAdapterImpl(), null);

        final long nonCacheStart = System.currentTimeMillis();
        api.get(new ExampleNonCachingApiMethodImpl(),new ApiCallbacks<GetApiModel>(GetApiModel.class) {
            public void onSuccess(GetApiModel apiModel) {
                append("NON-CACHE RESPONSE: " + apiModel + " in " + (System.currentTimeMillis() - nonCacheStart) + "ms");
            }

            public void onFailure(Exception apiException) {
                Log.e(TAG,apiException.getMessage(),apiException);
            }
        });

        final long cacheStart = System.currentTimeMillis();
        api.get(new ExampleCachingApiMethodImpl(), new ApiCallbacks<GetApiModel>(GetApiModel.class) {
            @Override public void onSuccess(GetApiModel apiModel) {
                append("CACHE RESPONE: "+ apiModel + " in " + (System.currentTimeMillis() - cacheStart) + "ms");
            }

            @Override public void onFailure(Exception apiException) {
                Log.e(TAG,apiException.getMessage(),apiException);
            }
        });
    }

    public static class ExampleNonCachingApiMethodImpl implements ApiMethod {

        public HttpMethod getMethod() {
            return HttpMethod.GET;
        }

        public HttpProtocol getProtocol() {
            return HttpProtocol.HTTP;
        }

        public String getHost() {
            return "httpbin.org";
        }

        public String getPath() {
            return "/get";
        }

        public String getCacheKey() {
            return null;
        }

        public String getQueryString() {
            return null;
        }
    }

    public static class ExampleCachingApiMethodImpl extends ExampleNonCachingApiMethodImpl {
        @Override public String getCacheKey() {
            return "get";
        }
    }

    public static class ExampleApiCacheImpl implements ApiCache {

        public File cacheDir;

        public ExampleApiCacheImpl(File cacheDir) {
            this.cacheDir = cacheDir;
        }

        public boolean exists(ApiMethod apiMethod) {
            String cacheKey = apiMethod.getCacheKey();
            return cacheDir != null && !TextUtils.isEmpty(cacheKey) && (new File(cacheDir, cacheKey)).exists();
        }

        public void put(ApiMethod apiMethod, InputStream inputStream) {
            String cacheKey = apiMethod.getCacheKey();

            if (!TextUtils.isEmpty(cacheKey)) {
                try {

                    OutputStream stream = new BufferedOutputStream(
                            new FileOutputStream(
                                    new File(cacheDir, cacheKey)));
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        stream.write(buffer, 0, len);
                    }
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void clear() {
            for (File file : cacheDir.listFiles()) {
                file.delete();
            }
        }

        public InputStream get(ApiMethod apiMethod) {
            if (exists(apiMethod)) {
                try {
                    return new FileInputStream(new File(cacheDir, apiMethod.getCacheKey()));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return null;
            }
        }
    }

    public static class GsonApiAdapterImpl implements ApiAdapter {

        private final Gson gson;

        public GsonApiAdapterImpl() {
            gson = new GsonBuilder().create();
        }

        public ApiModel parseToModel(Class clazz, InputStream inputStream) {
            return (ApiModel) gson.fromJson(new InputStreamReader(inputStream), clazz);
        }

        public void createRequest(HttpPost httpPost, ApiMethod apiMethod) {
            // create POST body here
        }
    }

    public static class GetApiModel implements ApiModel {
        String url;
        String origin;

        @Override public String toString() {
            return "GetApiModel{" +
                    "origin='" + origin + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    private void append(String str){
        textView.setText(textView.getText() +  str + "\n");
        Log.d(TAG,str);
    }


}