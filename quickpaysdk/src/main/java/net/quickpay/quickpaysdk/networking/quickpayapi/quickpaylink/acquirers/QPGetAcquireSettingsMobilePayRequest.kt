package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.acquirers

import com.android.volley.Request
import com.android.volley.Response
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.ObjectRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import org.json.JSONObject

internal class QPGetAcquireSettingsMobilePayRequest : QPrequest() {

    internal fun sendRequest(successListerner: Response.Listener<QPMobilePaySettings>, errorListener: Response.ErrorListener) {
        var request = ObjectRequest<QPMobilePaySettings>(Request.Method.GET, "$quickPayApiBaseUrl/acquirers/mobilepay", null, QPMobilePaySettings::class.java, successListerner, errorListener)
        request.headers = createHeaders()
        NetworkUtility.getInstance().addNetworkRequest(request)
    }

}

internal class QPMobilePaySettings : JSONObject() {

    var active: Boolean = false
    var delivery_limited_to: String? = null

}