package net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.subscriptions

import com.android.volley.Request
import net.quickpay.quickpaysdk.networking.quickpayapi.QPrequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPSubscription

class QPGetSubscriptionRequest(id: Int): QPrequest<QPSubscription>(Request.Method.GET, "/subscriptions/$id", null, QPSubscription::class.java)