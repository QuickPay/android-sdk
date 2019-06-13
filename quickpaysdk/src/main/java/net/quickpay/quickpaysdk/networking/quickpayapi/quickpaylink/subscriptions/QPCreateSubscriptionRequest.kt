package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.subscriptions

import com.android.volley.Request
import com.android.volley.Response
import com.google.gson.Gson
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.ObjectRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.*
import org.json.JSONObject
import java.util.*

class QPCreateSubscriptionRequest(params: QPCreateSubscriptionParameters): QPrequest() {

    private val params: QPCreateSubscriptionParameters = params

    fun sendRequest(successListerner: (QPSubscription)->Unit, errorListener: ()->Unit) {

        var paramJson = Gson().toJson(params)
        QuickPay.log(paramJson)


        var success = Response.Listener<QPSubscription>() {
            successListerner.invoke(it)
        }
        var error = Response.ErrorListener {
            var error = String(it.networkResponse.data) // TODO: This can be null and crash

            QuickPay.log("ERROR: $error")
            QuickPay.log("MESSAGE: ${it.message}")

            errorListener.invoke()
        }

        var request = ObjectRequest<QPSubscription>(Request.Method.POST, "$quickPayApiBaseUrl/subscriptions", this.params, QPSubscription::class.java, success, error)
        request.headers = createHeaders()
        NetworkUtility.getInstance().addNetworkRequest(request)
    }

}


class QPCreateSubscriptionParameters(currency: String, order_id: String, description: String): JSONObject() {

    // Required Properties

    var order_id: String = order_id
    var currency: String = currency
    var description: String = description


    // Optional Properties

    var branding_id: Int? = null
    var text_on_statement: String? = null
    var basket: MutableList<QPBasket>? = LinkedList<QPBasket>()
    var shipping: QPShipping? = null
    var invoice_address: QPAddress? = null
    var shipping_address:QPAddress? = null
    var group_ids: Array<Int>? = null
    var shopsystem: Array<QPShopSystem>? = null

}