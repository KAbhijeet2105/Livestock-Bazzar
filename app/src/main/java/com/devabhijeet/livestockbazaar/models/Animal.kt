package com.devabhijeet.livestockbazaar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data model class for Animals with required fields.
 */
@Parcelize
data class Animal(
    val owner_id: String = "",
    val owner_name: String = "",
    val owner_mobile: Long = 0,
    val owner_address:String = "",
    val category: String = "",
    val breed: String = "",
    val price: String = "",
    val description: String = "",
    val image: String = "",
    var images: List<String> = ArrayList(),
    var animal_id: String = "",

) : Parcelable
