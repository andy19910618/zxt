package com.tezwez.base.helper.fingerprint

import android.content.Context
import android.content.Intent

/**
 * Created by popfisher on 2016/11/7.
 */

object FingerprintUtil {

    private val ACTION_SETTING = "android.settings.SETTINGS"

    fun openFingerPrintSettingPage(context: Context) {
        val intent = Intent(ACTION_SETTING)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
        }
    }


}
