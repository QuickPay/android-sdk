package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models

import org.json.JSONObject

class QPNin: JSONObject() {

    // Optional Properties

    var number: String? = null
    var country_code: String? = null
    var gender: String? = null //TODO: Convert this into the Gender enum

}