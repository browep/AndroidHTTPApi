package com.github.browep.api.impl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.github.browep.httpapi.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class ExampleApiActivity extends Activity {
    private static String TAG = ExampleApiActivity.class.getCanonicalName();
    private Api api;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = new Api(this, new ExampleApiCacheImpl(getCacheDir()), new GsonApiAdapterImpl());

        api.get(new ExampleNonCachingApiMethodImpl(),new ApiCallbacks<GetApiModel>(GetApiModel.class) {
            public void onSuccess(GetApiModel apiModel) {
                Log.d(TAG,"recieved: " + apiModel);
            }

            public void onFailure(IOException apiException) {
                Log.e(TAG,apiException.getMessage(),apiException);
            }
        });
    }

    public static class ExampleNonCachingApiMethodImpl implements ApiMethod {

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
    }

    public static class ExampleApiCacheImpl implements ApiCache {

        public File cacheDir;

        public ExampleApiCacheImpl(File cacheDir) {
            this.cacheDir = cacheDir;
        }

        public boolean exists(ApiMethod apiMethod) {
            return cacheDir != null && (new File(cacheDir, apiMethod.getCacheKey())).exists();
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

}