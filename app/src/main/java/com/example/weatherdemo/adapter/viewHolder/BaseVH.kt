package com.example.weatherdemo.adapter.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseVH<T>(container : View) : RecyclerView.ViewHolder(container) {

    abstract fun bind(item: T)

}