package com.tenqube.sample.syrup.main.event

import android.view.View

interface EventListener {

    fun onItemClicked(view: View, item: String)

}