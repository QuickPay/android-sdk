package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments

import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.ObjectRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.*
import org.json.JSONObject

class QPAuthorizePaymentRequest(params: QPAuthorizePaymentParams): QPrequest() {

    var params: QPAuthorizePaymentParams = params

    fun sendRequest(successListerner: (QPPayment)->Unit, errorListener: ()->Unit) {

        var paramJson = Gson().toJson(params)
        QuickPay.log(paramJson)


        var success = Response.Listener<QPPayment>() {
            successListerner.invoke(it)
        }
        var error = Response.ErrorListener {
            var error = String(it.networkResponse.data)

            QuickPay.log("ERROR: $error")
            QuickPay.log("MESSAGE: ${it.message}")

            errorListener.invoke()
        }

        var request = ObjectRequest<QPPayment>(Request.Method.POST, "$quickPayApiBaseUrl/payments/${params.id}/authorize", this.params, QPPayment::class.java, success, error)
        request.headers = createHeaders()
        NetworkUtility.getInstance().addNetworkRequest(request)
    }
}


class QPAuthorizePaymentParams(id: Int, amount: Int): JSONObject() {

    // Required Properties

    var id: Int = id
    var amount: Int = amount


    // Optional Properties

    var quickPayCallbackUrl: String? = null // TODO: Must be encoded/decoded into 'QuickPay-Callback-Url'
    var synchronized: Boolean? = null
    var vat_rate: Double? = null
    var mobile_number: String? = null
    var auto_capture: Boolean? = null
    var acquirer: String? = null
    var autofee: Boolean? = null
    var customer_ip: String? = null
    //    var extras: Any?
    var zero_auth: Boolean? = null

    var card: QPCard? = null
    var nin: QPNin? = null
    var person: QPPerson? = null

}