package com.devabhijeet.livestockbazaar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoldAnimals (

    val owner_id: String = "",
    val buyer_id:String ="",
    val buyer_name: String = "",
    val buyer_mobile: Long = 0,
    val buyer_address:String = "",

    val sell_amount:String ="",
    val sell_date: Long = 0L,
    val category: String = "",
    val breed: String = "",
    val description: String = "",

    val image: String = "",
    var images: List<String> = ArrayList(),
    var animal_id: String = "",
    var id: String = "",

) : Parcelable