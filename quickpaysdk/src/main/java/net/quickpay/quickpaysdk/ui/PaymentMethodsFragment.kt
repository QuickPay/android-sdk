package net.quickpay.quickpaysdk.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.quickpay.quickpaysdk.PaymentMethod
import net.quickpay.quickpaysdk.R

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [PaymentMethodsFragment.OnPaymentMethodsListFragmentInteractionListener] interface.
 */
class PaymentMethodsFragment : Fragment() {

    private var listener: OnPaymentMethodsListFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_paymentmethods_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = MyPaymentMethodsRecyclerViewAdapter(PaymentContent.ITEMS, listener, view)

                addItemDecoration(MarginItemDecoration(15))
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnPaymentMethodsListFragmentInteractionListener) {
            listener = context
        }
        else {
            throw RuntimeException("$context must implement OnPaymentMethodsListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnPaymentMethodsListFragmentInteractionListener {
        fun onPaymentMethodSelected(item: PaymentMethod)
    }
}
