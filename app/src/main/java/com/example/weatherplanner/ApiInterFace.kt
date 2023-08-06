package com.example.weatherplanner

import okhttp3.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterFace {

    @GET("weather")
    fun getweatherData(
        @Query("q") city: String,
        @Query("appid")appid:String,
        @Query("units") units: String
    ): retrofit2.Call<WeatherApi>
}