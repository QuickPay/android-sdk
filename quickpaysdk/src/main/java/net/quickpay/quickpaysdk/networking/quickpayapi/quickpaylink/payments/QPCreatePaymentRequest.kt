package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments

import com.android.volley.Request
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPAddress
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPBasket
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPayment
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPShipping
import org.json.JSONObject

class QPCreatePaymentRequest(params: QPCreatePaymentParameters): QPrequest<QPPayment>(Request.Method.POST, "/payments", params, QPPayment::class.java)

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