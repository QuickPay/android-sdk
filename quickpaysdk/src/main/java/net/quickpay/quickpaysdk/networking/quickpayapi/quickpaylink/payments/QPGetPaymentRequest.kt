package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments

import com.android.volley.Request
import com.android.volley.Response
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.ObjectRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPayment

class QPGetPaymentRequest(id: Int): QPrequest() {

    // Properties

    var id: Int = id


    fun sendRequest(successListerner: (QPPayment)->Unit, errorListener: ()->Unit) {

        var success = Response.Listener<QPPayment>() {
            successListerner.invoke(it)
        }
        var error = Response.ErrorListener {
            var error = String(it.networkResponse.data)

            QuickPay.log("ERROR: $error")
            QuickPay.log("MESSAGE: ${it.message}")

            errorListener.invoke()
        }

            var request = ObjectRequest<QPPayment>(Request.Method.GET, "$quickPayApiBaseUrl/payments/$id", null, QPPayment::class.java, success, error)
        request.headers = createHeaders()
        NetworkUtility.getInstance().addNetworkRequest(request)
    }

}