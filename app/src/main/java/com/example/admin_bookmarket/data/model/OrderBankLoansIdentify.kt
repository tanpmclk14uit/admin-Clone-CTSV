package com.example.admin_bookmarket.data.model

import com.example.admin_bookmarket.data.common.Constants

class OrderBankLoansIdentify(
    id: String = "",
    studentEmail: String,
    status: String,
    dateTime: String,
    var tuitionKind: String,
    var familyKind: String
) : Order(id, studentEmail, status, dateTime) {

    init {
        this.kind = Constants.OrderKind.GXNVVNH.toString()
    }

    override fun toString(): String {
        return "$kind, $id,$studentEmail,$status,$dateTime,$tuitionKind,$familyKind"
    }
}