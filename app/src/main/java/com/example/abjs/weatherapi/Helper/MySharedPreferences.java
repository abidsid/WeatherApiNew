package com.example.abjs.weatherapi.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.abjs.weatherapi.R;

/**
 * Created by Abidullah on 2/19/2018.
 */


public class MySharedPreferences {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private String counter = "counter";
    private String reportCache = "reportcache";

    public MySharedPreferences(Context ctx) {
        // prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String abc = ctx.getString(R.string.app_name);
        Log.d("Appname", abc);
        //prefs = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE);
//        prefs = ctx.getSharedPreferences(ctx.getString(R.string.app_name), Context.MODE_PRIVATE);
        prefs = ctx.getSharedPreferences(abc, Context.MODE_PRIVATE);

        editor = prefs.edit();
    }

    public int getCounter() {

        // AppGlobal.Log("usm_res_default", "id= " + prefs.getInt(defaultRestaurantNumber, 0));
        return prefs.getInt(counter, 0);
    }

    public void setCounter(int counter) {
        editor.putInt(this.counter, counter);
        editor.commit();
    }

    public String getReportCache() {
        return prefs.getString(reportCache, "");
    }

    public void setReportCache(String reportCache) {
        editor.putString(this.reportCache, reportCache);
        editor.commit();

    }
}
