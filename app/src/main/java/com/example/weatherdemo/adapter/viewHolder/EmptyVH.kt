package com.example.weatherdemo.adapter.viewHolder

import android.view.View
import com.example.weatherdemo.adapter.BaseVM
import com.example.weatherdemo.adapter.EmptyVM
import com.example.weatherdemo.databinding.EmptyVhBinding

class EmptyVH (itemView : View) : BaseVH<BaseVM>(itemView){
    private val binding = EmptyVhBinding.bind(itemView)

    override fun bind(item: BaseVM) {
        item as EmptyVM
        binding.text.text = item.text
    }

}