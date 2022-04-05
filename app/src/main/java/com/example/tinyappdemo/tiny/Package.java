package com.example.tinyappdemo.tiny;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Package {

    public static final String TINY_FRAMEWORK_APP_ID = "6688";

    protected App mApp;

    public Package(App app) {
        mApp = app;
    }

    public String getResourceText(String url) {
        byte[] bytes = getResourceBytes(url);
        if (bytes != null) {
            try {
                return new String(bytes, "utf-8");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public byte[] getResourceBytes(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.endsWith("/favicon.ico")) {
            return new byte[0];
        }
        InputStream is = null;
        try {
            Uri uri = Uri.parse(url);
            if (!uri.isHierarchical()) {
                return null;
            }
            String host = uri.getHost();
            if (!host.endsWith("tiny.example.com")) {
                return null;
            }
            int dotIndex = host.indexOf('.');
            if (dotIndex == 1) {
                return null;
            }
            String appId = host.substring(0, dotIndex);
            if (!TextUtils.equals(appId, mApp.getAppId()) &&
                    !TextUtils.equals(appId, TINY_FRAMEWORK_APP_ID)) {
                return null;
            }
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            is = mApp.getContext().getAssets().open("tiny/res/" + appId + uri.getPath());
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                result.write(bytes, 0, len);
            }
            return result.toByteArray();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        try {
            byte[] bytes = getResourceBytes(url);
            if (bytes == null) {
                return null;
            }
            return new WebResourceResponse(MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    MimeTypeMap.getFileExtensionFromUrl(url)), "utf-8",
                    new ByteArrayInputStream(bytes));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}
