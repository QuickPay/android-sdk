package net.quickpay.quickpaysdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPPaymentLink;
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.models.QPSubscriptionLink;


public class QuickPayActivity extends AppCompatActivity {
    public final static String CONTINUE_URL = "https://qp.payment.success";
    public final static String CANCEL_URL = "https://qp.payment.failure";

    public static final String SUCCESS_RESULT = "Success";
    public static final String CANCEL_RESULT = "Cancel";

    private static final String urlPropertyName = "quickpayLink";
    public static final int QUICKPAY_INTENT_CODE = 1318;

    /**
     * Opens a view that allows you to enter payment information.
     *
     * @param a    The activity from which to send an intent.
     * @param link The QuickPay payment link, which was created by using a QPCreatePaymentLinkRequest.
     */
    public static void openQuickPayPaymentWindow(Activity a, QPPaymentLink link) {
        openQuickPayPaymentWindow(a, link.getUrl());
    }

    /**
     * Opens a view that allows you to enter payment information.
     *
     * @param a    The activity from which to send an intent.
     * @param link The QuickPay subscription link, which was created by using a QPCreateSubscriptionLinkRequest.
     */
    public static void openQuickPayPaymentWindow(Activity a, QPSubscriptionLink link) {
        openQuickPayPaymentWindow(a, link.getUrl());
    }

    private static void openQuickPayPaymentWindow(Activity a, String URL) {
        Intent intent = new Intent(a, QuickPayActivity.class);
        intent.putExtra(urlPropertyName, URL);
        a.startActivityForResult(intent, QUICKPAY_INTENT_CODE);
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String requestedUrl = getIntent().getStringExtra(urlPropertyName);
        setContentView(R.layout.activity_quick_pay);

        final WebView webView = findViewById(R.id.quickpay_webview);

        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);

            webView.setWebViewClient(new OverrideUrl(this));
            webView.loadUrl(requestedUrl);
        }
    }

        private class OverrideUrl extends WebViewClient {
            private final Activity activity;

            OverrideUrl(Activity activity) {
                this.activity = activity;
            }

            private void done(String result) {
                Intent data = new Intent();
            data.setData(Uri.parse(result));

            // Close down the activity and deliver a result.
            this.activity.setResult(RESULT_OK, data);
            this.activity.finish();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains(CONTINUE_URL)) {
                this.done(SUCCESS_RESULT);
                return true;
            }
            else if (url.contains(CANCEL_URL)) {
                this.done(CANCEL_RESULT);
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view,
                                    WebResourceRequest request,
                                    WebResourceError error) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                QuickPay.Companion.log("onReceivedError: " + request.getUrl());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                QuickPay.Companion.log("onReceivedError: Error: " + error.getDescription());
            }
        }
    }
}
