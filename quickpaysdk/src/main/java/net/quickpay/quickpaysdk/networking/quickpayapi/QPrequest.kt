package net.quickpay.quickpaysdk.networking.quickpayapi

import com.android.volley.Response
import com.google.gson.Gson
import net.quickpay.quickpaysdk.networking.NetworkUtility
import net.quickpay.quickpaysdk.networking.ObjectRequest
import org.json.JSONObject
import java.lang.StringBuilder
import kotlin.collections.HashMap

/**
 * The QPRequest is a utility class that bridges the gap between QuickPay domain code and the general purpose networking code
 */
open class QPrequest<T>(private val method: Int, private val path: String, protected val params: JSONObject?, private val clazz: Class<T>) {

    // Static

    companion object {
        protected const val quickPayApiBaseUrl = "https://api.quickpay.net"
    }


    // Util

    private fun createHeaders() : Map<String, String> {
        val qpHeaders = QPHeaders()

        val headers = HashMap<String, String>()
        headers["Authorization"] = qpHeaders.encodedAuthorization()
        headers["Accept-Version"] = qpHeaders.acceptVersion
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"

        return headers
    }

    fun sendRequest(listener: (T) -> Unit, errorListener: ((statusCode: Int?, message: String?, qpError: QPError?) -> Unit)?) {
        val request = ObjectRequest<T>(method, "$quickPayApiBaseUrl$path", params, clazz, Response.Listener {
            listener.invoke(it)
        }, Response.ErrorListener {
            val message: String? = try {
                String(it.networkResponse.data)
            } catch (e: Exception) {
                it.message
            }

            val qpError: QPError? = try {
                Gson().fromJson(message, QPError::class.java)
            }
            catch (e: Exception) {
                null
            }

            errorListener?.invoke(it.networkResponse?.statusCode, it.message, qpError)
        })

        request.headers = createHeaders()

        /*
        // Debug info
        QuickPay.log("Adding request to queue")
        QuickPay.log("Method: ${request.method}")
        QuickPay.log("Url: ${request.url}")
        QuickPay.log("Headers: ${request.headers}")
        */

        NetworkUtility.getInstance().addNetworkRequest(request)
    }
}

class QPError : JSONObject() {

    var message: String = ""
    var errors: Map<String, Array<String>>? = null
    var error_code: String? = null

    override fun toString(): String {
        val sb = StringBuilder("Message: $message")
        sb.append("\n")

        sb.append("Error_Code: $error_code")
        sb.append("\n")

        if(errors != null && errors?.isNotEmpty() == true) {
            errors?.keys?.forEach { key: String? ->
                sb.append("$key - ${errors?.get(key)?.firstOrNull()}")
                sb.append("\n")
            }
        }

        return sb.toString()
    }
}