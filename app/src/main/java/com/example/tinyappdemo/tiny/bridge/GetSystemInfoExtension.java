package com.example.tinyappdemo.tiny.bridge;

import android.os.Build;

import com.example.tinyappdemo.tiny.Bridge;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetSystemInfoExtension extends BridgeExtension {

    @Override
    public JSONObject executeSync(JSONObject params) {
        try {
            JSONObject result = new JSONObject();
            result.put("success", true);
            result.put("platform", "Android");
            result.put("system", Build.VERSION.RELEASE);
            result.put("model", Build.MODEL);
            result.put("brand", Build.BRAND);
            result.put("screenWidth", mContext.getResources().
                    getDisplayMetrics().widthPixels);
            result.put("screenHeight", mContext.getResources().
                    getDisplayMetrics().heightPixels);
            result.put("app", "TinyAppDemo");
            result.put("version", "1.0");
            result.put("currentTime", new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void execute(JSONObject params, Bridge.Callback<JSONObject> callback) {
        if (callback != null) {
            callback.callback(executeSync(params));
        }
    }

}
