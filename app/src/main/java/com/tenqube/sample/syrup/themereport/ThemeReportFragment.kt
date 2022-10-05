/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tenqube.sample.syrup.themereport

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tenqube.sample.databinding.FragmentThemeReportBinding
import com.tenqube.sample.util.runOnMainThread
import com.tenqube.sample.util.showToastMsg
import com.tenqube.sample.syrup.main.VisualActivity
import com.tenqube.visual.Visual
import com.tenqube.visual.VisualService
import com.tenqube.visual.listener.Callback
import com.tenqube.visual.listener.VisualSyncService
import com.tenqube.visual.model.ReportRequest
import com.tenqube.visual.model.ReportResult
import com.tenqube.visual.model.SyncResult
import com.tenqube.visual.model.WebBundle

class ThemeReportFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentThemeReportBinding
    private var visualService: VisualService? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentThemeReportBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as VisualActivity).callback = null
        initVisualService()
        setupActionEvents()
    }

    /**
     * 비주얼 서비스 생성
     */
    private fun initVisualService() {
        visualService = Visual.getInstance(viewDataBinding.root.context.applicationContext)
        visualService?.setDebugMode(true)
        visualService?.syncTransactions(object : VisualSyncService {
            override fun fetchTransactions(date: String, syncNextIndex: Int,  callback: Callback<SyncResult>) {
                Log.i("fetchTransactions", "date $date")
                callback.onDataLoaded(SyncResult(emptyList(), listOf("20201010", "20201010").random(), listOf(0, 1,2,3).random()))
            }

            override fun onCompleted() {
                Toast.makeText(context, "onCompleted", Toast.LENGTH_LONG).show()
            }

            override fun onError(msg: String) {
                Toast.makeText(context, "onError", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * 클릭이벤트 설정
     */
    private fun setupActionEvents() {
        viewDataBinding.report.setOnClickListener { requestReceipt() }
        viewDataBinding.sync.setOnClickListener { syncWebBundle() }
    }

    /**
     * 테마리포트 요청
     */
    private fun requestReceipt() {
        visualService?.queryReceipt(ReportRequest(), object : Callback<ReportResult> {
            override fun onDataLoaded(value: ReportResult) {
                runOnMainThread {
                    viewDataBinding.resultText.text = "result : $value"
                }
            }
        })
    }

    /**
     * 웹번들 동기화
     */
    private fun syncWebBundle() {
        val webBundleVersion = visualService?.getWebBundleVersion() ?: 0
        // 해당 버전으로 api 요청 합니다.
        visualService?.syncWebBundle(
            webBundle = WebBundle(
                filePath = "https://visual-third-party.s3.ap-northeast-2.amazonaws.com/htmlBundle_1_3.zip",
                version = 3
            )
        )
        val newVersion = visualService?.getWebBundleVersion() ?: 0
        context?.showToastMsg("beforeVersion: $webBundleVersion / version $newVersion")
    }

}
