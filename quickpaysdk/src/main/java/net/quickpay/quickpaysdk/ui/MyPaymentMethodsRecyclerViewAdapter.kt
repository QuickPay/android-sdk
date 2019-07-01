package net.quickpay.quickpaysdk.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import kotlinx.android.synthetic.main.fragment_paymentmethods.view.*
import net.quickpay.quickpaysdk.R

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnPaymentMethodsListFragmentInteractionListener].
 * TODO: Replace the implementation with code for yourdata type.
 */
class MyPaymentMethodsRecyclerViewAdapter(
    private val mValues: List<PaymentContent.PaymentItem>,
    private val mListener: PaymentMethodsFragment.OnPaymentMethodsListFragmentInteractionListener?
) : RecyclerView.Adapter<MyPaymentMethodsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as PaymentContent.PaymentItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onPaymentMethodSelected(item.method)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_paymentmethods, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.id

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
    }
}
