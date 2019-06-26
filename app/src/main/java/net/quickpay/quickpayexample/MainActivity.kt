package net.quickpay.quickpayexample

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import net.quickpay.quickpaysdk.PaymentMethodsFragment
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.QuickPayActivity
import net.quickpay.quickpaysdk.dummy.DummyContent
import net.quickpay.quickpaysdk.networking.quickpayapi.QPError
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments.*
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.subscriptions.QPCreateSubscriptionLinkParameters
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.subscriptions.QPCreateSubscriptionLinkRequest
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.subscriptions.QPCreateSubscriptionParameters
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.subscriptions.QPCreateSubscriptionRequest
import java.util.UUID

class MainActivity : AppCompatActivity(), PaymentMethodsFragment.OnListFragmentInteractionListener, ShopItemComponent.ShopItemComponentListener {

    companion object {
        private const val TSHIRT_PRICE = 1.0
        private const val FOOTBALL_PRICE = 0.5

        private const val CURRENT_ID_KEY = "CURRENT_ID_KEY"
    }


    // Properties

    private var shopItemCompomentTshit: ShopItemComponent? = null
    private var shopItemCompomentFootball: ShopItemComponent? = null
    private var currentPaymentId: Int = 0


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

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putInt(CURRENT_ID_KEY, currentPaymentId)
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


    // Buttons

    fun onCreditCardClicked(v: View) {
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

    fun onMobilePayClicked(v: View) {
        /*
        var uuid = UUID.randomUUID().toString()
        uuid = uuid.replace("-", "")
        uuid = uuid.substring(15)
        QuickPay.log("ORDER_ID: $uuid")

        var params = QPCreatePaymentParameters("DKK", uuid)
        var request = QPCreatePaymentRequest(params)

        request.sendRequest( successListerner = {
            QuickPay.log("WEEEEE: ${it.id}")
            currentPaymentId = it.id


            var mpp = MobilePayParameters("quickpayexample://open?paymentid=${it.id}", "dk", "https://quickpay.net/images/payment-methods/payment-methods.png")
            var sessionParams = QPCreatePaymentSessionParameters(100, mpp)
            var sessionRequest = QPCreatePaymentSessionRequest(it.id, sessionParams)

            sessionRequest.sendRequest(successListerner = {
                QuickPay.log("SESSION OK")
                QuickPay.instance.authorizeWithMobilePay(it, applicationContext)
            }, errorListener = {
                QuickPay.log("SESSION FAILED")
            })

        }, errorListener = {
            QuickPay.log("NOOOOOO")
        })
        */
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


    override fun counterChanged(component: ShopItemComponent, count: Int) {
        updateSummary()
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Toast.makeText(this, "HEJSA", Toast.LENGTH_SHORT).show()
    }

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


    // Lifecycle

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
}
