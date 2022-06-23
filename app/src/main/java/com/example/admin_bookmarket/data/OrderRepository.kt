package com.example.admin_bookmarket.data

import com.example.admin_bookmarket.data.common.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query

import javax.inject.Inject
import javax.inject.Named


class OrderRepository @Inject constructor(
    @Named(Constants.USERS_REF) private val userCollRef: CollectionReference
) {
    private val TAG: String = "userOrder"
    fun getAllUserFromDB(): Query {
        return userCollRef
    }



    fun getAllOrderFromDB(userId: String): Query {
        return userCollRef.document(userId).collection(TAG).orderBy(
            "dateTime",
            Query.Direction.DESCENDING
        )
    }

    fun updateOrderStatus(userId: String, docId: String, status: String): Boolean{
        return userCollRef.document(userId).collection(TAG).document(docId).update("status", status).isSuccessful
    }

}