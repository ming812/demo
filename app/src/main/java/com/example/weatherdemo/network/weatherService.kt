package com.example.weatherdemo.network

import com.example.weatherdemo.model.CityWeatherDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query


interface weatherService{
//    @GET("users/{user}/repos")
//    fun listRepos(@Path("user") user: String?): Call<List<Repo?>?>?
@GET("data/2.5/onecall")
fun getWeather(
    @Query("lat") lat : String,
    @Query("lon") lon : String,
    @Query("exclude") exclude : String = "minutely,hourly,daily,alerts",
    @Query("appid") appid : String = "95d190a434083879a6398aafd54d9e73",
    @Query("units") units : String = "imperial"
) : Call<CityWeatherDetail>

}

fun retrofit() : Retrofit{
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit
}