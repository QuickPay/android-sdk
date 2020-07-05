package net.quickpay.quickpaysdk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPaymentLink
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPSubscriptionLink

class QuickPayActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestedUrl = intent.getStringExtra(urlPropertyName)
        setContentView(R.layout.activity_quick_pay)

        val webView = findViewById<WebView>(R.id.quickpay_webview)
        if (webView != null) {
            webView.settings.javaScriptEnabled = true
            webView.webChromeClient = WebChromeClient()

            webView.webViewClient = QuickPayWebViewClient(onDone = {
                val data = Intent()
                data.data = Uri.parse(it)

                // Close down the activity and deliver a result.
                this.setResult(Activity.RESULT_OK, data)
                this.finish()
            });

            webView.loadUrl(requestedUrl)
        }
    }

    companion object {
        const val SUCCESS_URL = "https://qp.payment.success"
        const val FAILURE_URL = "https://qp.payment.failure"
        const val SUCCESS_RESULT = "Success"
        const val FAILURE_RESULT = "Failure"
        private const val urlPropertyName = "quickpayLink"
        const val QUICKPAY_INTENT_CODE = 1318

        /**
         * Opens a view that allows you to enter payment information.
         *
         * @param a    The activity from which to send an intent.
         * @param link The QuickPay payment link, which was created by using a QPCreatePaymentLinkRequest.
         */
        fun openQuickPayPaymentWindow(a: Activity, link: QPPaymentLink) {
            openQuickPayPaymentWindow(a, link.url)
        }

        /**
         * Opens a view that allows you to enter payment information.
         *
         * @param a    The activity from which to send an intent.
         * @param link The QuickPay subscription link, which was created by using a QPCreateSubscriptionLinkRequest.
         */
        fun openQuickPayPaymentWindow(a: Activity, link: QPSubscriptionLink) {
            openQuickPayPaymentWindow(a, link.url)
        }

        private fun openQuickPayPaymentWindow(a: Activity, URL: String) {
            val intent = Intent(a, QuickPayActivity::class.java)
            intent.putExtra(urlPropertyName, URL)
            a.startActivityForResult(intent, QUICKPAY_INTENT_CODE)
        }
    }
}