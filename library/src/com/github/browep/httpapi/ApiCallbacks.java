package com.github.browep.httpapi;

import java.io.IOException;

public interface ApiCallbacks {
    static String TAG = ApiCallbacks.class.getCanonicalName();
    public void onSuccess(ApiModel apiModel);
    public void onFailure(IOException apiException);

}
