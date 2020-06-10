package com.chengte99.itstar19.model;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class AndroidJS extends Object {

    private static final String TAG = AndroidJS.class.getSimpleName();

    @JavascriptInterface
    public void hello(String message) {
        Log.d(TAG, "hello: " + message);
    }

    @JavascriptInterface
    public void postMessage(String message) {
        Log.d(TAG, "postMessage: " + message);
    }

    @JavascriptInterface
    public void identifyMessage(String message) {
        Log.d(TAG, "identifyMessage: " + message);
    }
}
