package com.tenqube.sample.syrup.visual

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tenqube.sample.databinding.FragmentVisualWebBinding
import com.tenqube.sample.syrup.main.VisualActivity
import com.tenqube.visual.Visual
import com.tenqube.visual.VisualService
import com.tenqube.visual.listener.Callback
import com.tenqube.visual.model.ThemeReportResult


class VisualWebFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentVisualWebBinding
    private var visualService: VisualService? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentVisualWebBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initVisualService()
        startVisualService()
    }

    /**
     * 비주얼 서비스 생성
     */
    private fun initVisualService() {
        visualService = Visual.getInstance(viewDataBinding.root.context.applicationContext)
        visualService?.setDebugMode(true)
    }

    /**
     * 비주얼 서비스 시작
     */
    private fun startVisualService(){
        visualService?.startVisual(
            "87844d3d7734e280c498440e9fb1e6b965201d76",
            object : Callback<ThemeReportResult> {
                override fun onDataLoaded(value: ThemeReportResult) {

                    (activity as VisualActivity).callback = value.themeReportLifecycle

                    value.themeReportLifecycle.onCreate(
                        activity = activity!!,
                        webView = viewDataBinding.webView,
                        resultCode = value.statusCode
                    )
                }
            })
    }



}
