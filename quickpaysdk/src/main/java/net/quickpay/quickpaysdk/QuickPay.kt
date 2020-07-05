package net.quickpay.quickpaysdk

import android.content.Context
import android.util.Log
import net.quickpay.quickpaysdk.networking.NetworkUtility
import java.lang.RuntimeException


public class QuickPay(internal var apiKey: String) {

    // Singleton

    companion object {
        private const val LOGTAG = "QuickpayAct"

        private var backingInstance: QuickPay? = null
        var instance: QuickPay
            get() {
                return backingInstance ?: throw RuntimeException("The QuickPay SDK needs to be initialized before usage. \nQuickPay.init(\"<API_KEY>\", <CONTEXT>)")
            }
            private set(value) {
                backingInstance = value
            }

        // Static Init
        fun init(apiKey: String, context: Context) {
            NetworkUtility.init(context)
            instance = QuickPay(apiKey)

        }

        fun log(msg: String) {
            Log.d(LOGTAG, msg)
        }
    }
}