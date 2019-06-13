package net.quickpay.quickpaysdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.android.volley.Response
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.acquirers.QPGetAcquireSettingsClearhausRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.acquirers.QPGetAcquireSettingsMobilePayRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPayment
import java.lang.RuntimeException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

interface InitializeListener {

    fun initializationStarted()
    fun initializationCompleted()

}

class QuickPay(apiKey: String, context: Context) {

    // Singleton

    companion object {
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
            NetworkUtility.getInstance(context)
            instance = QuickPay(apiKey, context)
            instance.fetchAquires()
        }

        fun log(msg: String) {
            Log.d("QUICKPAYDEBUG", msg)
        }
    }


    // Properties

    var isMobilePayOnlineEnabled: Boolean? = null
    var isInitializing: Boolean = true

    var initializeListener: InitializeListener? = null

    var apiKey: String = apiKey
    var context: Context = context

    fun fetchAquires() {
        isInitializing = true
        initializeListener?.initializationStarted()

        isMobilePayEnabled { enabled -> isMobilePayOnlineEnabled = enabled
            isInitializing = false
            initializeListener?.initializationCompleted()
        }
    }




    // MobilePay

    fun isMobilePayEnabled(callback: (Boolean)->Unit) {
        QPGetAcquireSettingsMobilePayRequest().sendRequest(Response.Listener {
            log("MobilePay enabled: ${it.active}")
            callback(it.active)
        }, Response.ErrorListener {
            callback(false)
        })
    }

    fun isMobilePayAvailableOnDevice(): Boolean {
        val mobilePayIntent: Intent = Uri.parse("mobilepayonline://").let { mobilePay -> Intent(Intent.ACTION_VIEW, mobilePay) }
        val activities: List<ResolveInfo> = context.packageManager.queryIntentActivities(mobilePayIntent, PackageManager.MATCH_DEFAULT_ONLY)
        return activities.isNotEmpty()
     }

    public var paymentId: Int? = null
    fun authorizeWithMobilePay(payment: QPPayment) {
        val mobilePayToken = payment.operations?.get(0)?.data?.get("session_token")
        paymentId = payment.id

        if (mobilePayToken.isNullOrEmpty()) {
            QuickPay.log("MobilePay Token is NULL")
        }
        else {
            QuickPay.log("MobilePay Token: $mobilePayToken")

            var mpUrl = "mobilepayonline://online?sessiontoken=$mobilePayToken&version=2"
            val mobilePayIntent: Intent = Uri.parse(mpUrl).let { mobilePay -> Intent(Intent.ACTION_VIEW, mobilePay) }

            var activity: Activity? = context as Activity
            activity?.startActivityForResult(mobilePayIntent, 1384)
//            activity?.startActivity(mobilePayIntent)
        }


    }
}