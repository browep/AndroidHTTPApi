package com.github.browep.httpapi;

import com.github.browep.extlogging.Log;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class HttpUtils {
    private static String TAG = HttpUtils.class.getCanonicalName();

    public static void logHttpObject(HttpUriRequest method) {
        Log.d(TAG, "HOST:" + method.getURI().getHost());
        Log.d(TAG, "METHOD:" + method.getMethod());
        Log.d(TAG, "PATH:" + method.getURI().getPath() );
        Log.d(TAG, "QUERY:" + method.getURI().getQuery() );

        HttpParams httpParams = method.getParams();
        Class clazz = httpParams.getClass();

        // DEBUG, very slow methods, should be removed before release to production
        try {
            Field fields[] = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                if ("parameters".equals(name)) {
                    Log.d(TAG, "Parameters: " + field.get(httpParams));
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            Log.d(TAG, "error trying to get HttpParams: ", e);
        }

        Header[] headers = method.getAllHeaders();
        Log.d(TAG, "Headers:" + headers.length);

        for (Header header : headers) {
            Log.d(TAG, "\t\t" + header.getName() + ": " + header.getValue());
        }

        if (method instanceof HttpPost) {
            try {
                String bodyStr = EntityUtils.toString(((HttpPost) method).getEntity());
                Log.d(TAG, "BODY:\n" + bodyStr);
                File fileToWrite = new File("/mnt/sdcard/", "tmp.xml");
                //                IOUtil.write(fileToWrite,bodyStr);
                //                LogIt.d(HTTPUtils.class,"wrote " + method.getURI() + " to " + fileToWrite.getAbsolutePath());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (UnsupportedOperationException e) {
                Log.e(TAG, "logHttpObject:" + e.getMessage());
            }
        }

    }
}
