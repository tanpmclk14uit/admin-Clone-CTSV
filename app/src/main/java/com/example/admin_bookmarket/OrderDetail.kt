package com.example.admin_bookmarket

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_bookmarket.ViewModel.OrderViewModel
import com.example.admin_bookmarket.data.adapter.DetailBillItemAdapter
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.model.Order
import com.example.admin_bookmarket.databinding.ActivityOrderDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class OrderDetail : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private val viewModel: OrderViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
        binding.backClick.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private val currentOrder: Order = AppUtil.currentOrder

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setUpView() {
        binding.apply {
            orderId.text = currentOrder.id
            val statusValue = resources.getStringArray(R.array.status)
            val arrayAdapter = ArrayAdapter(
                this.root.context,
                R.layout.status_dropdown_item,
                statusValue
            )
            status.setAdapter(arrayAdapter)
            status.setText(
                currentOrder.status,
                false
            )
            if (currentOrder.status == "WAITING") {
                setUpUpdateButton(true, "CONFIRM")
            } else {
                if (currentOrder.status == "CANCEL") {
                    status.isEnabled = false
                    statusBox.isEnabled = false
                }
                setUpUpdateButton(false, "UPDATE")
            }
            status.doOnTextChanged { text, start, before, count ->
                setStatusOnTextChange(text.toString())
            }
            val formatter = DecimalFormat("#,###")
//
//            userEmail.text = currentOrder.currentUser.email
//
//            dateTime.text = currentOrder.dateTime
//            orderName.text = currentOrder.userDeliverAddress.fullName
//            orderPhoneNumber.text = currentOrder.userDeliverAddress.phoneNumber
//            orderAddress.text =
//                currentOrder.userDeliverAddress.addressLane + ", " + currentOrder.userDeliverAddress.district + ", " + currentOrder.userDeliverAddress.city + "."
//            val billAdapter: DetailBillItemAdapter = DetailBillItemAdapter(currentOrder.listbooks)
//            orderItemBill.adapter = billAdapter
//            orderItemBill.layoutManager = LinearLayoutManager(this.root.context)
//            orderSum.text = formatter.format(currentOrder.totalPrince.toLong()) + "đ"
            if (currentOrder.status == "CANCEL") {
                userReason.visibility = View.VISIBLE
                userReasonLayout.visibility = View.VISIBLE
                userReason.text = currentOrder.cancelReason
                reasonLine.visibility = View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setStatusOnTextChange(text: String) {
        val currentOrder: Order = AppUtil.currentOrder
        if (text != "WAITING") {
            if (currentOrder.status != text) {
                setUpUpdateButton(true, "UPDATE")
            } else {
                setUpUpdateButton(false, "UPDATE")
            }
        } else {
            if (currentOrder.status != text) {
                setUpUpdateButton(true, "UPDATE")
            } else {
                setUpUpdateButton(true, "CONFIRM")
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setUpUpdateButton(avai: Boolean, content: String) {
        if (avai) {
            binding.updateButton.isEnabled = true;
            if (content == "CONFIRM") {
                binding.updateButton.setBackgroundColor(resources.getColor(R.color.green))
            } else {
                binding.updateButton.setBackgroundColor(resources.getColor(R.color.blue_light))
            }
        } else {
            binding.updateButton.isEnabled = false;
            binding.updateButton.setBackgroundColor(resources.getColor(R.color.disable))
        }
        binding.updateButton.setOnClickListener {
            setUpdateButtonClick(content = content)
        }
        binding.updateButton.text = content
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setUpdateButtonClick(content: String) {
        if (content == "CONFIRM") {
            binding.status.setText("CONFIRMED", false)
            currentOrder.status = binding.status.text.toString()
            setUpUpdateButton(false,"UPDATE")
        }else{
            currentOrder.status = binding.status.text.toString()
            if(currentOrder.status == "WAITING"){
                setUpUpdateButton(true, "CONFIRM")
            }else{
                if(currentOrder.status == "CANCEL"){
                    binding.status.isEnabled = false
                    binding.statusBox.isEnabled = false
                }
                setUpUpdateButton(false,"UPDATE")
            }
        }
        if(viewModel.updateUserStatus(currentOrder.studentEmail, currentOrder.id, currentOrder.status)){
            Toast.makeText(this, "update success", Toast.LENGTH_SHORT).show()
        }
    }
}