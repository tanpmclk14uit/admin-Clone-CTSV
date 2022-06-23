package com.example.admin_bookmarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.databinding.ActivityCancelOrderBinding
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CancelOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCancelOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
            finish()
        }
        binding.submit.setOnClickListener {
            if(binding.LoginEmail.text.isNullOrBlank()){
                cancelOrder(AppUtil.currentOrder.id, "Lỗi cú pháp", AppUtil.currentOrder.studentEmail)
            }else{
                cancelOrder(AppUtil.currentOrder.id, binding.LoginEmail.text.toString(), AppUtil.currentOrder.studentEmail)
            }

            finish()
        }
    }

    private val TAG: String = "userOrder"
    private fun cancelOrder(orderId: String, reason: String, studentEmail: String){
        FirebaseFirestore.getInstance().collection("accounts").document(studentEmail).collection(TAG).document(orderId).update("status",
            Constants.OrderStatus.CANCEL.toString())
        FirebaseFirestore.getInstance().collection("accounts").document(studentEmail).collection(TAG).document(orderId).update("cancelReason","Phòng CTXV phản hồi: $reason")
    }
}