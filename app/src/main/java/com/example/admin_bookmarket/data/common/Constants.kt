package com.example.admin_bookmarket.data.common

import com.example.admin_bookmarket.R


object Constants {
    //App
    const val TAG = "FireAppTag"

    //Intents
    const val SPLASH_INTENT = "splashIntent"
    const val AUTH_INTENT = "authIntent"
    const val MAIN_INTENT = "mainIntent"

    //Bundles
    const val MOVIE = "movie"

    //Database Types
    const val PRODUCT_NAME = "productName"
    const val CLOUD_FIRESTORE = "Cloud Firestore"
    const val REALTIME_DATABASE = "Realtime Database"

    //References
    const val USERS_REF = "accounts"
    const val BOOK_REF = "books"

    //Fields
    const val NAME = "name"
    const val EMAIL = "email"
    const val PHOTO_URL = "photoUrl"
    const val CREATED_AT = "createdAt"
    const val RATING = "rating"

    //Bindings
    const val MOVIE_POSTER = "moviePoster"
    const val PRODUCT_LOGO = "productLogo"
    const val DEFAULT_IMG_PLACEHOLDER = R.drawable.bg_radius_white
    enum class ACTIVITY {
       PROFILE, MENU, SEARCH, CART, CATEGORY, FEATURE, CATEGORY_DETAIL;

        override fun toString(): String {
            return name.toLowerCase().capitalize()
        }
    }
    enum class TRANSACTION{
        RECEIVED, COMPLETE,DELIVERING,CANCEL;
        override fun toString(): String {
            return name.toLowerCase().capitalize()
        }
    }

    // activity detail
    const val ITEM = "ITEM_TO_DISPLAY"


    //Tags
    const val VMTAG = "VMTAG"

    enum class CATEGORY{
        CHUNG, HV, VB2, LTCQ, NB, PH;
        override fun toString(): String {
            val result = when(this){
                CHUNG -> "Chung"
                HV -> "Học vụ"
                VB2 -> "Văn bằng 2"
                LTCQ -> "Liên thông chính quy"
                NB -> "Nghỉ/ bù"
                PH -> "Phòng học"
            }
            return result
        }

        fun getShortName(): String{
            val result = when(this){
                CHUNG -> "Chung"
                HV -> "Học vụ"
                VB2 -> "Văn bằng 2"
                LTCQ -> "LTCQ"
                NB -> "Nghỉ/ bù"
                PH -> "Phòng học"
            }
            return result.toUpperCase()
        }
    }
    enum class OrderKind{
        GXNSV, GXNVVNH
    }
    enum class OrderStatus{
        WAITING, CONFIRMED, PRINTED, CANCEL, COMPLETE;

        override fun toString(): String {
            return when(this){
                WAITING -> "Chờ duyệt"
                CONFIRMED -> "Đã duyệt"
                PRINTED -> "Đã in"
                CANCEL -> "Đã hủy"
                COMPLETE -> "Đã nhận"
            }
        }
    }
}