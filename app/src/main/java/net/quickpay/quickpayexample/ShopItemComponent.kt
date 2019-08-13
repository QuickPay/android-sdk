package net.quickpay.quickpayexample

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
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

    private var listener: ShopItemComponentListener? = null
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
            listener?.counterChanged(this, counter)
        }

        view.findViewById<Button>(R.id.shop_item_component_fragment_removebutton)?.setOnClickListener {
            if (counter > 0) {
                counter--
            }
            listener?.counterChanged(this, counter)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_COUNT, counter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ShopItemComponentListener) {
            listener = context
        }
        else {
            throw RuntimeException("$context must implement ShopItemComponentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    // Utils

    fun setImage(res: Int) {
        view?.findViewById<ImageView>(R.id.shop_item_component_fragment_image)?.setImageResource(res)
    }


    // Communication interfaces

    interface ShopItemComponentListener {

        fun counterChanged(component: ShopItemComponent, count: Int)

    }
}