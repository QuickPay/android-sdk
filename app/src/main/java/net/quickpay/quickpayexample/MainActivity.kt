package net.quickpay.quickpayexample

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import net.quickpay.quickpaysdk.PaymentMethod
import net.quickpay.quickpaysdk.ui.PaymentMethodsFragment
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.QuickPayActivity
import net.quickpay.quickpaysdk.ui.PaymentContent
import net.quickpay.quickpaysdk.networking.quickpayapi.QPError
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments.*
import java.util.UUID

class MainActivity : AppCompatActivity(), PaymentMethodsFragment.OnPaymentMethodsListFragmentInteractionListener, ShopItemComponent.ShopItemComponentListener {

    // Static

    companion object {
        private const val TSHIRT_PRICE = 1.0
        private const val FOOTBALL_PRICE = 0.5

        private const val CURRENT_ID_KEY = "CURRENT_ID_KEY"
    }


    // Properties

    private var shopItemCompomentTshit: ShopItemComponent? = null
    private var shopItemCompomentFootball: ShopItemComponent? = null
    private var currentPaymentId: Int = 0
    private var selectedPaymentMethod: PaymentMethod? = null


    // Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentPaymentId = savedInstanceState?.getInt(CURRENT_ID_KEY) ?: 0

        // Init the QuickPay API
        QuickPay.init("f1a4b80189c73862655552d06f9419dd7574c65de916fef88cf9854f6907f1b4", this)

        shopItemCompomentFootball = supportFragmentManager.findFragmentById(R.id.shop_soccerball_fragment) as ShopItemComponent
        shopItemCompomentFootball?.setImage(R.drawable.soccerball)

        shopItemCompomentTshit = supportFragmentManager.findFragmentById(R.id.shop_tshirt_fragment) as ShopItemComponent
        shopItemCompomentTshit?.setImage(R.drawable.tshirt)

        updateSummary()

        onMobilePayReturn(intent)
    }

    private fun onMobilePayReturn(intent: Intent) {
        if (intent.data == null) {
            return
        }

        if (intent.data?.scheme?.equals("quickpayexample") == true) {
            QuickPay.log("MainActivity id: $currentPaymentId")
            QuickPay.log("Intent: $intent")
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putInt(CURRENT_ID_KEY, currentPaymentId)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == QuickPayActivity.QUICKPAY_INTENT_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                val returnedResult = data!!.data!!.toString()
                if (returnedResult == QuickPayActivity.SUCCESS_RESULT) {

                    if (currentPaymentId > 0) {
                        val getPaymentRequest = QPGetPaymentRequest(currentPaymentId)

                        getPaymentRequest.sendRequest(listener = { payment ->
                            Toast.makeText(this, "Success: ${payment.acquirer}", Toast.LENGTH_LONG).show()
                        }, errorListener = ::printError)

                        currentPaymentId = 0
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

    fun onPaymentButtonClicked(v: View) {
        if (selectedPaymentMethod == PaymentMethod.PAYMENTCARD) {
            handleCreditCardPayment()
        }
        else if (selectedPaymentMethod == PaymentMethod.MOBILEPAY) {
            handleMobilePayPayment()
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

    /**
     * TODO - Update when QuickPay has made the final revision on how to retrieve the MobilePay session id
     */
    private fun handleMobilePayPayment() {
        val createPaymentParams = QPCreatePaymentParameters("DKK", createRandomOrderId())
        val createPaymentRequest = QPCreatePaymentRequest(createPaymentParams)

        createPaymentRequest.sendRequest(listener = { payment ->
            QuickPay.log("Create Payment Success - Id = ${payment.id}")
            currentPaymentId = payment.id

            val mobilePayParameters = MobilePayParameters("quickpayexample://open?paymentid=${payment.id}", "dk", "https://quickpay.net/images/payment-methods/payment-methods.png")
            val createSessionParameters = QPCreatePaymentSessionParameters(100, mobilePayParameters)
            val createSessionRequest = QPCreatePaymentSessionRequest(payment.id, createSessionParameters)

            createSessionRequest.sendRequest(listener = { payment ->
                QuickPay.log("Create Session Success")
                QuickPay.instance.authorizeWithMobilePay(payment, this)
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

    private fun printError(statusCode: Int?, message: String?, error: QPError?) : Unit {
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
        findViewById<Button>(R.id.shop_payment_button)?.isEnabled = true
        Toast.makeText(this, "${selectedPaymentMethod?.defaultTitle()} selected", Toast.LENGTH_SHORT).show()
    }

}
