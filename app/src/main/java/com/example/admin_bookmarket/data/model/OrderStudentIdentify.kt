package com.example.admin_bookmarket.data.model

import com.example.admin_bookmarket.data.common.Constants

class OrderStudentIdentify(
    id: String = "",
    studentEmail: String,
    status: String,
    dateTime: String,
    var reason: String
) : Order(id, studentEmail, status, dateTime) {

    init {
        this.kind = Constants.OrderKind.GXNSV.toString()
    }
    override fun toString(): String {
        return "$kind,$id,$studentEmail,$status,$dateTime,$reason"
    }
}