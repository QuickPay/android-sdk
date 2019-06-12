package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments

import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.ObjectRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPayment
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPerson
import org.json.JSONObject

class QPCreatePaymentSessionRequest(id: Int, params: QPCreatePaymentSessionParameters): QPrequest() {


    var id: Int = id
    var params: QPCreatePaymentSessionParameters = params


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

        var request = ObjectRequest<QPPayment>(Request.Method.POST, "$quickPayApiBaseUrl/payments/${this.id}/session?synchronized", this.params, QPPayment::class.java, success, error)
        request.headers = createHeaders()
        NetworkUtility.getInstance().addNetworkRequest(request)
    }

}


class QPCreatePaymentSessionParameters(amount: Int): JSONObject() {

    constructor(amount: Int, mobilePayParameters: MobilePayParameters): this(amount) {
        extras.put("mobilepay", mobilePayParameters)
        acquirer = "mobilepay"
    }


    // Required Properties

    var amount: Int = amount


    // Optional Properties

    var auto_capture: Boolean? = null
    var acquirer: String? = null
    var autofee: Boolean? = null
    var customer_ip: String? = null
    var person: QPPerson? = null
    var extras: MutableMap<String, Any> = HashMap<String, Any>()

}

class MobilePayParameters(return_url: String): JSONObject() {

    constructor(return_url: String, language: String?, shop_logo_url: String?) : this(return_url) {
        this.language = language
        this.shop_logo_url = shop_logo_url
    }


    // Required Properties

    var return_url: String = return_url


    // Optional Properties

    var language: String? = null
    var shop_logo_url: String? = null

}