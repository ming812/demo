package com.example.weatherdemo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.weatherdemo.R
import com.example.weatherdemo.adapter.viewHolder.*
import com.example.weatherdemo.model.SearchKey

class CityListListAdapter(
    itemCallback : DiffUtil.ItemCallback<BaseVM>,
    val click : (SearchKey) -> Unit,
    val clear : () -> Unit
) : ListAdapter<BaseVM , BaseVH<*>>(itemCallback) {

        companion object {
            const val TYPE_SEARCH = 0
            const val TYPE_RECENT = 1
            const val TYPE_HISTORY = 2
            const val TYPE_CLEAR = 3
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<*> {
        Log.e("onCreateViewHolder" , "viewType = $viewType")
        return when(viewType){
            TYPE_SEARCH ->{
                CityListVH(
                    LayoutInflater.from(parent.context).inflate(R.layout.city_list_vh,parent,false) ,
                    click)
            }
            TYPE_RECENT -> {
                RecentVH(
                    LayoutInflater.from(parent.context).inflate(R.layout.recent_vh , parent, false),
                    click
                )
            }
            TYPE_HISTORY -> {
                CityListVH(
                    LayoutInflater.from(parent.context).inflate(R.layout.city_list_vh,parent,false) ,
                    click)
            }
            TYPE_CLEAR -> {
                ClearHistoryVH(
                    LayoutInflater.from(parent.context).inflate(R.layout.clear_history_vh , parent, false),
                    clear
                )
            }
            else -> EmptyVH(
                LayoutInflater.from(parent.context).inflate(R.layout.empty_vh,parent,false) ,
            )
        }
    }

    override fun onBindViewHolder(holder: BaseVH<*>, position: Int) {
        when (holder) {
            is CityListVH -> {
    //            holder as CityListVH
                holder.bind(getItem(position))
            }
            is RecentVH -> {
                holder.bind(getItem(position))
            }
            is ClearHistoryVH -> {
                holder.bind(getItem(position))
            }
            is EmptyVH -> {
                holder.bind(getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }
}