package com.tenqube.sample.syrup.parser.one

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.text.TextUtils
import com.tenqube.sample.syrup.utils.Utils
import com.tenqube.visual.Visual
import com.tenqube.visual.listener.Callback
import com.tenqube.visual.model.CategorizedTransaction
import com.tenqube.visual.model.ResultCode
import com.tenqube.visual.model.SMS

class SMSCatcher : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (SMS_RECEIVED_ACTION == intent.action) {
            val sms = parseSms(intent.extras)
            sms?.let {
                val result = Visual.getInstance(context).parse(sms)

                result.let {
                    if (result.resultCode == ResultCode.SEND_TO_SERVER) {

                        Visual.getInstance(context).onNetworkResult(it.transactions, true, it.transactions.map { transaction ->
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

                            }
                        })
                    }
                }
            }
        }
    }

    private fun parseSms(bundle: Bundle?): SMS? {

        return bundle?.run {

            this["pdus"]?.let { pdus ->
                if(pdus is Array<*>) {

                    var originMsg = ""
                    var displayMsg = ""
                    var displayTel = ""
                    var originTel = ""
                    var date = 0L

                    pdus.forEach { obj ->
                        val currentMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            val format = bundle.getString("format")
                            SmsMessage.createFromPdu(
                                obj as ByteArray?,
                                format
                            )
                        } else {
                            SmsMessage.createFromPdu(obj as ByteArray?)
                        }

                        displayTel = currentMessage.displayOriginatingAddress
                        originTel = currentMessage.originatingAddress.toString()
                        date = currentMessage.timestampMillis


                        originMsg += currentMessage.messageBody
                        displayMsg += currentMessage.displayMessageBody

                    }

                    SMS(0, selectMsg(originMsg, displayMsg),
                        originTel, displayTel, Utils.getDateTime(date), 2)


                } else {
                    null
                }
            }
        }
    }


    private fun selectMsg(originMsg: String, displayMsg: String): String {
        if (TextUtils.isEmpty(originMsg)) {
            return displayMsg
        }
        if (TextUtils.isEmpty(displayMsg)) {
            return originMsg
        }
        return if (originMsg == displayMsg) {
            originMsg
        } else {
            displayMsg
        }
    }

    companion object {
        private const val SMS_TEST_ACTION = "ACTION_VISUAL_SMS_TEST"
        private const val SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}