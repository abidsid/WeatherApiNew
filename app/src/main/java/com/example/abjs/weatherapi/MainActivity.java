package com.example.abjs.weatherapi;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.abjs.weatherapi.Helper.MySharedPreferences;
import com.example.abjs.weatherapi.Service.MyService;


import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Context context;
    TextView counter_tv, summary_tv, temperature_tv, dewPoint_tv, humidity_tv, pressure_tv, windSpeed_tv, cloudCover_tv, ozone_tv, visibility_tv;
    MySharedPreferences mySharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);

        mySharedPreferences = new MySharedPreferences(context);
        if (!isMyServiceRunning(MyService.class)) {
            startBackgroundService();
        }
        setViews();

        setContent();

        IntentFilter filter = new IntentFilter(MyService.INTENT_FILTER_WEATHER_REPORT);

        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                setContent();


            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(myReceiver, filter);

    }

    private void setViews() {

        counter_tv = findViewById(R.id.counter_tv);
        summary_tv = findViewById(R.id.summary_tv);
        temperature_tv = findViewById(R.id.temperature_tv);
        dewPoint_tv = findViewById(R.id.dewPoint_tv);
        humidity_tv = findViewById(R.id.humidity_tv);
        pressure_tv = findViewById(R.id.pressure_tv);
        windSpeed_tv = findViewById(R.id.windSpeed_tv);
        cloudCover_tv = findViewById(R.id.cloudCover_tv);
        ozone_tv = findViewById(R.id.ozone_tv);
        visibility_tv = findViewById(R.id.visibility_tv);
    }
    public void setContent() {
        try {
            if (!mySharedPreferences.getReportCache().isEmpty()) {
                counter_tv.setText("Counter: " + String.valueOf(mySharedPreferences.getCounter()));
                JSONObject currentlyObj = new JSONObject(mySharedPreferences.getReportCache());

                summary_tv.setText("Summary: " + currentlyObj.getString("summary"));
                temperature_tv.setText("Temperature: " + String.valueOf(currentlyObj.getDouble("temperature")));
                dewPoint_tv.setText("DewPoint: " + String.valueOf(currentlyObj.getDouble("dewPoint")));
                humidity_tv.setText("Humidity: " + String.valueOf(currentlyObj.getDouble("humidity")));
                pressure_tv.setText("Pressure: " + String.valueOf(currentlyObj.getDouble("pressure")));
                windSpeed_tv.setText("WindSpeed: " + String.valueOf(currentlyObj.getDouble("windSpeed")));
                cloudCover_tv.setText("CloudCover: " + String.valueOf(currentlyObj.getDouble("cloudCover")));
                visibility_tv.setText("Visibility: " + String.valueOf(currentlyObj.getDouble("visibility")));
                ozone_tv.setText("Ozone: " + String.valueOf(currentlyObj.getDouble("ozone")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void startBackgroundService() {
        startService(new Intent(getApplicationContext(), MyService.class));
    }
}


