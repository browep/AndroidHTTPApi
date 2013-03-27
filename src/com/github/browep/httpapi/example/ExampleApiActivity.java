package com.github.browep.httpapi.example;

import android.app.Activity;
import android.os.Bundle;
import com.github.browep.httpapi.Api;
import com.github.browep.httpapi.ApiMethod;

public class ExampleApiActivity extends Activity {
    private static String TAG = ExampleApiActivity.class.getCanonicalName();
    private Api api;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static class ExampleNonCachingApiMethodImpl implements ApiMethod {

        public HttpProtocol getProtocol() {
            return HttpProtocol.HTTP;
        }

        public String getHost() {
            return "httpbin.org";
        }

        public String getMethod() {
            return "/stream/20";
        }

        public String getCacheKey() {
            return null;
        }
    }

}
