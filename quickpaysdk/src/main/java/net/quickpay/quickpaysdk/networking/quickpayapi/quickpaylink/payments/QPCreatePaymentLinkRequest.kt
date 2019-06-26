package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments

import com.android.volley.Request

import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPaymentLink
import org.json.JSONObject

import net.quickpay.quickpaysdk.QuickPayActivity

class QPCreatePaymentLinkRequest(params: QPCreatePaymentLinkParameters): QPrequest<QPPaymentLink>(Request.Method.PUT, "/payments/${params.id}/link", params, QPPaymentLink::class.java) {

    init {
        params.cancel_url = QuickPayActivity.CANCEL_URL
        params.continue_url = QuickPayActivity.CONTINUE_URL
    }

}

class QPCreatePaymentLinkParameters(id: Int, amount: Double): JSONObject() {

    // Required Properties

    var id: Int = id
    var amount: Double = amount


    // Optional Properties

    var agreement_id: Int? = null
    var language: String? = null
    var continue_url: String? = null
    var cancel_url: String? = null
    var callback_url: String? = null
    var payment_methods: String? = null
    var auto_fee: Boolean? = null
    var branding_id: Int? = null
    var google_analytics_tracking_id: String? = null
    var google_analytics_client_id: String? = null
    var acquirer: String? = null
    var deadline: String? = null
    var framed: Int? = null
    //    var branding_config: Any?
    var customer_email: String? = null
    var invoice_address_selection: Boolean? = null
    var shipping_address_selection: Boolean? = null
    var auto_capture: Int? = null

}