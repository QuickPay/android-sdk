package net.quickpay.quickpayexample

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import net.quickpay.quickpaysdk.PaymentMethodsFragment
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.QuickPayActivity
import net.quickpay.quickpaysdk.dummy.DummyContent
import net.quickpay.quickpaysdk.networking.quickpayapi.quickpaylink.payments.*
import java.util.UUID

class MainActivity : AppCompatActivity(), PaymentMethodsFragment.OnListFragmentInteractionListener, ShopItemComponent.ShopItemComponentListener {

    companion object {
        private const val TSHIRT_PRICE = 1.0
        private const val FOOTBALL_PRICE = 0.5
    }


    // Properties

    private var shopItemCompomentTshit: ShopItemComponent? = null
    private var shopItemCompomentFootball: ShopItemComponent? = null
    private var currentPaymentId: Int? = null

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

        updateSummary()
    }


    // Buttons

    fun onCreditCardClicked(v: View) {
        var uuid = UUID.randomUUID().toString()
        uuid = uuid.replace("-", "")
        uuid = uuid.substring(15)
        QuickPay.log("ORDER_ID: $uuid")

        var params = QPCreatePaymentParameters("DKK", uuid)
        var request = QPCreatePaymentRequest(params)

        request.sendRequest( successListerner = {
            QuickPay.log("WEEEEE: ${it.id}")
            currentPaymentId = it.id

            var makeLinkRequestParams = QPCreatePaymentLinkParameters(it.id, 100.0)
            var makeLinkRequest = QPCreatePaymentLinkRequest(makeLinkRequestParams)

            makeLinkRequest.sendRequest(successListerner = {
                QuickPay.log("CREATE LINK YESSSS: ${it.url}")


                // Now the fun begins
                QuickPayActivity.openQuickPayPaymentURL(this, it.url)

            }, errorListener = {
                QuickPay.log("CREATE LINK NOOOO")
            })


        }, errorListener = {
            QuickPay.log("NOOOOOO")
        })
    }

    fun onMobilePayClicked(v: View) {
        var uuid = UUID.randomUUID().toString()
        uuid = uuid.replace("-", "")
        uuid = uuid.substring(15)
        QuickPay.log("ORDER_ID: $uuid")

        var params = QPCreatePaymentParameters("DKK", uuid)
        var request = QPCreatePaymentRequest(params)

        request.sendRequest( successListerner = {
            QuickPay.log("WEEEEE: ${it.id}")
            currentPaymentId = it.id


            var mpp = MobilePayParameters("quickpayexampleshop://open", "dk", "https://quickpay.net/images/payment-methods/payment-methods.png")
            var sessionParams = QPCreatePaymentSessionParameters(100, mpp)
            var sessionRequest = QPCreatePaymentSessionRequest(it.id, sessionParams)

            sessionRequest.sendRequest(successListerner = {
                QuickPay.log("SESSION OK")

                QuickPay.instance.authorizeWithMobilePay(it, listener = {

                })

            }, errorListener = {
                QuickPay.log("SESSION FAILED")
            })

        }, errorListener = {
            QuickPay.log("NOOOOOO")
        })
    }


    // Utils

    private fun updateSummary() {
        var tShirtCount = shopItemCompomentTshit?.counter ?: 0
        var footballCount = shopItemCompomentFootball?.counter ?: 0

        var tshirtTotal = TSHIRT_PRICE * tShirtCount
        var footballTotal = FOOTBALL_PRICE * footballCount

        findViewById<TextView>(R.id.shop_summary_tshirt_count)?.text = "T-Shirt x $tShirtCount"
        findViewById<TextView>(R.id.shop_summary_tshirt_price)?.text = "$tshirtTotal DKK"

        findViewById<TextView>(R.id.shop_summary_football_count)?.text = "T-Shirt x $tShirtCount"
        findViewById<TextView>(R.id.shop_summary_football_price)?.text = "$footballTotal DKK"

        findViewById<TextView>(R.id.shop_summary_total_price)?.text = "${tshirtTotal+footballTotal} DKK"
    }


    override fun counterChanged(component: ShopItemComponent, count: Int) {
        updateSummary()
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Toast.makeText(this, "HEJSA", Toast.LENGTH_SHORT).show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == QuickPayActivity.QUICKPAY_INTENT_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                QuickPay.log("DATA: ${data?.data}")

                val returnedResult = data!!.data!!.toString()
                if (returnedResult == QuickPayActivity.SUCCESS_RESULT) {

                    if (currentPaymentId != null) {
                        var paymentId = currentPaymentId
                        QPGetPaymentRequest(paymentId ?: 0).sendRequest(successListerner = {
                            Toast.makeText(this, "Success: ${it.acquirer}", Toast.LENGTH_LONG).show()
                        }, errorListener = {
                            QuickPay.log("NOT GOOOOOOD")
                        })
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
