package net.quickpay.quickpaysdk.ui

import net.quickpay.quickpaysdk.PaymentMethod
import net.quickpay.quickpaysdk.R
import java.util.ArrayList

object PaymentContent {

    val ITEMS: MutableList<PaymentItem> = ArrayList()

    init {
        // Add the card payment
//        addItem(PaymentItem("1", PaymentMethod.MOBILEPAY, R.drawable.ic_mobilepayhorizontal))
        addItem(PaymentItem("2", PaymentMethod.PAYMENTCARD, R.drawable.ic_mobilepayhorizontal))
    }

    private fun addItem(item: PaymentItem) {
        ITEMS.add(item)
    }

    data class PaymentItem(val id: String, val method: PaymentMethod, val logo: Int) {
        override fun toString(): String = method.defaultTitle()
    }
}
