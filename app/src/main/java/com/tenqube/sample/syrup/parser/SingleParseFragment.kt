package com.tenqube.sample.syrup.parser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tenqube.sample.databinding.FragmentSingleParserBinding
import com.tenqube.sample.util.runOnMainThread
import com.tenqube.sample.syrup.main.VisualActivity
import com.tenqube.sample.syrup.utils.Utils
import com.tenqube.visual.Visual
import com.tenqube.visual.VisualService
import com.tenqube.visual.listener.Callback
import com.tenqube.visual.model.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SingleParseFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentSingleParserBinding
    private var visualService: VisualService? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentSingleParserBinding.inflate(inflater, container, false)
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
    }

    /**
     * 클릭이벤트 설정
     */
    private fun setupActionEvents() {
        viewDataBinding.addTestNum.setOnClickListener { addTestNum() }
        viewDataBinding.sync.setOnClickListener { syncParsingRule() }
        viewDataBinding.parse.setOnClickListener { parse() }
        viewDataBinding.syncKey.setOnClickListener{
            syncKey()
        }

        viewDataBinding.init.setOnClickListener {
            visualService?.initDb()
        }

        viewDataBinding.getVersion.setOnClickListener {
            Toast.makeText(activity!!, "version ${visualService?.getRuleVersion()}", Toast.LENGTH_LONG).show()
        }

    }

    /**
     * 테스트 넘버 추가
     */
    private fun addTestNum() {
        visualService?.addTestOriginNumber(viewDataBinding.sender.text.toString())
    }

    /**
     * 파싱룰 동기화
     */
    private fun syncParsingRule() {
        visualService?.syncParsingRule(
            ParsingRule(
                "TENQUBE&SKPSQS!@",
                30,
                339,
                listOf(
                    RegData(regId = 1699,
                    sender = "e음",
                    regExpression = "vEEegUjKsFs/cr1063HGktewps9EXwobfykev1p2qwqMdEmgZ1yIuzlAOmDuPJJ+wVFNLIHLRxNR2l1NLNvBv8i4K/UeT0A5LvAIaO/dx8Yor34Ls7OSl+f2KFVfgx6uderSwU3sVIMphtnIfOSh7g==",
                    cardName = "e음",
                    cardType = "체크",
                    cardSubType = "기본",
                    cardNum = "",
                    balance = "4",
                    spentMoney = "1",
                    spentDate = "",
                    keyword = "3",
                    installmentCount = "",
                    dwType = "출금",
                    isCancel = "2",
                    currency = "",
                    userName = "",
                    smsType = 0,
                    isDelete = 0,
                    priority = 1
                    ),
                    RegData(regId = 1735,
                        sender = "BC우리",
                        regExpression = "jGMw0RKFIx1sjIjc46EN5+uxE27babYBaIeH5p5MV+XrsT54c69qUkSQKsVI+7YlXof9SEmPx8Sp08aaBA9hceeJCOLkb60m+ToM8JK9B3iivLrVTN+REDqCylM6DOBwN5MqcfH8X657uVYsdA0w1K1QuamWBfCexXjwp2lxjTu2K8BM9msoZSMYHcju1v7l3DmrV7eQ19qg5cbKcHE8qZ+rzj/Er3C5em9uBJtL09EqUOempQN78YZtnmy5dIpjjzQbyNv2f7l7Y9P0kVIbkKvvTMCPiLrd4/17emBfx2zN0M1OEzrQclvZLfEyHapcrYaXUxcl7pODrC62Jhlav+SPcNgzJccO8uUNAlkLl5Kp+DFntu3pmn09cH2HkIFj",
                        cardName = "1",
                        cardType = "신용",
                        cardSubType = "4",
                        cardNum = "",
                        balance = "4",
                        spentMoney = "6",
                        spentDate = "10",
                        keyword = "14",
                        installmentCount = "9",
                        dwType = "출금",
                        isCancel = "7",
                        currency = "",
                        userName = "5",
                        smsType = 3,
                        isDelete = 0,
                        priority = 1
                    )

                ),
                emptyList(),
                emptyList(),//
                listOf(FinancialProductRule(ruleId = 1,
                    sender = "",
                    repSender = "수협",
                    productType = 0,
                    productSubType = 0,
                    finCode = "900400",
                    regex = "\\s*(수협)알림\\s*(\\D+)님\\s*계좌\\s*[0-9*\\-#]+\\s*([0-9,]+)원\\s*신규개설.*",
                    finNameRule = "1",
                    productNameRule = "",
                    userNameRule = "",
                    amountRule = "",
                    yearRule = "",
                    monthRule = "",
                    dayRule = "",
                    hourRule = "",
                    minRule = "",
                    secRule = "",
                    optionRule = "",
                    smsType = -1

                    ))
            )
        )
    }

    fun syncKey() {
        visualService?.syncParsingRule(
            ParsingRule(
                "TENQUBE&SKPSQS!@",
                30,
                30,
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList()
            )
        )
    }

    /**
     * 파싱 시작
     */
    private fun parse() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = visualService?.parse(
                SMS(
                    0,
                    fullSms = viewDataBinding.msg.text.toString(),
                    sender = viewDataBinding.sender.text.toString(),
                    displaySender = viewDataBinding.sender.text.toString(),
                    smsDate = Utils.getDateTime(Date().time),
                    smsType = 2
                )
            )

            withContext(Dispatchers.Main) {
                viewDataBinding.result.text = result.toString()
            }

            result?.let {
                if (result.resultCode == ResultCode.SEND_TO_SERVER) {

                    visualService?.onNetworkResult(it.transactions, true, it.transactions.map { transaction ->
                        CategorizedTransaction(
                            transaction.identifier,
                            transaction.spentMoney,
                            1,
                            221010,
                            "address",
                            "name"
                        )
                    }, object : Callback<Boolean> {
                        override fun onDataLoaded(value: Boolean) {
                            runOnMainThread {
                                viewDataBinding.result.text =
                                        "${viewDataBinding.result.text} onSave: $value"
                            }
                        }
                    })
                }
            }
        }
    }

}
