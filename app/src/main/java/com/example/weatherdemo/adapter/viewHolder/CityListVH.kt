package com.example.weatherdemo.adapter.viewHolder

import android.view.View
import com.example.weatherdemo.adapter.BaseVM
import com.example.weatherdemo.adapter.CityListVM
import com.example.weatherdemo.databinding.CityListVhBinding
import com.example.weatherdemo.model.SearchKey

class CityListVH(itemView: View ,val  click: (SearchKey) -> Unit) : BaseVH<BaseVM>(itemView) {
    private val binding = CityListVhBinding.bind(itemView)
    override fun bind(item: BaseVM) {
        item as CityListVM
        binding.name.text = item.name
        itemView.setOnClickListener {
                click(SearchKey(item.id, item.lat , item.lon , item.name))
        }
        //Do your view assignment here from the data model
    }
}