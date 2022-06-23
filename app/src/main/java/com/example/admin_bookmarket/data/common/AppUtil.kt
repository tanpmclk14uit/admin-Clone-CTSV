package com.example.admin_bookmarket.data.common

import com.example.admin_bookmarket.data.model.*
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlin.math.roundToInt

object AppUtil {
    var currentInformation: Information = Information()
    var currentAccount: AppAccount = AppAccount("", "", currentInformation)
    lateinit var currentOrder: Order

    fun toBook(doc: QueryDocumentSnapshot): Book {
        val bookItem = Book()
        bookItem.id = doc.id
        bookItem.Description = doc["Description"].toString()
        bookItem.Image = doc["Image"].toString()
        bookItem.Kind = doc["Kind"].toString()
        bookItem.Name = doc["Name"].toString()
        bookItem.imageId = doc["ImageId"].toString()
        return bookItem
    }
    fun toOrder(doc: QueryDocumentSnapshot): Order{
            if (doc["kind"].toString() == Constants.OrderKind.GXNSV.toString()) {
                val order = OrderStudentIdentify(
                    doc.id,
                    doc["studentEmail"].toString(),
                    doc["status"].toString(),
                    doc["dateTime"].toString(),
                    doc["reason"].toString(),
                )
                if(order.status == Constants.OrderStatus.CANCEL.toString()){
                    order.cancelReason = doc["cancelReason"].toString()
                }
                return order
            } else {
                val order = OrderBankLoansIdentify(
                    doc.id,
                    doc["studentEmail"].toString(),
                    doc["status"].toString(),
                    doc["dateTime"].toString(),
                    doc["tuitionKind"].toString(),
                    doc["familyKind"].toString(),
                )
                if(order.status == Constants.OrderStatus.CANCEL.toString()){
                    order.cancelReason = doc["cancelReason"].toString()
                }
                return order
            }
    }
    fun checkName(str: String): Boolean {
        val regex =
            "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_][^\\d?!@#\\\$%\\^\\&*\\)\\(:';,\"+=._-`~{}|/\\\\]{1,}\$".toRegex()
        return str.matches(regex)
    }

    fun checkAddress(str: String): Boolean {
        val regex =
            "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_][^?!@#\\\$%\\^\\&*\\)(:'\"+=_-`~{}|]{3,}\$".toRegex()
        return str.matches(regex)
    }

    fun checkPhoneNumber(str:String):Boolean{
        val regex = "(84|0[3|5|7|8|9])+([0-9]{8})\\b".toRegex()
        return str.matches(regex)
    }


}