package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.subscriptions

import com.android.volley.Request
import com.android.volley.Response
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.ObjectRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPSubscription

class QPGetSubscriptionRequest(id: Int): QPrequest() {

    private val id: Int = id

    fun sendRequest(successListerner: (QPSubscription)->Unit, errorListener: ()->Unit) {
        var success = Response.Listener<QPSubscription>() {
            successListerner.invoke(it)
        }
        var error = Response.ErrorListener {
            var error = String(it.networkResponse.data) // TODO: This can be null and crash

            QuickPay.log("ERROR: $error")
            QuickPay.log("MESSAGE: ${it.message}")

            errorListener.invoke()
        }

        var request = ObjectRequest<QPSubscription>(Request.Method.GET, "$quickPayApiBaseUrl/subscriptions/$this.id", null, QPSubscription::class.java, success, error)
        request.headers = createHeaders()
        NetworkUtility.getInstance().addNetworkRequest(request)
    }

}