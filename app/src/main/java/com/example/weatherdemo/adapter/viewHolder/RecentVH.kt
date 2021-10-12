package com.example.weatherdemo.adapter.viewHolder

import android.util.Log
import android.view.View
import com.example.weatherdemo.adapter.BaseVM
import com.example.weatherdemo.adapter.RecentVM
import com.example.weatherdemo.databinding.RecentVhBinding
import com.example.weatherdemo.model.SearchKey
import java.text.SimpleDateFormat
import java.util.*

class RecentVH(itemView: View ,val  click: (SearchKey) -> Unit) : BaseVH<BaseVM>(itemView) {
    private val binding = RecentVhBinding.bind(itemView)

    override fun bind(item: BaseVM) {
        item as RecentVM
        //Do your view assignment here from the data model
        binding.name.text = item.name
            Log.e("itemname", "${item.name}")
            binding.weather.apply {
                text = this.text.toString().replace(
                    "%1\$s", item.weatherDetail?.current?.weather?.get(0)?.main.orEmpty()
                )
            }
            binding.description.apply {
                text = this.text.toString().replace(
                    "%1\$s",
                    item.weatherDetail?.current?.weather?.get(0)?.description.orEmpty()
                )
            }
            binding.sunrise.apply {
                text = this.text.toString().replace(
                    "%1\$s",
                    convertLongToTime(item.weatherDetail?.current?.sunrise ?: 0L)
                )
            }
            binding.sunset.apply {
                text = this.text.toString().replace(
                    "%1\$s",
                    convertLongToTime(item.weatherDetail?.current?.sunset ?: 0L)
                )
            }
            binding.temp.apply {
                text = this.text.toString().replace(
                    "%1\$s",
                    item.weatherDetail?.current?.temp?.toString().orEmpty()
                )
            }
            binding.feelsLike.apply {
                text = this.text.toString().replace(
                    "%1\$s",
                    item.weatherDetail?.current?.feels_like?.toString().orEmpty()
                )
            }
            itemView.setOnClickListener{
                click(SearchKey(item.id, item.lat , item.lon , item.name))
            }
        }
    }

    fun convertLongToTime(time: Long): String {
        return if(time == 0L){
            "unknown"
        }else {
            val date = Date(time)
            val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
            format.format(date)
        }
    }