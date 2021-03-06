package com.example.admin_bookmarket.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Book
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private var _lstCurrentBook = MutableLiveData<MutableList<Book>>()
    val lstCurrentBook get() = _lstCurrentBook

    init {
        loadBook()
    }

    private fun loadBook()
    {
        FirebaseFirestore.getInstance().collection("books").addSnapshotListener(
            object: EventListener<QuerySnapshot>{
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
                    lstCurrentBook.value = bookList
                }

            }
        )
    }
}