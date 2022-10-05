# SyrupSample
# 빌드
```gradle
repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation 'com.tenqube:visual-theme:0.6.2'
}
```

# RCS
## BradcastReceiver

```
class RCSCatcher() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            requireNotNull(context)
            requireNotNull(intent)
            if (RCS_RECEIVED_ACTION == intent.action) {
                intent.extras?.get(MSG_ID)?.toString()?.let { msgId ->
                    val result = Visual.getInstance(context).parse(msgId)
                    if (result.resultCode == ResultCode.SEND_TO_SERVER) {
                        // 서버 통신 후 결과 전달
                        Visual.getInstance(context)
                            .onNetworkResult(result.transactions, true, result.transactions.map { transaction ->
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
    companion object {
        const val RCS_RECEIVED_ACTION = "com.services.rcs.MESSAGE_RECEIVED"
        const val MSG_ID = "msg_id"
    }
}
```

## AndroidManifest.xml
```
  <receiver
      android:name=".syrup.parser.one.RCSCatcher"
      android:enabled="true"
      android:exported="true">
      <intent-filter>
          <action android:name="com.services.rcs.MESSAGE_RECEIVED" />
      </intent-filter>
  </receiver>
```
