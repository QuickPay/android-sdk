package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments

import com.android.volley.Request
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPayment

class QPGetPaymentRequest(id: Int): QPrequest<QPPayment>(Request.Method.GET, "/payments/$id", null, QPPayment::class.java)