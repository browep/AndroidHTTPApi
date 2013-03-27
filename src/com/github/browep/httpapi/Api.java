package com.github.browep.httpapi;

import android.content.Context;

public class Api {
    private static String TAG = Api.class.getCanonicalName();
    public static int UNKNOWN_ERROR = 600;

    private Context context;

    public Api(Context context) {
        this.context = context;
    }

    public void get(ApiMethod apiMethod, ApiCallbacks apiCallbacks){
//        new ApiTask(context, new HttpGet(), apiCallbacks, )
    }
}
