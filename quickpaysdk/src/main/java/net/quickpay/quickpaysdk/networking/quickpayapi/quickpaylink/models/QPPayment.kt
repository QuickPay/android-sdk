package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models

import org.json.JSONObject

class QPPayment: JSONObject() {

    // Properties

    var id: Int = 0
    var merchant_id: Int = 0
    var order_id: String = ""

    var accepted: Boolean = false
    var type: String = ""
    var text_on_statement: String? = null
    var currency: String = ""
    var state: String = ""
    var test_mode: Boolean = false
    var created_at: String = ""
    var updated_at: String = ""
    var balance: Int = 0

    var branding_id: String? = null
    var acquirer: String? = null
    var facilitator: String? = null
    var retented_at: String? = null
    var fee: Int? = null
    var subscriptionId: Int? = null
    var deadline_at: String? = null

    var operations: List<QPOperation>? = null
    var shipping_address: QPAddress? = null
    var invoice_address: QPAddress? = null
    var basket: List<QPBasket>? = null
    var shipping: QPShipping? = null
    var metadata: QPMetadata? = null
    var link: QPPaymentLink? = null
}