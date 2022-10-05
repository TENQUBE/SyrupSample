package com.tenqube.sample.syrup.parser

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tenqube.sample.databinding.FragmentBulkParserBinding
import com.tenqube.sample.util.runOnMainThread
import com.tenqube.sample.util.showToastMsg
import com.tenqube.sample.syrup.main.VisualActivity
import com.tenqube.visual.Visual
import com.tenqube.visual.VisualService
import com.tenqube.visual.listener.OnNetworkResultListener
import com.tenqube.visual.model.CategorizedTransaction
import com.tenqube.visual.model.FinancialProduct
import com.tenqube.visual.model.Transaction
import com.tenqube.sample.syrup.parser.bulk.BulkListener
import com.tenqube.sample.syrup.parser.bulk.SmsLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BulkParseFragment : Fragment(), BulkListener {

    private lateinit var viewDataBinding: FragmentBulkParserBinding
    private var statusStr = StringBuffer()
    private var visualService: VisualService? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentBulkParserBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as VisualActivity).callback = null
        initVisualService()
        setupActionEvents()
        checkAndRequestSmsReadPermission()
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
        viewDataBinding.bulk.setOnClickListener { parse() }
    }

    private fun checkAndRequestSmsReadPermission() {
        activity?.let {
            val readSmsPermission = android.Manifest.permission.READ_SMS

            if (ContextCompat.checkSelfPermission(it, readSmsPermission)
                != PackageManager.PERMISSION_GRANTED
            ) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(it, readSmsPermission)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(
                        it, arrayOf(readSmsPermission),
                        SMS_PERMISSION_REQUEST_CODE
                    )
                }
            } else {
                // Permission has already been granted
            }
        }
    }

    /**
     * 파싱 시작
     */
    private fun parse() {
        GlobalScope.launch(Dispatchers.IO) {
            context?.let {
                visualService?.parseBulk(SmsLoader(it, this@BulkParseFragment))
            }
        }
    }

    /**
     * 벌크파싱 진행상황 콜백 함수
     *
     * @param now 현재 수
     * @param total 전체 수
     */
    override fun onProgress(now: Int, total: Int) {
        runOnMainThread {
            viewDataBinding.progress.text = "$now/$total"
        }
    }

    /**
     * 벌크 파싱 완료됨 콜백 함수
     *
     */
    override fun onComplete() {
        runOnMainThread {
            statusStr.append("completed\n")
            viewDataBinding.status.text = statusStr.toString()
        }
    }

    /**
     * 벌크파싱 에러 콜백 함수
     *
     * @param code
     */
    override fun onError(code: Int) {
        runOnMainThread {
            statusStr.append("error : $code\n")
            viewDataBinding.status.text = statusStr.toString()
        }
    }

    /**
     * 파싱된 정보를 전달받는 콜백 함수
     *
     * @param transactions
     * @param financialProducts
     * @param listener
     */
    override fun sendToServerTransactions(
        transactions: List<Transaction>,
        financialProducts: List<FinancialProduct>,
        listener: OnNetworkResultListener
    ) {

        transactions.map {
            it.locationAccuracy = 0.0
            it.spentLatitude
        }

        listener.onResult(
            true,
            transactions.map {
                            CategorizedTransaction(
                it.identifier,
                spentMoney = it.spentMoney,
                companyId = 1,
                companyName = "",
                companyAddress = "",
                categoryCode = 101010
            )
        }
        )

        runOnMainThread {
            viewDataBinding.log.text =
                "transactions : $transactions\n financialProducts : $financialProducts"
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            SMS_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    context?.showToastMsg("READ_SMS 권한이 허용되었습니다.")
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    context?.showToastMsg("READ_SMS 권한이 거부되었습니다.")
                }
                return
            }
            else -> {context?.showToastMsg("냐 ? ")}
        }
    }


    companion object {
        private const val SMS_PERMISSION_REQUEST_CODE = 100
    }

}
