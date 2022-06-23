package com.example.admin_bookmarket.ViewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.admin_bookmarket.data.OrderRepository
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Cart
import com.example.admin_bookmarket.data.model.MyUser
import com.example.admin_bookmarket.data.model.Order
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    @ApplicationContext    private val appContext: Context
) :
    ViewModel() {

    private var _orders = MutableLiveData<MutableList<Order>>()
    private var allOrderValue: MutableList<Order> = ArrayList()
    private var _membersID = MutableLiveData<MutableList<String>>()
    val orders get() = _orders

    fun getAllOrder(): MutableLiveData<MutableList<Order>> {
        orderRepository.getAllUserFromDB().addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
                if(!AppUtils.checkInternet(context = appContext)){
                    Toast.makeText(appContext,"Please checking your internet connection!", Toast.LENGTH_SHORT).show()
                }
            } else {
                for (doc in value!!) {
                    getAllOrderOfId(doc.id)
                }

            }
        }
        return orders
    }

    private fun getAllOrderOfId(userId: String) {
        orderRepository.getAllOrderFromDB(userId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(Constants.VMTAG, "Listen failed.", error)
                if(!AppUtils.checkInternet(context = appContext)){
                    Toast.makeText(appContext,"Please checking your internet connection!", Toast.LENGTH_SHORT).show()
                }
            } else {
                for (doc in value!!) {
                        val order = AppUtil.toOrder(doc)
                        if (isExitsInAllOrder(order) == null) {
                            allOrderValue.add(order)
                        } else {
                            allOrderValue[allOrderValue.indexOf(isExitsInAllOrder(order))] = order
                        }
                        orders.value = allOrderValue
                }
            }
        }
    }

    private fun isExitsInAllOrder(newOrder: Order): Order? {
        for (order in allOrderValue) {
            if (order.id == newOrder.id) {
                return order
            }
        }
        return null
    }

    fun updateUserStatus(userId: String, docId: String, status: String): Boolean {
        return if (orderRepository.updateOrderStatus(userId, docId, status)) {
            true
        }else{
            if(!AppUtils.checkInternet(context = appContext)){
                Toast.makeText(appContext,"Please checking your internet connection!", Toast.LENGTH_SHORT).show()
            }
            false
        }

    }
}