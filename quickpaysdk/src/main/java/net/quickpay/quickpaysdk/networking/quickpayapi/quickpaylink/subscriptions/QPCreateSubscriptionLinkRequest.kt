package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.subscriptions

import com.android.volley.Request
import net.quickpay.quickpaysdk.QuickPayActivity
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPSubscriptionLink
import org.json.JSONObject

class QPCreateSubscriptionLinkRequest(params: QPCreateSubscriptionLinkParameters): QPrequest<QPSubscriptionLink>(Request.Method.PUT, "/subscriptions/${params.id}/link", params, QPSubscriptionLink::class.java) {

    init {
        params.cancel_url = QuickPayActivity.FAILURE_URL
        params.continue_url = QuickPayActivity.SUCCESS_URL
    }

}

class QPCreateSubscriptionLinkParameters(id: Int, amount: Double): JSONObject() {

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

}