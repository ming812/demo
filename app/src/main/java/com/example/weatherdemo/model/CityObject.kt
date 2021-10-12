package com.example.weatherdemo.model

import com.google.gson.annotations.SerializedName


data class CityObject(
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("state") val state : String = "",
    @SerializedName("country") val country : String,
    @SerializedName("coord") val coord : Coord
)

data class Coord(
    @SerializedName("lon") val lon : String,
    @SerializedName("lat") val lat : String
)


