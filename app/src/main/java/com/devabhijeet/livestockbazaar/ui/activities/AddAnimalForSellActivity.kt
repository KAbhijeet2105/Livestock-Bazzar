package com.devabhijeet.livestockbazaar.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.Animal
import com.devabhijeet.livestockbazaar.models.User
import com.devabhijeet.livestockbazaar.ui.adapters.AddAnimalImageAdapter
import com.devabhijeet.livestockbazaar.utils.Constants
import com.devabhijeet.livestockbazaar.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_animal_for_sell.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.IOException

class AddAnimalForSellActivity : BaseActivity(), View.OnClickListener {

    private var mSelectedImageFileUri: Uri? = null
    // A global variable for uploaded product image URL.
    private var mAnimalImageURL: String = ""

    private lateinit var mUserDetails: User

    private var imgList = mutableListOf<Uri>()

    private  var mAnimalMultiImageUrls : ArrayList<String> = arrayListOf<String>()

    private val getImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { result ->
            imgList.addAll(result)
            if (imgList.size > 4) {
                imgList = imgList.subList(0, 4)
                showErrorSnackBar("Maximum 4 images are allowed!",true)
            }
            if(imgList.size == 0)
            {
                showErrorSnackBar("Select at list one image",true)

            }
            val adapter = AddAnimalImageAdapter(this, imgList)
            rv_add_animal_images.adapter = adapter

            mAnimalImageURL = imgList.get(0).toString()

            GlideLoader(this).loadAnimalPicture(mAnimalImageURL,iv_animal_image_add_animals_for_sell)

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_animal_for_sell)

        val animalCategories = resources.getStringArray(R.array.Animal_Categories)


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, animalCategories)
        spinr_category_add_animals_for_sell.adapter = adapter


        iv_add_update_animal_add_animals_for_sell.setOnClickListener(this)
        btn_submit_add_animals_for_sell.setOnClickListener(this)
       // setupFlow()

        add_animal_images_btn.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        if (v != null)
        {
            when (v.id) {

                // The permission code is similar to the user profile image selection.
                R.id.iv_add_update_animal_add_animals_for_sell -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        Constants.showImageChooser(this@AddAnimalForSellActivity)
                    }
                    else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit_add_animals_for_sell -> {
                    //  Toast.makeText(this,"submit btn clicked outer without validation ",Toast.LENGTH_SHORT).show();

                    if (validateAnimalDetails()) {

                        // Toast.makeText(this,"submit btn clicked ",Toast.LENGTH_SHORT).show();

                        if (mUserDetails.profileCompleted == 0) {
                            showErrorSnackBar(
                                "Please complete your profile first! Goto dashboard and then settings to access your profile",
                                true
                            )
                        } else {
                            Toast.makeText(this,"at stage 0 uploading pics",Toast.LENGTH_SHORT)
                            //for (i in 1..imgList.size )
                                uploadAnimalImage()
                        }
                    }
                }


                R.id.add_animal_images_btn -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                       // Constants.showImageChooser(this@AddAnimalForSellActivity)
                        getImages.launch("image/*")
                    }
                    else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

            }

        }
    }





    //when activity starts first get the user details from firestore which will required later for animal details uploading

    override fun onResume() {
        super.onResume()

        setupFlow()
    }


    private fun setupFlow()
    {
        //step 1 : collect the user details
        getUserDetails()



    }




//get user details first

    /**
     * A function to get the user details from firestore.
     */
    private fun getUserDetails() {

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this@AddAnimalForSellActivity)
    }
    // END

    // START
    /**
     * A function to receive the user details and populate it in the UI.
     */
    fun userDetailsSuccess(user: User) {

        mUserDetails = user

        // TODO Step 9: Set the user details to UI.
        // START
        // Hide the progress dialog
        hideProgressDialog()

    }



    /**
     * A function to upload the selected product image to firebase cloud storage.
     */
    private fun uploadAnimalImage() {

        showProgressDialog(resources.getString(R.string.please_wait))

        mSelectedImageFileUri = imgList[0]

        FirestoreClass().uploadImageToCloudStorage(
            this@AddAnimalForSellActivity,
            mSelectedImageFileUri,
            Constants.ANIMAL_IMAGE
        )
    }

    /**
     * A function to get the successful result of product image upload.
     */
    fun imageUploadSuccess(imageURL: String) {

        // Initialize the global image url variable.
        mAnimalImageURL = imageURL


        imgList.removeFirst()

        mAnimalMultiImageUrls.add(imageURL)

        if (imgList.isEmpty()) //if all images uploaded and urls set then
             uploadAnimalDetails()
        else
            uploadAnimalImage()
    }




    private fun uploadAnimalDetails() {

        //make sure  Get the user details before you submit animal.
        mAnimalImageURL = mAnimalMultiImageUrls[0]

        // Here we get the text from editText and trim the space
        val animal = Animal(
            FirestoreClass().getCurrentUserID(),
            mUserDetails.fullName,
            mUserDetails.mobile,
            mUserDetails.address,
            spinr_category_add_animals_for_sell.selectedItem.toString().trim() ,
            et_animal_breed_animals_for_sell.text.toString().trim { it <= ' ' },
            et_animal_price_add_animals_for_sell.text.toString().trim { it <= ' ' },
            et_animal_description_add_animals_for_sell.text.toString().trim { it <= ' ' },
            mAnimalImageURL,
            mAnimalMultiImageUrls
        )

        FirestoreClass().uploadAnimalDetails(this@AddAnimalForSellActivity , animal)
    }

    /**
     * A function to return the successful result of Product upload.
     */
    fun animalUploadSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@AddAnimalForSellActivity,
            resources.getString(R.string.animal_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

























    /**
     * A function to validate the product details.
     */
    private fun validateAnimalDetails(): Boolean {
        return when {

           // mSelectedImageFileUri == null -> {
            //    showErrorSnackBar(resources.getString(R.string.err_msg_select_animal_image), true)
            //    false
          //  }

            (imgList.isEmpty()) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_animal_image), true)
                false
            }

            TextUtils.isEmpty(et_animal_breed_animals_for_sell.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_animal_breed), true)
                false
            }

            TextUtils.isEmpty(et_animal_price_add_animals_for_sell.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_animal_price), true)
                false
            }

            TextUtils.isEmpty(et_animal_description_add_animals_for_sell.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_animal_description),
                    true
                )
                false
            }

              (spinr_category_add_animals_for_sell.selectedItemPosition  == 0)  -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_select_animal_category),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }




//permissions
    /**
     * This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@AddAnimalForSellActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //image edit or choose

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.PICK_IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {

            // Replace the add icon with edit icon once the image is selected.
            iv_add_update_animal_add_animals_for_sell.setImageDrawable(
                ContextCompat.getDrawable(
                    this@AddAnimalForSellActivity,
                    R.drawable.ic_vector_edit
                )
            )

            // The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data!!

            try {
                //TODO Load the product image in the ImageView.
                GlideLoader(this@AddAnimalForSellActivity).loadAnimalPicture(mSelectedImageFileUri!!, iv_animal_image_add_animals_for_sell)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("Request Cancelled","Image selection cancelled")
        }

    }


}