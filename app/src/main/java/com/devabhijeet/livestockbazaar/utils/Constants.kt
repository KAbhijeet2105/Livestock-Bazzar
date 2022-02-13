package com.devabhijeet.livestockbazaar.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val USERS: String = "users";

    const val ANIMALS: String = "animals"

    const val SOLD_ANIMALS: String = "sold_animals"


    const val BOUGHT_ANIMALS: String = "bought_animals"


    const val LIVESTOCK_BAZAAR_PREFRENCES: String = "LivestockBazaarPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val EXTRA_USER_ID: String ="extra_user_id"


     const val CAMERA_PERMISSION_CODE = 100


    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val FULL_NAME :String = "fullName"
    const val ADDRESS :String = "address"
    const val MOBILE:String = "mobile"
    const val GENDER:String = "gender"
    const val IMAGE:String = "image"
    const val IMAGES:String = "images"
    const val COMPLETE_PROFILE:String = "profileCompleted"
    const val USER_ID: String = "user_id"

    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"

//animals

    const val ANIMAL_IMAGE: String = "Animal_Image"


    const val OWNER_ID: String = "owner_id"
    const val OWNER_NAME: String = "owner_name"
    const val OWNER_MOBILE: String = "owner_mobile"
    const val OWNER_ADDRESS: String = "owner_address"
    const val CATEGORY: String = "category"
    const val BREED: String = "breed"
    const val PRICE: String = "price"
    const val DESCRIPTION: String = "description"
    const val ANIMAL_ID: String = "animal_id"


    // Intent extra constants.
    const val EXTRA_ANIMAL_ID: String = "extra_animal_id"
    const val EXTRA_SOLD_ANIMAL_ID: String = "extra_sold_animal_id"
    const val EXTRA_BOUGHT_ANIMAL_ID: String = "extra_bought_animal_id"
    const val EXTRA_ANIMAL_OWNER_ID: String = "extra_animal_owner_id"



    //bought animals

    const val BUYER_ID: String = "buyer_id"

    const val SELLER_ID: String = "seller_id"


    //sold animals





    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }


    fun getFileExtension(activity: Activity, uri: Uri?): String?{
        /*
        * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         *
        * */
//just a fancy way of saying give me the file extension / filetype

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

/*
    const val AVAILABLE_YES: String = "Yes"
    const val AVAILABLE_No: String = "No"
    const val AVAILABILITY: String = "availability"
    */





}