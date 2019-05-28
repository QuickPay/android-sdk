package net.quickpay.quickpayexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import net.quickpay.quickpaysdk.PaymentMethodsFragment
import net.quickpay.quickpaysdk.QuickPay
import net.quickpay.quickpaysdk.dummy.DummyContent


class MainActivity : AppCompatActivity(), PaymentMethodsFragment.OnListFragmentInteractionListener, ShopItemComponent.ShopItemComponentListener {

    companion object {
        private const val TSHIRT_PRICE = 1.0
        private const val FOOTBALL_PRICE = 0.5
    }


    // Fields

    private var shopItemCompomentTshit: ShopItemComponent? = null
    private var shopItemCompomentFootball: ShopItemComponent? = null


    // Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init the QuickPay API
        QuickPay.init("f1a4b80189c73862655552d06f9419dd7574c65de916fef88cf9854f6907f1b4", this)
        QuickPay.instance.checkClearhaus()
        QuickPay.instance.checkMobilePay()

        shopItemCompomentFootball = supportFragmentManager.findFragmentById(R.id.shop_soccerball_fragment) as ShopItemComponent
        shopItemCompomentFootball?.setImage(R.drawable.soccerball)

        shopItemCompomentTshit = supportFragmentManager.findFragmentById(R.id.shop_tshirt_fragment) as ShopItemComponent
        shopItemCompomentTshit?.setImage(R.drawable.tshirt)

        updateSummary()
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
}
