package com.example.tinyappdemo.tiny;

import com.example.tinyappdemo.tiny.bridge.BridgeExtension;
import com.example.tinyappdemo.tiny.bridge.GetSystemInfoExtension;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Bridge {

    private static final Map<String, Class<? extends BridgeExtension>> sExtensionMap = new ConcurrentHashMap<>();

    protected App mApp;
    protected Map<String, BridgeExtension> mExtensionMap;

    public Bridge(App app) {
        mApp = app;
        mExtensionMap = new ConcurrentHashMap<>();
    }

    public void call(String api, String params, Callback<String> callback) {
        try {
            BridgeExtension extension = findExtensionByApi(api);
            if (extension != null) {
                extension.execute(new JSONObject(params), new Callback<JSONObject>() {
                    @Override
                    public void callback(JSONObject data) {
                        if (callback != null && data != null) {
                            callback.callback(data.toString());
                        }
                    }
                });
            }
        } catch (Throwable e) {
            e.printStackTrace();
            if (callback != null) {
                callback.callback("{}");
            }
        }
    }

    public String callSync(String api, String params) {
        try {
            BridgeExtension extension = findExtensionByApi(api);
            if (extension != null) {
                return extension.executeSync(new JSONObject(params)).toString();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "{}";
    }

    protected BridgeExtension findExtensionByApi(String api) {
        try {
            BridgeExtension extension = mExtensionMap.get(api);
            if (extension == null) {
                Class<? extends BridgeExtension> clazz = sExtensionMap.get(api);
                if (clazz != null) {
                    extension = clazz.newInstance();
                    extension.init(mApp.getContext(), mApp.getAppId());
                    mExtensionMap.put(api, extension);
                }
            }
            return extension;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void register(String api, Class<? extends BridgeExtension> extension) {
        sExtensionMap.put(api, extension);
    }

    public interface Callback<T> {
        void callback(T data);
    }

    static {
        sExtensionMap.put("getSystemInfo", GetSystemInfoExtension.class);
    }
}
