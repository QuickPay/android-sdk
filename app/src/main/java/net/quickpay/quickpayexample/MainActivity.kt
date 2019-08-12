package net.quickpay.quickpayexample

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import net.quickpay.quickpaysdk.PaymentMethod
import net.quickpay.quickpaysdk.ui.PaymentMethodsFragment
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.QuickPayActivity
import net.quickpay.quickpaysdk.networking.quickpayapi.QPError
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments.*
import java.util.UUID

class MainActivity : AppCompatActivity(), PaymentMethodsFragment.OnPaymentMethodsListFragmentInteractionListener, ShopItemComponent.ShopItemComponentListener {

    // Static

    companion object {
        private const val TSHIRT_PRICE = 1.0
        private const val FOOTBALL_PRICE = 0.5
    }


    // Properties

    private var shopItemCompomentTshit: ShopItemComponent? = null
    private var shopItemCompomentFootball: ShopItemComponent? = null
    private var progressBar: ProgressBar? = null
    private var checkoutButton: Button? = null
    private var currentPaymentId: Int? = null
    private var selectedPaymentMethod: PaymentMethod? = null


    // Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init the QuickPay API
        QuickPay.init("f1a4b80189c73862655552d06f9419dd7574c65de916fef88cf9854f6907f1b4", this)

        shopItemCompomentFootball = supportFragmentManager.findFragmentById(R.id.shop_soccerball_fragment) as ShopItemComponent
        shopItemCompomentFootball?.setImage(R.drawable.soccerball)

        shopItemCompomentTshit = supportFragmentManager.findFragmentById(R.id.shop_tshirt_fragment) as ShopItemComponent
        shopItemCompomentTshit?.setImage(R.drawable.tshirt)

        progressBar = findViewById(R.id.shop_progressbar)
        checkoutButton = findViewById(R.id.shop_payment_button)

        updateSummary()
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == QuickPayActivity.QUICKPAY_INTENT_CODE) {
            progressBar?.visibility = View.INVISIBLE
            checkoutButton?.isEnabled = true

            if (resultCode == Activity.RESULT_OK) {

                val returnedResult = data!!.data!!.toString()
                if (returnedResult == QuickPayActivity.SUCCESS_RESULT) {

                    if (currentPaymentId != null) {
                        val getPaymentRequest = QPGetPaymentRequest(currentPaymentId!!)

                        getPaymentRequest.sendRequest(listener = { payment ->
                            Toast.makeText(this, "Success - Acquirer is ${payment.acquirer}", Toast.LENGTH_LONG).show()
                        }, errorListener = ::printError)

                        currentPaymentId = null
                    }
                }
                else if (returnedResult == QuickPayActivity.CANCEL_RESULT) {
                    val toast = Toast.makeText(this, "Result: $returnedResult", Toast.LENGTH_LONG)
                    toast.show()
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                val toast = Toast.makeText(this, "The user cancelled out of the activity", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }


    // UI Buttons

    @Suppress("UNUSED_PARAMETER")
    fun onPaymentButtonClicked(v: View) {
        if (selectedPaymentMethod != null) {
            progressBar?.visibility = View.VISIBLE
            checkoutButton?.isEnabled = false
        }

        if (selectedPaymentMethod == PaymentMethod.PAYMENTCARD) {
            handleCreditCardPayment()
        }
    }


    // Payment Handling

    private fun handleCreditCardPayment() {
        val createPaymentParams = QPCreatePaymentParameters("DKK", createRandomOrderId())
        val createPaymentRequest = QPCreatePaymentRequest(createPaymentParams)

        createPaymentRequest.sendRequest(listener = { payment ->
            QuickPay.log("Create Payment Success - Id = ${payment.id}")
            currentPaymentId = payment.id

            val createPaymentLinkParameters = QPCreatePaymentLinkParameters(payment.id, 100.0)
            val createPaymentLinkRequest = QPCreatePaymentLinkRequest(createPaymentLinkParameters)

            createPaymentLinkRequest.sendRequest(listener = { paymentLink ->
                QuickPay.log("Create Payment Link Success - Url = ${paymentLink.url}")

                // Now we open the payment window
                QuickPayActivity.openQuickPayPaymentWindow(this, paymentLink)
            }, errorListener = ::printError)
        }, errorListener = ::printError)
    }


    // Utils

    private fun updateSummary() {
        var tShirtCount = shopItemCompomentTshit?.counter ?: 0
        var footballCount = shopItemCompomentFootball?.counter ?: 0

        var tshirtTotal = TSHIRT_PRICE * tShirtCount
        var footballTotal = FOOTBALL_PRICE * footballCount

        findViewById<TextView>(R.id.shop_summary_tshirt_count)?.text = String.format(resources.getString(R.string.tshirt_count), tShirtCount)
        findViewById<TextView>(R.id.shop_summary_tshirt_price)?.text = String.format(resources.getString(R.string.total_dkk), tshirtTotal)

        findViewById<TextView>(R.id.shop_summary_football_count)?.text = String.format(resources.getString(R.string.football_count), footballCount)
        findViewById<TextView>(R.id.shop_summary_football_price)?.text = String.format(resources.getString(R.string.total_dkk), footballTotal)

        findViewById<TextView>(R.id.shop_summary_total_price)?.text = String.format(resources.getString(R.string.total_dkk), tshirtTotal+footballTotal)
    }

    /**
     * In a real application you should not generate random order ids.
     * This is only for demonstration purposes.
     */
    private fun createRandomOrderId(): String {
        return UUID.randomUUID().toString().replace("-", "").substring(15)
    }


    // Error handling

    private fun printError(statusCode: Int?, message: String?, error: QPError?) {
        QuickPay.log("Request error")
        QuickPay.log("Message: $message")
        QuickPay.log("StatusCode: $statusCode")
        QuickPay.log("Errors: ${error.toString()}")
    }


    // ShopItemComponentListener implementation

    override fun counterChanged(component: ShopItemComponent, count: Int) {
        updateSummary()
    }


    // OnPaymentMethodsListFragmentInteractionListener implementation

    override fun onPaymentMethodSelected(paymentMethod: PaymentMethod) {
        selectedPaymentMethod = paymentMethod

        if (progressBar?.visibility == View.INVISIBLE) {
            checkoutButton?.isEnabled = true
        }
    }

}
