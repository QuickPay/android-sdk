package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.subscriptions

import com.android.volley.Request
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.*
import org.json.JSONObject
import java.util.*

class QPCreateSubscriptionRequest(params: QPCreateSubscriptionParameters): QPrequest<QPSubscription>(Request.Method.POST, "/subscriptions", params, QPSubscription::class.java)

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