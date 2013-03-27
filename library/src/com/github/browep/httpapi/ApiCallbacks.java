package com.github.browep.httpapi;

import java.io.IOException;

public abstract class ApiCallbacks<T extends ApiModel> {
    static String TAG = ApiCallbacks.class.getCanonicalName();
    private Class<T> clazz;

    protected ApiCallbacks(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public abstract void onSuccess(T apiModel);
    public abstract void onFailure(IOException apiException);

}
