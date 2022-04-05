package com.example.tinyappdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tinyappdemo.tiny.App;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    protected App mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mApp = new App(this, new JSONObject().put("appId", "20220404"));
            mApp.onCreate();
            setContentView(mApp.getView());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}