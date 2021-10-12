package com.example.weatherdemo.adapter

data class CityListVM(
    override var id: String,
    override var lat: String,
    override var lon: String,
    override var viewType: Int,
    val name : String
) : BaseVM()