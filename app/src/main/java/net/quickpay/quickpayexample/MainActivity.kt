package net.quickpay.quickpayexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import net.quickpay.quickpaysdk.PaymentMethodsFragment
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.dummy.DummyContent


class MainActivity : AppCompatActivity(), PaymentMethodsFragment.OnListFragmentInteractionListener {

    companion object {
        private const val TSHIRT_PRICE = 1.0
        private const val FOOTBALL_PRICE = 0.5
    }


    // Fields

    private lateinit var shopItemCompomentTshit: ShopItemComponent
    private lateinit var shopItemCompomentFootball: ShopItemComponent

    private var tShirtCount: Int = 0
    private var footballCount: Int = 0


    // Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        shopItemCompomentFootball = supportFragmentManager.findFragmentById(R.id.shop_soccerball_fragment) as ShopItemComponent
        shopItemCompomentFootball.setImage(R.drawable.soccerball)
        footballCount = shopItemCompomentFootball.counter
        shopItemCompomentFootball.callback = object : ShopItemComponentInterface {
            override fun counterChanged(count: Int) {
                footballCount = count
                updateSummary()
            }
        }

        shopItemCompomentTshit = supportFragmentManager.findFragmentById(R.id.shop_tshirt_fragment) as ShopItemComponent
        shopItemCompomentTshit.setImage(R.drawable.tshirt)
        tShirtCount = shopItemCompomentTshit.counter
        shopItemCompomentTshit.callback = object : ShopItemComponentInterface {
            override fun counterChanged(count: Int) {
                tShirtCount = count
                updateSummary()
            }
        }

        updateSummary()
    }


    // Utils

    private fun updateSummary() {
        var tshirtTotal = TSHIRT_PRICE*tShirtCount
        var footballTotal = FOOTBALL_PRICE*footballCount

        findViewById<TextView>(R.id.shop_summary_tshirt_count)?.text = "T-Shirt x $tShirtCount"
        findViewById<TextView>(R.id.shop_summary_tshirt_price)?.text = "$tshirtTotal DKK"

        findViewById<TextView>(R.id.shop_summary_football_count)?.text = "T-Shirt x $tShirtCount"
        findViewById<TextView>(R.id.shop_summary_football_price)?.text = "$footballTotal DKK"

        findViewById<TextView>(R.id.shop_summary_total_price)?.text = "${tshirtTotal+footballTotal} DKK"
    }


    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Toast.makeText(this, "HEJSA", Toast.LENGTH_SHORT).show()
    }
}
