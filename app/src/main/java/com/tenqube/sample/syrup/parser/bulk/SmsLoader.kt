package com.tenqube.sample.syrup.parser.bulk

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.tenqube.sample.syrup.utils.Utils.getDateTime
import com.tenqube.visual.listener.BulkSmsAdapter
import com.tenqube.visual.listener.OnNetworkResultListener
import com.tenqube.visual.model.FinancialProduct
import com.tenqube.visual.model.SMS
import com.tenqube.visual.model.Transaction
import java.text.SimpleDateFormat
import java.util.*

class SmsLoader(
    private val context: Context,
    private val bulkListener: BulkListener
) : BulkSmsAdapter {

    init {
        initCursor()
    }

    private var cursor: Cursor? = null

    @SuppressLint("Range")
    private fun getSMS(): SMS? {

        return cursor?.run {
            SMS(
                this.getInt(this.getColumnIndex("_id")),
                this.getString(this.getColumnIndex("body")),
                this.getString(this.getColumnIndex("address")),
                this.getString(this.getColumnIndex("address")),
                getDateTime(this.getLong(this.getColumnIndex("date"))),
                2
            ).also {
                this.moveToNext()
            }
        }
    }



    private fun initCursor() {
        try {
            val uri = Uri.parse("content://sms/inbox")
            cursor = context.contentResolver.query(uri, null, null, null, "date asc")
            cursor?.moveToFirst()
        } catch (e: Exception) {
            onError(1)
        }
    }

    override fun getSmsCount(): Int {
        return cursor?.count ?: 0
    }

    override fun getSmsAt(n: Int): SMS? {
        return getSMS()
    }

    override fun onProgress(now: Int, total: Int) {
        bulkListener.onProgress(now, total)
    }

    override fun sendToServerTransactions(
        transactions: List<Transaction>,
        financialProducts: List<FinancialProduct>,
        listener: OnNetworkResultListener
    ) {
        bulkListener.sendToServerTransactions(transactions, financialProducts, listener)
    }

    override fun onCompleted() {
        bulkListener.onComplete()
    }

    override fun onError(errorCode: Int) {
        bulkListener.onError(errorCode)
    }
}