package com.example.admin_bookmarket.data.model

import com.google.firebase.firestore.auth.User

abstract class Order(
    var id: String ="",
    var studentEmail: String = "",
    var status: String ="",
    var dateTime: String ="",
    var kind: String ="",
    var cancelReason: String = "",
)