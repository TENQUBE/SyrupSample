package com.tenqube.sample.syrup.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.tenqube.sample.R
import com.tenqube.visual.listener.ThemeReportLifecycle


class VisualActivity : AppCompatActivity() {
    var callback: ThemeReportLifecycle? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visual)
        setupNavigationBar()
    }

    private fun setupNavigationBar() {
        findNavController(R.id.nav_container).apply {
            setGraph(R.navigation.list)
        }
    }

    override fun onBackPressed() {
        Log.i("tag", "onBackPressed")
        val result = callback?.onBackPressed()
        Log.i("tag", "onBackPressed result : $result")
        if(result == false || result == null) {
            super.onBackPressed()
        }

    }
}