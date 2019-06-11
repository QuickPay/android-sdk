package net.quickpay.quickpaysdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class QuickPayActivity extends AppCompatActivity {
    public final static String CONTINUE_URL = "https://qp.payment.success";
    public final static String CANCEL_URL = "https://qp.payment.failure";

    public static final String SUCCESS_RESULT = "Success";
    public static final String CANCEL_RESULT = "Cancel";

    public static final String urlPropertyName = "quickpayLink";
    public static final String LOGTAG = "Quickpay Act";
    public static final String DEFAULT_VERSION = "v10";
    public static final int QUICKPAY_INTENT_CODE = 1318;

    /**
     * Opens a view that allows you to enter payment information.
     *
     * @param a   The activity from which to send an intent.
     * @param URL The QuickPay url, which was created by using a GeneratePaymentLinkRequest.
     */
    public static void openQuickPayPaymentURL(Activity a, String URL) {
        Intent intent = new Intent(a, QuickPayActivity.class);
        intent.putExtra(urlPropertyName, URL);
        a.startActivityForResult(intent, QUICKPAY_INTENT_CODE);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String requestedUrl = getIntent().getStringExtra(urlPropertyName);
        setContentView(R.layout.activity_quick_pay);

        final WebView webView = (WebView) findViewById(R.id.quickpay_webview);
        WebSettings webSettings;
        if (webView != null) {
            webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webView.setWebViewClient(new OverrideUrl(this));
            webView.loadUrl(requestedUrl);
        }
    }

    public class OverrideUrl extends WebViewClient {
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
                Log.e(LOGTAG, "onReceivedError: " + request.getUrl());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e(LOGTAG, "onReceivedError: Error: " + error.getDescription());
            }
        }
    }
}
