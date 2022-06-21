package com.example.admin_bookmarket.data

import android.util.Log
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Book
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.math.roundToInt

public class FullBookList private constructor(var lstFullBook: MutableList<Book> = mutableListOf()) {

    init {
        getDataBySnapshot()
    }

    private fun getDataBySnapshot() {
        var ref = FirebaseFirestore.getInstance().collection("books")
        ref.addSnapshotListener (object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }
                val bookList: MutableList<Book> = ArrayList()
                for (doc in value!!) {
                        val bookItem = AppUtil.toBook(doc)
                        bookList.add(bookItem)
                }
                lstFullBook = bookList
            }
        })
    }
    private object Holder {
        val INSTANCE = FullBookList()
    }

    companion object {

        fun getInstance(): FullBookList {
            return Holder.INSTANCE
        }
        fun destroyOld()
        {
            Holder.INSTANCE.getDataBySnapshot()
        }

    }
}