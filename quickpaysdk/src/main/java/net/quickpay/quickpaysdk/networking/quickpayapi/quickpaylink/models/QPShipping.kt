package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models

import org.json.JSONObject

class QPShipping: JSONObject() {

    // Optional Properties

    var method: String? = null
    var company: String? = null
    var amount: Int? = null
    var vat_rate: Double? = null
    var tracking_number: String? = null
    var tracking_url: String? = null

}