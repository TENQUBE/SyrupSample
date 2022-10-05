package com.tenqube.sample.syrup.parser.bulk

import com.tenqube.visual.listener.OnNetworkResultListener
import com.tenqube.visual.model.FinancialProduct
import com.tenqube.visual.model.Transaction

interface BulkListener {

    fun onProgress(now: Int, total: Int)

    fun onComplete()

    fun onError(code: Int)

    fun sendToServerTransactions(
        transactions: List<Transaction>,
        financialProducts: List<FinancialProduct>,
        listener: OnNetworkResultListener
    )
}