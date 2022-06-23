package com.example.admin_bookmarket.data.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_bookmarket.R
import com.example.admin_bookmarket.RecyclerViewClickListener
import com.example.admin_bookmarket.ViewModel.OrderViewModel
import com.example.admin_bookmarket.data.OrderRepository
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Order
import com.example.admin_bookmarket.data.model.OrderBankLoansIdentify
import com.example.admin_bookmarket.data.model.OrderStudentIdentify
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.inject.Inject


class OrderAdapter(
    var listOder: MutableList<Order>,
    var context: Context,
    private val itemListener: RecyclerViewClickListener,
    private val viewModel: OrderViewModel
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val status: TextView = view.findViewById(R.id.status)
        val dateTime: TextView = view.findViewById(R.id.dateTime)
        val orderKind: TextView = view.findViewById(R.id.orderKind)
        val reason: TextView = view.findViewById(R.id.reason)
        val tuitionKind: TextView = view.findViewById(R.id.tuitionKind)
        val familyKind: TextView = view.findViewById(R.id.familyKind)
        val cancelReasonLayout: RelativeLayout = view.findViewById(R.id.cancelReasonLayout)
        val cancelReason: TextView = view.findViewById(R.id.cancelReason)
        val confirm: Button = view.findViewById(R.id.update)
        val layout: CardView = view.findViewById(R.id.layout)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)
        return ViewHolder(view)
    }

    fun addOrder(change: MutableList<Order>) {
        if (this.listOder.isNotEmpty()) {
            this.listOder.clear()
        }
        this.listOder.addAll(change)
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentOrder: Order = listOder[position]
        with(holder.itemView) {
            setOnClickListener {
                AppUtil.currentOrder = currentOrder
                itemListener.recyclerViewListClicked(this, currentOrder.id)
            }
        }
        holder.apply {

            if (currentOrder.status != Constants.OrderStatus.WAITING.toString()) {
                confirm.isEnabled = false
                confirm.setBackgroundColor(context.resources.getColor(R.color.disable))
                if (currentOrder.status == Constants.OrderStatus.CANCEL.toString()) {
                    layout.alpha = 0.7F
                }else{
                    layout.alpha = 1F
                }
            } else {
                confirm.isEnabled = true
                confirm.setBackgroundColor(context.resources.getColor(R.color.green))
                layout.alpha = 1F
            }

            status.text = currentOrder.status
            dateTime.text = currentOrder.dateTime
            orderKind.text = currentOrder.kind
            if (currentOrder.kind == Constants.OrderKind.GXNSV.toString()) {
                // update view
                reason.visibility = View.VISIBLE
                tuitionKind.visibility = View.GONE
                familyKind.visibility = View.GONE
                // set data
                reason.text = "Lý do xác nhận: ${(currentOrder as OrderStudentIdentify).reason}"

            } else {
                // update view
                reason.visibility = View.GONE
                tuitionKind.visibility = View.VISIBLE
                familyKind.visibility = View.VISIBLE
                // set data
                tuitionKind.text = "Thuộc diện: ${ (currentOrder as OrderBankLoansIdentify).tuitionKind}"
                familyKind.text =  "Thuộc đối tượng: ${ (currentOrder).familyKind}"
            }
            if (currentOrder.status == Constants.OrderStatus.CANCEL.toString()) {
                // update view
                cancelReasonLayout.visibility = View.VISIBLE
                status.setTextColor(Color.GRAY)
                // set data
                cancelReason.text = currentOrder.cancelReason
            } else {
                // update view
                cancelReasonLayout.visibility = View.GONE
            }
            confirm.setOnClickListener {
                if(viewModel.updateUserStatus(currentOrder.studentEmail,currentOrder.id,Constants.OrderStatus.CONFIRMED.toString())){
                    currentOrder.status = Constants.OrderStatus.CONFIRMED.toString()
                    AppUtil.currentOrder = currentOrder
                    status.text = currentOrder.status
                    confirm.isEnabled = false
                    confirm.setBackgroundColor(context.resources.getColor(R.color.disable))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listOder.size
    }

}