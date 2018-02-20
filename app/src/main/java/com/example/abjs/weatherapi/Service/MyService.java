package com.example.abjs.weatherapi.Service;

/**
 * Created by Abidullah on 2/19/2018.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.JsonObject;
import com.example.abjs.weatherapi.Helper.APIs;
import com.example.abjs.weatherapi.Helper.InternetConnectionDetector;
import com.example.abjs.weatherapi.Helper.MySharedPreferences;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyService extends Service {



    public static final String INTENT_FILTER_WEATHER_REPORT = "com.example.abjs.app.WEATHER_UPDATED";
    private static final int TIMER_DELAY_IN_SEC = 5 * 60; // seconds
    Handler handler;
    Runnable runnable;
    Retrofit retrofit;
    InternetConnectionDetector connectionDetector;
    MySharedPreferences mySharedPreferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service","start");
        connectionDetector = new InternetConnectionDetector(getApplicationContext());
        mySharedPreferences = new MySharedPreferences(getApplicationContext());

        initRetrofit();
        setTimer();
        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(APIs.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void setTimer() {
        handler = new Handler();
        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                connectionDetector = new InternetConnectionDetector(getApplicationContext());
                if (connectionDetector.isConnectingToInternet()) {
                    requestToGetReport();
                }
                handler.postDelayed(runnable, TIMER_DELAY_IN_SEC * 1000);
            }
        };
        handler.postDelayed(runnable, 10);  // ms
    }

    private void requestToGetReport() {

        APIs.GetDataService service = retrofit.create(APIs.GetDataService.class);
        Map<String, String> parameters = new HashMap<>();

        Call<JsonObject> call = service.getObject(APIs.GET_WEATHER_REPORT, parameters);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("usm_response", "code= " + response.code() + " ,data= " + response.body());
                JsonObject object = response.body();
                if (object == null) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());

                    if (jsonObject.has("currently")) {
                        Log.d("usm_json", "data= " + jsonObject.getJSONObject("currently").toString());

                        mySharedPreferences.setReportCache(jsonObject.getJSONObject("currently").toString());
                        mySharedPreferences.setCounter(mySharedPreferences.getCounter() + 1);

                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(INTENT_FILTER_WEATHER_REPORT));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();


            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
