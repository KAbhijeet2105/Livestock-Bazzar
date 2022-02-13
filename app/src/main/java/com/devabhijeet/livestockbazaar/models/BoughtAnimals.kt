package com.devabhijeet.livestockbazaar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BoughtAnimals (

    val user_id: String = "",
    val seller_id:String ="",
    val seller_name: String = "",
    val seller_mobile: Long = 0,
    val seller_address:String = "",

    val buy_amount:String ="",
    val buy_date: Long = 0L,
    val category: String = "",
    val breed: String = "",
    val description: String = "",
    val image: String = "",
    var images: List<String> = ArrayList(),
    var animal_id: String = "",
    var id: String = "",


    ) : Parcelable