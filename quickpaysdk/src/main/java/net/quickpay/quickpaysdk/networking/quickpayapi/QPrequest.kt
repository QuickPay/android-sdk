package net.quickpay.quickpaysdk.networking.quickpayapi

open class QPrequest {

    // Properties

    internal val quickPayApiBaseUrl = "https://api.quickpay.net"
    private val headers = QPHeaders() // Make this injectable if the users should ever need to add their own headers


    // Util

    protected fun createHeaders() : Map<String, String> {
        var headers = HashMap<String, String>()
        headers["Authorization"] = this.headers.encodedAuthorization()
        headers["Accept-Version"] = this.headers.acceptVersion

        return headers
    }

}