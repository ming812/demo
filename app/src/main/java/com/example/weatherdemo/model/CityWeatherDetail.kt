package com.example.weatherdemo.model

import com.google.gson.annotations.SerializedName

data class CityWeatherDetail(
    @SerializedName("lat")val lat : String,
    @SerializedName("lon")val lon : String,
    @SerializedName("timezone")val timezone : String,
    @SerializedName("timezone_offset")val timezone_offset : Double,
    @SerializedName("current")val current : Current
)

data class Current(
    @SerializedName("dt")val dt : Long,
    @SerializedName("sunrise")val sunrise : Long,
    @SerializedName("sunset")val sunset : Long,
    @SerializedName("temp")val temp : Float,
    @SerializedName("feels_like") val feels_like : Float,
    @SerializedName("pressure")val pressure : Float,
    @SerializedName("humidity")val humidity : Float,
    @SerializedName("dew_point") val dew_point : Float,
    @SerializedName("uvi")val uvi : Float,
    @SerializedName("clouds")val clouds : Float,
    @SerializedName("visibility")val visibility : Float,
    @SerializedName("wind_speed")val wind_speed : Float,
    @SerializedName("wind_deg")val wind_deg : Float,
    @SerializedName("weather")val weather : List<Weather>,
    @SerializedName("rain")val rain : Rain
)

data class Weather(
    @SerializedName("id")val id : Int,
    @SerializedName("main")val main : String,
    @SerializedName("description") val description : String,
    @SerializedName("icon")val icon : String

)

data class Rain(
    @SerializedName("1h")val oneH : Float
)