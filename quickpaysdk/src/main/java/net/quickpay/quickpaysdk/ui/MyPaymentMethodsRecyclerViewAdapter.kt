package net.quickpay.quickpaysdk.ui

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import kotlinx.android.synthetic.main.fragment_paymentmethods.view.*
import net.quickpay.quickpaysdk.PaymentMethod
import net.quickpay.quickpaysdk.R

class MyPaymentMethodsRecyclerViewAdapter(private val mValues: List<PaymentContent.PaymentItem>, private val mListener: PaymentMethodsFragment.OnPaymentMethodsListFragmentInteractionListener?) : RecyclerView.Adapter<MyPaymentMethodsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            v.setBackgroundColor(Color.parseColor("#ff0000"))

            val item = v.tag as PaymentContent.PaymentItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onPaymentMethodSelected(item.method)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (mValues[position].method == PaymentMethod.PAYMENTCARD) {
            0
        } else {
            1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 0) {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_paymentmethod_cards, parent, false))
        }
        else {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_paymentmethods, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTitleView.text = item.toString()

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.item_title
        val mLayout: LinearLayout = mView.item_layout
    }
}
