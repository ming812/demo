package com.example.weatherdemo.adapter

import androidx.recyclerview.widget.DiffUtil

abstract class BaseVM : DiffUtil.ItemCallback<BaseVM>(){
    open var id : String = ""
    open var lat : String = ""
    open var lon : String = ""
    open var viewType : Int = 999

    override fun areItemsTheSame(oldItem: BaseVM, newItem: BaseVM): Boolean {
        return oldItem.viewType === newItem.viewType
    }

    override fun areContentsTheSame(oldItem: BaseVM, newItem: BaseVM): Boolean {
        if (oldItem.viewType != newItem.viewType) return false
        return oldItem.id == newItem.id
    }
}