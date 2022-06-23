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

            val reason = if (binding.LoginEmail.text.isNullOrBlank()) {
                "Lỗi cú pháp"
            } else {
                binding.LoginEmail.text.toString()
            }
            cancelOrder(AppUtil.currentOrder.id, reason, AppUtil.currentOrder.studentEmail)
            AppUtil.currentOrder.status = Constants.OrderStatus.CANCEL.toString()
            AppUtil.currentOrder.cancelReason = "Phòng CTSV phản hồi: $reason"
            finish()
        }
    }

    private val TAG: String = "userOrder"
    private fun cancelOrder(orderId: String, reason: String, studentEmail: String) {
        FirebaseFirestore.getInstance().collection("accounts").document(studentEmail)
            .collection(TAG).document(orderId).update(
            "status",
            Constants.OrderStatus.CANCEL.toString()
        )
        FirebaseFirestore.getInstance().collection("accounts").document(studentEmail)
            .collection(TAG).document(orderId)
            .update("cancelReason", "Phòng CTSV phản hồi: $reason")
    }
}