package net.quickpay.quickpaysdk

enum class PaymentMethod(val id: String) {

    MOBILEPAY("PaymentMobilePay"),
    PAYMENTCARD("PaymentCard");

    fun defaultTitle(): String {
        if (this == MOBILEPAY) {
            return "MobilePay"
        }
        else if (this == PAYMENTCARD) {
            return "Cards"
        }
        else {
            return ""
        }
    }

}