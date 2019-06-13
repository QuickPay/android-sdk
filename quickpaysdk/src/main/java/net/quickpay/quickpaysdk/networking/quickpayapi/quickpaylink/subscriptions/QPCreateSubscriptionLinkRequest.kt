package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.subscriptions

import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.QuickPayActivity
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.ObjectRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPSubscriptionLink
import org.json.JSONObject

class QPCreateSubscriptionLinkRequest(params: QPCreateSubscriptionLinkParameters): QPrequest() {

    private val params: QPCreateSubscriptionLinkParameters = params

    fun sendRequest(successListerner: (QPSubscriptionLink)->Unit, errorListener: ()->Unit) {

        params.cancel_url = QuickPayActivity.CANCEL_URL
        params.continue_url = QuickPayActivity.CONTINUE_URL

        var paramJson = Gson().toJson(params)
        QuickPay.log(paramJson)


        var success = Response.Listener<QPSubscriptionLink>() {
            successListerner.invoke(it)
        }
        var error = Response.ErrorListener {
            var error = String(it.networkResponse.data)

            QuickPay.log("ERROR: $error")
            QuickPay.log("MESSAGE: ${it.message}")

            errorListener.invoke()
        }

        var request = ObjectRequest<QPSubscriptionLink>(Request.Method.PUT, "$quickPayApiBaseUrl/subscriptions/${params.id}/link", this.params, QPSubscriptionLink::class.java, success, error)
        request.headers = createHeaders()
        NetworkUtility.getInstance().addNetworkRequest(request)
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