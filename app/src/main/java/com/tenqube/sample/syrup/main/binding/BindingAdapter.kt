package com.tenqube.sample.syrup.main.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tenqube.sample.syrup.main.adapter.TestAdapter

@BindingAdapter("testList")
fun setTestList(list: RecyclerView, items: List<String>) {

    list.adapter?.let {
        if (list.adapter is TestAdapter) {
            (list.adapter as TestAdapter).submitList(items)
        }
    }
}