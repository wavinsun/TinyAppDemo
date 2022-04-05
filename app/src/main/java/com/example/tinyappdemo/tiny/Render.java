package com.example.tinyappdemo.tiny;

import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Render {

    protected App mApp;
    protected WebView mWebRender;
    protected List<Runnable> mRenderEvents;
    protected boolean mRenderReady;

    public Render(App app) {
        mApp = app;
        mRenderEvents = new LinkedList<>();
        mWebRender = new WebView(mApp.getContext());
        mWebRender.getSettings().setJavaScriptEnabled(true);
        mWebRender.addJavascriptInterface(new Serializable() {

            @JavascriptInterface
            public void call(String name, String params) {
                mApp.getWorker().callWorker(name, params);
            }

            @JavascriptInterface
            public void onRenderReady() {
                mApp.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRenderReady = true;
                        for (Runnable action : mRenderEvents) {
                            action.run();
                        }
                        mRenderEvents.clear();
                    }
                });
            }

        }, "worker");
        mWebRender.setWebViewClient(new WebViewClient() {
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return mApp.getPackage().shouldInterceptRequest(view, url);
            }
        });
        String appUrl = String.format("https://%s.tiny.example.com/index.html", mApp.getAppId());
        String frameworkHtml = mApp.getPackage().getResourceText("https://6688.tiny.example.com/index.html");
        String appHtml = mApp.getPackage().getResourceText(appUrl);
        String finalHtml = frameworkHtml.replace("${APP_DIV}", appHtml);
        mWebRender.loadDataWithBaseURL(appUrl, finalHtml, "text/html", "utf-8", null);
    }

    public View getView() {
        return mWebRender;
    }

    public void init(JSONObject data, JSONObject methods) {
        mApp.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!mRenderReady) {
                    mRenderEvents.add(this);
                    return;
                }
                mWebRender.loadUrl(String.format("javascript:initRender(%s,%s)", data.toString(), methods.toString()));
            }
        });
    }

    public void setData(JSONObject data) {
        mApp.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!mRenderReady) {
                    mRenderEvents.add(this);
                    return;
                }
                mWebRender.loadUrl(String.format("javascript:setData(%s)", data.toString()));
            }
        });
    }
}
