package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments

import com.android.volley.Request
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPayment
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPerson
import org.json.JSONObject

class QPCreatePaymentSessionRequest(id: Int, params: QPCreatePaymentSessionParameters): QPrequest<QPPayment>(Request.Method.POST, "/payments/$id/session?synchronized", params, QPPayment::class.java)

class QPCreatePaymentSessionParameters(amount: Int): JSONObject() {

    constructor(amount: Int, mobilePayParameters: MobilePayParameters): this(amount) {
        extras["mobilepay"] = mobilePayParameters
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