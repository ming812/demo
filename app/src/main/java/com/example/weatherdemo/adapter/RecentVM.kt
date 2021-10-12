package com.example.weatherdemo.adapter

import com.example.weatherdemo.model.CityWeatherDetail

data class RecentVM(
    override var id: String,
    override var lat: String,
    override var lon: String,
    override var viewType: Int,
    val name : String,
    val weatherDetail: CityWeatherDetail?
) : BaseVM()
