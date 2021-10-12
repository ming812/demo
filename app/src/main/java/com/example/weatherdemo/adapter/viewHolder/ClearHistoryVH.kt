package com.example.weatherdemo.adapter.viewHolder

import android.util.Log
import android.view.View
import com.example.weatherdemo.adapter.BaseVM
import com.example.weatherdemo.adapter.ClearHistoryVM
import com.example.weatherdemo.databinding.ClearHistoryVhBinding

class ClearHistoryVH(itemView: View, val  click: () -> Unit) : BaseVH<BaseVM>(itemView) {
    private val binding = ClearHistoryVhBinding.bind(itemView)
    override fun bind(item: BaseVM) {
        item as ClearHistoryVM
        binding.btnClear.text = item.title
        binding.btnClear.setOnClickListener{
            click()
        }
//        binding.btnClear.setOnClickListener { click }
        itemView.setOnClickListener {
            Log.e("ClearHistory" , "onClick")
            click
        }
        //Do your view assignment here from the data model
    }
}