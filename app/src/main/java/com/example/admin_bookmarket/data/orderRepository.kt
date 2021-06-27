package com.example.admin_bookmarket.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.admin_bookmarket.data.common.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Named

class OrderRepository @Inject constructor(
    @Named(Constants.USERS_REF) private val userCollRef: CollectionReference
) {
    private val TAG: String = "userOrder"
    fun getAllUserFromDB(): Query{
        return userCollRef
    }
    fun getAllOrderFromDB(userId: String): Query{
        return userCollRef.document(userId).collection(TAG).orderBy(
            "dateTime",
            Query.Direction.DESCENDING
        )
    }
    fun getAllBillingIemFromDB(userId: String, docId: String): Query{
        return userCollRef.document(userId).collection(TAG).document(docId).collection("books").orderBy(
            "price",
            Query.Direction.ASCENDING
        )
    }

}