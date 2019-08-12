package net.quickpay.quickpaysdk

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.util.Log
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.acquirers.QPGetAcquireSettingsMobilePayRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPayment
import java.lang.RuntimeException

/*
interface InitializeListener {

    fun initializationStarted()
    fun initializationCompleted()

}
*/

class QuickPay(internal var apiKey: String) {

    // Singleton

    companion object {
        private const val MOBILE_PAY_SCHEME = "mobilepayonline://"
        private const val LOGTAG = "QuickpayAct"

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
            instance = QuickPay(apiKey)
//            instance.fetchAcquirers(context)
        }

        fun log(msg: String) {
            Log.d(LOGTAG, msg)
        }
    }


    // Properties

    /*
    var isMobilePayOnlineEnabled: Boolean? = null
    var isInitializing: Boolean = true

    private val initializeListeners: MutableList<InitializeListener> = ArrayList()

    fun addInitializeListener(listener: InitializeListener) {
        if( !initializeListeners.contains(listener) ) {
            initializeListeners.add(listener)
        }
    }

    fun removeInitializeListener(listener: InitializeListener) {
        initializeListeners.remove(listener)
    }

    fun fetchAcquirers(context: Context) {
        isInitializing = true
        initializeListeners.forEach { it.initializationStarted() }

        isMobilePayEnabled { enabled ->
            isMobilePayOnlineEnabled = enabled && isMobilePayAvailableOnDevice(context)
            isInitializing = false
            initializeListeners.forEach { it.initializationCompleted() }
        }
    }
    */


    // MobilePay

    /*
    private fun isMobilePayEnabled(callback: (Boolean)->Unit) {
        QPGetAcquireSettingsMobilePayRequest().sendRequest(listener = {
            log("MobilePay enabled on backend: ${it.active}")
            callback(it.active)
        }, errorListener = { _, _, _ ->
            log("MobilePay settings request failed")
            callback(false)
        })
    }

    private fun isMobilePayAvailableOnDevice(context: Context): Boolean {
        val mobilePayIntent: Intent = Uri.parse(MOBILE_PAY_SCHEME).let { mobilePay -> Intent(Intent.ACTION_VIEW, mobilePay) }
        val activities: List<ResolveInfo> = context.packageManager.queryIntentActivities(mobilePayIntent, PackageManager.MATCH_DEFAULT_ONLY)
        return activities.isNotEmpty()
     }

    fun authorizeWithMobilePay(payment: QPPayment, context: Context) {
        val mobilePayToken = payment.operations?.get(0)?.data?.get("session_token")

        if (mobilePayToken.isNullOrEmpty()) {
            QuickPay.log("MobilePay Token is NULL")
        }
        else {
            QuickPay.log("MobilePay Token: $mobilePayToken")

            var mpUrl = "${MOBILE_PAY_SCHEME}online?sessiontoken=$mobilePayToken&version=2"
            val mobilePayIntent: Intent = Uri.parse(mpUrl).let { mobilePay -> Intent(Intent.ACTION_VIEW, mobilePay) }

            context.startActivity(mobilePayIntent)
        }
    }
    */
}