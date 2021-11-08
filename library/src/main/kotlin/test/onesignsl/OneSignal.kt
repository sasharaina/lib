package io.lib.test.onesignsl

import android.content.Context
import com.onesignal.OneSignal

class OneSignal(private val context: Context, private val oneSignalID: String) {

    fun initialize(){
        OneSignal.initWithContext(context)
        OneSignal.setAppId(oneSignalID)
    }

}