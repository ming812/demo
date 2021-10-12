package com.example.weatherdemo.adapter

data class EmptyVM(
    val text : String = "No Result",
    override var viewType: Int = 999
) : BaseVM()