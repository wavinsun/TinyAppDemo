package com.example.tinyappdemo.tiny;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebView;

import com.example.tinyappdemo.BuildConfig;

import org.json.JSONObject;

public class App {

    protected Context mContext;
    protected JSONObject mStartParams;
    protected String mAppId;
    protected Render mRender;
    protected Worker mWorker;
    protected Package mPackage;

    public App(Context context, JSONObject startParams) {
        mContext = context;
        mStartParams = startParams;
        mAppId = startParams.optString("appId");
    }

    public void onCreate() {
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        mPackage = new Package(this);
        mRender = new Render(this);
        mWorker = new Worker(this);
    }

    public Context getContext() {
        return mContext;
    }

    public String getAppId() {
        return mAppId;
    }

    public Render getRender() {
        return mRender;
    }

    public Worker getWorker() {
        return mWorker;
    }

    public Package getPackage() {
        return mPackage;
    }

    public View getView() {
        return mRender != null ? mRender.getView() : null;
    }

    public void runOnUiThread(Runnable action) {
        if (action != null) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                action.run();
            } else {
                new Handler(Looper.getMainLooper()).post(action);
            }
        }
    }

}
