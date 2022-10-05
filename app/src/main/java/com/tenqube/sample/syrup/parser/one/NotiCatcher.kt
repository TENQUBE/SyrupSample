package com.tenqube.sample.syrup.parser.one

import android.annotation.TargetApi
import android.app.Notification
import android.app.Notification.EXTRA_BIG_TEXT
import android.app.Service
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.annotation.RequiresApi
import com.tenqube.visual.Visual
import com.tenqube.visual.listener.Callback
import com.tenqube.visual.model.CategorizedTransaction
import com.tenqube.visual.model.NotiRequest
import com.tenqube.visual.model.ResultCode

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class NotiCatcher : NotificationListenerService() {
    private val TAG = this.javaClass.simpleName

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        parseNoti(sbn) // 알림 수신시 파싱을 합니다.
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun parseNoti(sbn: StatusBarNotification) {
        val packageName: String
        var title = "none"
        var content = ""
        var bigContent = ""
        try {
            if (shouldParse(sbn)) {
                // 데이터 파싱  시작
                packageName = sbn.packageName
                val notification = sbn.notification
                val titleCS =
                    notification.extras.getCharSequence(Notification.EXTRA_TITLE)
                val contentCS =
                    notification.extras.getCharSequence(Notification.EXTRA_TEXT)
                val bigCS =
                    notification.extras.getCharSequence(EXTRA_BIG_TEXT)

                title = titleCS?.toString() ?: ""
                content = contentCS?.toString() ?: ""
                bigContent = bigCS?.toString() ?: ""
                if (bigCS != null) bigContent = bigCS.toString()

                val result = Visual.getInstance(application).parse(NotiRequest(
                    packageName,
                    title,
                    content,
                    bigContent,
                    notification.`when`
                ).toSMS(applicationContext)!!)

                result.let {
                    if (result.resultCode == ResultCode.SEND_TO_SERVER) {

                        Visual.getInstance(applicationContext).onNetworkResult(it.transactions, true, it.transactions.map { transaction ->
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun shouldParse(sbn: StatusBarNotification?): Boolean {
        return sbn != null &&
                !sbn.isOngoing && sbn.notification != null && sbn.notification.extras != null && sbn.notification.extras.getInt(
            Notification.EXTRA_PROGRESS
        ) == 0 && sbn.notification.extras.getInt(Notification.EXTRA_PROGRESS_MAX) == 0
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {}
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_REDELIVER_INTENT
    }
}
