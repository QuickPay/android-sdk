package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.Gson
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.ObjectRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPAddress
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPBasket
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPayment
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPShipping
import org.json.JSONObject

class QPCreatePaymentRequest(params: QPCreatePaymentParameters) : QPrequest() {

    private val params: QPCreatePaymentParameters = params

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

        var request = ObjectRequest<QPPayment>(Request.Method.POST, "$quickPayApiBaseUrl/payments", this.params, QPPayment::class.java, success, error)
        request.headers = createHeaders()
        NetworkUtility.getInstance().addNetworkRequest(request)
    }

}


class QPCreatePaymentParameters(currency: String,order_id: String): JSONObject() {

    // Required Properties

    var currency: String = currency
    var order_id: String = order_id


    // Optional Properties

    var branding_id: Int? = null
    var text_on_statement: String? = null
    var basket: List<QPBasket>? = ArrayList()
    var shipping: QPShipping? = null
    var invoice_address: QPAddress? = null
    var shipping_address:QPAddress? = null

}