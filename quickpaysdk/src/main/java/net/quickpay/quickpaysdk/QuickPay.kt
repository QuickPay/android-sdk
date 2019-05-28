package net.quickpay.quickpaysdk

import android.content.Context
import android.util.Log
import com.android.volley.Response
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.acquirers.QPGetAcquireSettingsClearhausRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.acquirers.QPGetAcquireSettingsMobilePayRequest
import java.lang.RuntimeException

class QuickPay(apiKey: String, context: Context) {

    companion object {

        // Singleton

        private var _instance: QuickPay? = null
        var instance: QuickPay
            get() {
                return _instance ?: throw RuntimeException("The QuickPay SDK needs to be initialized before usage. \nQuickPay.init(\"<API_KEY>\", <CONTEXT>)")
            }
            private set(value) {
                _instance = value
            }

        // Static Init
        fun init(apiKey: String, context: Context) {
            instance = QuickPay(apiKey, context)
        }

    }


    var apiKey: String = apiKey

    init {
        NetworkUtility.getInstance(context)
    }


    fun checkClearhaus() {
        val request = QPGetAcquireSettingsClearhausRequest()
        request.sendRequest(Response.Listener {
            Log.d("QUICKPAYRESPONSE", "ApplePay: ${it.apple_pay}")
        }, Response.ErrorListener {
            Log.d("QUICKPAYRESPONSE", "Could not fetch Clearhaus settings")
        })
    }

    fun checkMobilePay() {
        QPGetAcquireSettingsMobilePayRequest().sendRequest(Response.Listener {
            Log.d("QUICKPAYRESPONSE", "MobilePay: ${it.active}")
        }, Response.ErrorListener {
            Log.d("QUICKPAYRESPONSE", "Could not fetch MobilePay settings")
        })
    }

}