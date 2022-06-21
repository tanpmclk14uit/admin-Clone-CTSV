package com.example.admin_bookmarket.data.model

import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Named

class UserRepository @Inject constructor(
    @Named(Constants.USERS_REF) private val userCollRef: CollectionReference
) {
    var information: Information = Information()

    fun loadData() {
       information = AppUtil.currentInformation

    }
    fun updateUserData(information: Information){
       AppUtil.currentAccount.information = information
        AppUtil.currentInformation = information
        FirebaseFirestore.getInstance().collection("salerAccount").document(AppUtil.currentAccount.email).set(AppUtil.currentAccount)
    }
}

