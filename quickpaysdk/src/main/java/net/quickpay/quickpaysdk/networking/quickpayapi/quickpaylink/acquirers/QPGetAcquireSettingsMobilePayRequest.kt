package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.acquirers

import com.android.volley.Request
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import org.json.JSONObject

internal class QPGetAcquireSettingsMobilePayRequest: QPrequest<QPMobilePaySettings>(Request.Method.GET, "/acquirers/mobilepay", null, QPMobilePaySettings::class.java)

internal class QPMobilePaySettings : JSONObject() {

    var active: Boolean = false
    var delivery_limited_to: String? = null

}