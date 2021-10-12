package com.example.weatherdemo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherdemo.R
import com.example.weatherdemo.ViewType
import com.example.weatherdemo.model.CityObject

class CityListAdapter(private val list : List<CityObject>?) : RecyclerView.Adapter<CityListAdapter.CityListHolder>() {

    class CityListHolder(v : View) : RecyclerView.ViewHolder(v){
        val textView : TextView = v.findViewById(R.id.name)
//        fun bind(cityObject : CityObject){
//            textView.text = cityObject.name
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_list_vh, parent, false)

        return CityListHolder(view)
    }

    override fun onBindViewHolder(holder: CityListHolder, position: Int) {
        holder.textView.text = list?.get(position)?.name.orEmpty()
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

}

