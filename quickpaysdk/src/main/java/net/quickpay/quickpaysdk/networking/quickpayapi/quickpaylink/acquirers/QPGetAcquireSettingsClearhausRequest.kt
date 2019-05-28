package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.acquirers

import com.android.volley.Request
import com.android.volley.Response
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.ObjectRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import org.json.JSONObject

internal class QPGetAcquireSettingsClearhausRequest : QPrequest() {

    internal fun sendRequest(successListerner: Response.Listener<QPClearhausSettings>, errorListener: Response.ErrorListener) {
        var request = ObjectRequest<QPClearhausSettings>(Request.Method.GET, "$quickPayApiBaseUrl/acquirers/clearhaus", null, QPClearhausSettings::class.java, successListerner, errorListener)
        request.headers = createHeaders()
        NetworkUtility.getInstance().addNetworkRequest(request)
    }

}

internal class QPClearhausSettings : JSONObject() {

    var active: Boolean = false
    var api_key: String = ""
    var apple_pay: Boolean = false
    var recurring: Boolean = false
    var payout: Boolean = false
    var mpi_merchant_id: String? = null

}