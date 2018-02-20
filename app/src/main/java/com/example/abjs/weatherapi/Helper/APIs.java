package com.example.abjs.weatherapi.Helper;

/**
 * Created by Abidullah on 2/19/2018.
 */

import java.util.Map;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;



public class APIs {
    public static final String BASE_URL = "https://api.darksky.net/forecast/c63d1a62f0f56add8fff7deb5f217aac/";
    public static final String GET_WEATHER_REPORT = "37.8267,-122.4233";

    public interface GetDataService {
        @GET
        Call<JsonObject> getObject(@Url String url, @QueryMap Map<String, String> options);
    }
}
