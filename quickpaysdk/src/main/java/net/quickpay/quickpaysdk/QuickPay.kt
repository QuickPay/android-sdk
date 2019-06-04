package net.quickpay.quickpaysdk

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

    public fun fetchAquires() {
        isInitializing = true
        initializeListener?.initializationStarted()

        isMobilePayEnabled { enabled -> isMobilePayOnlineEnabled = enabled
            isInitializing = false
            initializeListener?.initializationCompleted()
        }
    }


    //Fetch Aquires

    public fun isMobilePayEnabled(callback: (Boolean)->Unit) {
        QPGetAcquireSettingsMobilePayRequest().sendRequest(Response.Listener {
            log("MobilePay enabled: ${it.active}")
            callback(it.active)
        }, Response.ErrorListener {
            callback(false)
        })
    }

    public fun isMobilePayAvailableOnDevice(): Boolean {
        val mobilePayIntent: Intent = Uri.parse("mobilepayonline://").let { mobilePay -> Intent(Intent.ACTION_VIEW, mobilePay) }
        val activities: List<ResolveInfo> = context.packageManager.queryIntentActivities(mobilePayIntent, PackageManager.MATCH_DEFAULT_ONLY)
        return activities.isNotEmpty()
     }

}