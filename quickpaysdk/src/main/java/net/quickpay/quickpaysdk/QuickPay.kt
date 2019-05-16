package net.quickpay.quickpaysdk

import java.lang.RuntimeException

class QuickPay {

    companion object {

        // Static Properties

        var apiKey: String? = null
            get() {
                if (field != null) {
                    return field
                }
                else {
                    throw RuntimeException("The QuickPay SDK needs to be initialized before usage. \nQuickPay.init(\"<API_KEY>\")")
                }
            }
        private set(value) {
            field = value
        }


        // Static Init

        fun init(apiKey: String) {
            this.apiKey = apiKey
        }
    }

}