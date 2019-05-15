package net.quickpay.quickpayexample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView

class ShopItemComponent : Fragment() {

    companion object {
        private const val STATE_COUNT = "count"
    }

    // Fields
    var callback: ShopItemComponentInterface? = null
    var counter: Int = 0


    // Lifecycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shop_item_component_fragment, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        counter = savedInstanceState?.getInt(STATE_COUNT) ?: 0

        view.findViewById<Button>(R.id.shop_item_component_fragment_addbutton)?.setOnClickListener {
            counter++
            callback?.counterChanged(counter)
        }

        view.findViewById<Button>(R.id.shop_item_component_fragment_removebutton)?.setOnClickListener {
            if (counter > 0) {
                counter--
            }
            callback?.counterChanged(counter)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_COUNT, counter)
    }


    // Utils
    fun setImage(res: Int) {
        view?.findViewById<ImageView>(R.id.shop_item_component_fragment_image)?.setImageResource(res)
    }
}

interface ShopItemComponentInterface {

    fun counterChanged(count: Int)

}