package com.example.admin_bookmarket

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
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
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Order
import com.example.admin_bookmarket.data.model.OrderBankLoansIdentify
import com.example.admin_bookmarket.data.model.OrderStudentIdentify
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

        binding.backClick.setOnClickListener {
            onBackPressed()
        }
        binding.userEmail.setOnClickListener {
            startActivity(Intent(this, StudentProfileActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        setUpView()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private lateinit var currentOrder: Order

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setUpView() {
        currentOrder = AppUtil.currentOrder
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
            if (currentOrder.status == Constants.OrderStatus.WAITING.toString()) {
                setUpUpdateButton(true, Constants.ButtonState.XACNHAN.toString())
            } else {
                if (currentOrder.status == Constants.OrderStatus.CANCEL.toString()||currentOrder.status == Constants.OrderStatus.COMPLETE.toString()) {
                    status.isEnabled = false
                    statusBox.isEnabled = false
                }
                setUpUpdateButton(false, Constants.ButtonState.CAPNHAT.toString())
            }
            status.doOnTextChanged { text, start, before, count ->
                setStatusOnTextChange(text.toString())
            }
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
                familyKind.text =  "Thuộc đối tượng: ${ (currentOrder as OrderBankLoansIdentify).familyKind}"
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
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setStatusOnTextChange(text: String) {
        val currentOrder: Order = AppUtil.currentOrder
        if (text != Constants.OrderStatus.CONFIRMED.toString()) {
            if (currentOrder.status != text) {
                setUpUpdateButton(true, Constants.ButtonState.CAPNHAT.toString())
            } else {
                setUpUpdateButton(false, Constants.ButtonState.CAPNHAT.toString())
            }
        } else {
            if (currentOrder.status != text) {
                setUpUpdateButton(true, Constants.ButtonState.CAPNHAT.toString())
            } else {
                setUpUpdateButton(true, Constants.ButtonState.XACNHAN.toString())
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setUpUpdateButton(avai: Boolean, content: String) {
        if (avai) {
            binding.updateButton.isEnabled = true;
            if (content == Constants.ButtonState.XACNHAN.toString()) {
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
        if (content == Constants.ButtonState.XACNHAN.toString()) {
            binding.status.setText(Constants.OrderStatus.CONFIRMED.toString(), false)
            currentOrder.status = binding.status.text.toString()
            setUpUpdateButton(false,Constants.ButtonState.CAPNHAT.toString())
        }else{
            if(binding.status.text.toString() != Constants.OrderStatus.CANCEL.toString()){
                currentOrder.status = binding.status.text.toString()
                if(currentOrder.status == Constants.OrderStatus.WAITING.toString()){
                    setUpUpdateButton(true, Constants.ButtonState.XACNHAN.toString())
                }else{
                    if(currentOrder.status == Constants.OrderStatus.COMPLETE.toString()){
                        binding.status.isEnabled = false
                        binding.statusBox.isEnabled = false
                    }
                    setUpUpdateButton(false,Constants.ButtonState.CAPNHAT.toString())
                }
            }
        }
        if(binding.status.text.toString() == Constants.OrderStatus.CANCEL.toString()){
            startActivity(Intent(this,CancelOrderActivity::class.java))
        }else{
            if(viewModel.updateUserStatus(currentOrder.studentEmail, currentOrder.id, currentOrder.status)){
                Toast.makeText(this, "update success", Toast.LENGTH_SHORT).show()
            }
        }

    }
}