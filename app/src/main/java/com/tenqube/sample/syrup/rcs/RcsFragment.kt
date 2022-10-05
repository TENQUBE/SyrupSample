package com.tenqube.sample.syrup.rcs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tenqube.sample.databinding.FragmentRcsBinding
import com.tenqube.sample.syrup.main.VisualActivity
import com.tenqube.visual.RcsCallback
import com.tenqube.visual.Visual
import com.tenqube.visual.VisualService
import com.tenqube.visual.model.ParserResult

class RcsFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentRcsBinding
    private var visualService: VisualService? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentRcsBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as VisualActivity).callback = null
        initVisualService()

        viewDataBinding.requestButton.setOnClickListener {

            visualService?.parseRcs(0, 0, object : RcsCallback {
                override fun onResult(lastParsedTime: Long?, parseResult: ParserResult) {
                    viewDataBinding.resultText.text = printSms(lastParsedTime, parseResult)
                }
            })
        }
    }

    private fun printSms(lastParsedTime: Long?, result: ParserResult): String {
        val smsStr = StringBuilder()
        smsStr.append("lastParsedTime $lastParsedTime\n")
        smsStr.append("transactions \n")
        result.transactions.forEach {
            smsStr.append(it.toString() + "\n")
        }
        smsStr.append("finance \n")
        result.financialProducts.forEach {
            smsStr.append(it.toString() + "\n")
        }
        val str = smsStr.toString()
        return str.ifEmpty {
            "empty"
        }
    }

    /**
     * 비주얼 서비스 생성
     */
    private fun initVisualService() {
        visualService = Visual.getInstance(viewDataBinding.root.context.applicationContext)
    }
}
