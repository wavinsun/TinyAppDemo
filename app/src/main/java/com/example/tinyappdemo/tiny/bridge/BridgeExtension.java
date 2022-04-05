package com.example.tinyappdemo.tiny.bridge;

import android.content.Context;

import com.example.tinyappdemo.tiny.Bridge;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BridgeExtension {

    protected Context mContext;
    protected String mAppId;

    public void init(Context context, String appId) {
        mContext = context;
        mAppId = appId;
    }

    public JSONObject executeSync(JSONObject params) {
        try {
            return new JSONObject().put("success", false).
                    put("errMsg", "invalid operation");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract void execute(JSONObject params, Bridge.Callback<JSONObject> callback);

}
