package com.tenqube.sample.syrup.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tenqube.sample.databinding.ListViewItemBinding
import com.tenqube.sample.syrup.main.event.EventListener

class TestViewHolder private constructor(
    private val binding: ListViewItemBinding,
    private val eventListener: EventListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        binding.item = item
        binding.eventListener = eventListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup, eventListener: EventListener): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListViewItemBinding.inflate(layoutInflater, parent, false)
            return TestViewHolder(
                    binding,
                    eventListener
            )
        }
    }
}