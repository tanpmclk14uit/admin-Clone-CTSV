package com.example.admin_bookmarket.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Book(
    var id: String? = "",
    var Image: String? = "",
    var Name: String? = "",
    var Kind: String? = "",
    var Description: String? = "",
    var imageId: String? = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(Image)
        parcel.writeString(Name)
        parcel.writeString(Kind)
        parcel.writeString(Description)
        parcel.writeString(imageId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}
