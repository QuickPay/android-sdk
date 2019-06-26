package net.quickpay.quickpaysdk.networking.quickpayapi

import android.util.Base64
import net.quickpay.quickpaysdk.QuickPay

internal class QPHeaders {

    // Properties

    val acceptVersion
        get() = "v10"

    private val apiKey
        get() = QuickPay.instance.apiKey


    // Auth

    fun encodedAuthorization() : String {
        val credentials = String.format("%s:%s", "", apiKey)
        return "Basic " + Base64.encodeToString(
               credentials.toByteArray(),
               Base64.NO_WRAP
        ) // NO_WRAP is required, otherwise line breaks can occur, which are not allowed in http headers.
    }
}