/*
interface InitializeListener {

    fun initializationStarted()
    fun initializationCompleted()

}
*/


//        private const val MOBILE_PAY_SCHEME = "mobilepayonline://"


//            instance.fetchAcquirers(context)





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
