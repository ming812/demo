package com.example.weatherdemo.adapter

import androidx.recyclerview.widget.DiffUtil

class DiffCallback : DiffUtil.ItemCallback<BaseVM>() {
    override fun areItemsTheSame(oldItem: BaseVM, newItem: BaseVM): Boolean {
        return oldItem.areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: BaseVM, newItem: BaseVM): Boolean {
        return oldItem.areContentsTheSame(oldItem, newItem)
    }

    override fun getChangePayload(oldItem: BaseVM, newItem: BaseVM): Any? {
        return oldItem.getChangePayload(oldItem, newItem)
    }

    companion object {
        const val TAG = "DiffCallback"
    }
}