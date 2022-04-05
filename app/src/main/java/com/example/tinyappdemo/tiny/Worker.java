package com.example.tinyappdemo.tiny;

import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.Serializable;

public class Worker {

    protected App mApp;
    protected WebView mWebWorker;
    protected Bridge mBridge;

    public Worker(App app) {
        mApp = app;
        mBridge = new Bridge(app);
        mWebWorker = new WebView(mApp.getContext());
        mWebWorker.getSettings().setJavaScriptEnabled(true);
        mWebWorker.addJavascriptInterface(new Serializable() {

            @JavascriptInterface
            public void nativeCall(String api, String params, String id) {
                mBridge.call(api, params, new Bridge.Callback<String>() {
                    @Override
                    public void callback(String data) {
                        mApp.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWebWorker.loadUrl(String.format(
                                        "javascript:nativeCallResult('%s','%s')", id, data));
                            }
                        });
                    }
                });
            }

            @JavascriptInterface
            public String nativeCallSync(String api, String params) {
                return mBridge.callSync(api, params);
            }

        }, "my");
        mWebWorker.addJavascriptInterface(new Serializable() {

            @JavascriptInterface
            public void init(String data, String methods) {
                try {
                    mApp.getRender().init(new JSONObject(data), new JSONObject(methods));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @JavascriptInterface
            public void setData(String data) {
                try {
                    mApp.getRender().setData(new JSONObject(data));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        }, "render");
        mWebWorker.setWebViewClient(new WebViewClient() {
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return mApp.getPackage().shouldInterceptRequest(view, url);
            }
        });
        String workerUrl = String.format("https://%s.tiny.example.com/worker.html", mApp.getAppId());
        String workerHtml = mApp.getPackage().getResourceText("https://6688.tiny.example.com/index.worker.html");
        workerHtml = workerHtml.replace("${APP_ID}", mApp.getAppId());
        mWebWorker.loadDataWithBaseURL(workerUrl, workerHtml, "text/html", "utf-8", null);
    }

    public void callWorker(String name, String params) {
        mApp.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebWorker.loadUrl(String.format("javascript:callWorker('%s','%s')", name, params));
            }
        });
    }

}
