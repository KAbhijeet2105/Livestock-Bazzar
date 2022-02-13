package com.devabhijeet.livestockbazaar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val address:String = "",
    val image: String = "",
    val mobile: Long = 0,
    val gender: String = "",
    val profileCompleted: Int = 0 ): Parcelable