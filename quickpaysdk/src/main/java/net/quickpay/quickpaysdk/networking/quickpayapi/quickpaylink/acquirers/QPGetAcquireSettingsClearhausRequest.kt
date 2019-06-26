package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.acquirers

import com.android.volley.Request
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import org.json.JSONObject

internal class QPGetAcquireSettingsClearhausRequest(): QPrequest<QPClearhausSettings>(Request.Method.GET, "/acquirers/clearhaus", null, QPClearhausSettings::class.java) {}

internal class QPClearhausSettings : JSONObject() {

    var active: Boolean = false
    var api_key: String = ""
    var apple_pay: Boolean = false
    var recurring: Boolean = false
    var payout: Boolean = false
    var mpi_merchant_id: String? = null

}