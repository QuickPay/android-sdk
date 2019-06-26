package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments

import com.android.volley.Request
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.*
import org.json.JSONObject

class QPAuthorizePaymentRequest(params: QPAuthorizePaymentParams): QPrequest<QPPayment>(Request.Method.POST, "/payments/${params.id}/authorize", params, QPPayment::class.java) {}


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