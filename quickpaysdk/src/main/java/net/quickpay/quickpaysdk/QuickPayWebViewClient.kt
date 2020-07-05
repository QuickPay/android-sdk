package net.quickpay.quickpaysdk

import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import net.quickpay.quickpaysdk.QuickPayActivity.Companion.FAILURE_URL
import net.quickpay.quickpaysdk.QuickPayActivity.Companion.SUCCESS_URL


public class QuickPayWebViewClient(
    private val onDone: (data: String) -> Unit,
    private val onError: (error: WebResourceError?) -> Unit = {}
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean =
        when {
            url.contains(SUCCESS_URL) -> handleResult(QuickPayActivity.SUCCESS_RESULT)
            url.contains(FAILURE_URL) -> handleResult(QuickPayActivity.FAILURE_RESULT)
            else -> false
        }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        onError(error)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> QuickPay.Companion.log("onReceivedError: " + request?.url)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> QuickPay.Companion.log("onReceivedError: Error: " + error?.description)
        }
    }

    private fun handleResult(data: String): Boolean {
        onDone(data)
        return true
    }
}