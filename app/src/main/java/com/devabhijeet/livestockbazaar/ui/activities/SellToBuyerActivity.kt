package com.devabhijeet.livestockbazaar.ui.activities

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.Animal
import com.devabhijeet.livestockbazaar.utils.Constants
import com.devabhijeet.livestockbazaar.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_animal_buy_and_details.*
import kotlinx.android.synthetic.main.activity_sell_to_buyer.*
import com.google.zxing.integration.android.IntentIntegrator
import android.widget.Toast

import com.google.zxing.integration.android.IntentResult

import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.PagerSnapHelper
import com.devabhijeet.livestockbazaar.models.BoughtAnimals
import com.devabhijeet.livestockbazaar.models.SoldAnimals
import com.devabhijeet.livestockbazaar.models.User
import com.devabhijeet.livestockbazaar.ui.adapters.AnimalImageAdapter
import com.devabhijeet.livestockbazaar.utils.DotsIndicatorDecoration
import kotlinx.android.synthetic.main.activity_add_animal_for_sell.*


class SellToBuyerActivity : BaseActivity() {

    private var mAnimalId: String = ""

    private lateinit var mAnimalDetails: Animal

    private lateinit var mBuyer_id:String

    private lateinit var mBuyerDetails:User

    private lateinit var mOwnerDetails:User

    var animalImages: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_to_buyer)

        if (intent.hasExtra(Constants.EXTRA_ANIMAL_ID)) {
            mAnimalId = intent.getStringExtra(Constants.EXTRA_ANIMAL_ID)!!

        }

        if(ContextCompat.checkSelfPermission(this@SellToBuyerActivity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            checkPermission(Manifest.permission.CAMERA,
                Constants.CAMERA_PERMISSION_CODE)
        }


        getAnimalDetails()  //get the animal details
        getOwnerDetails()  //get owner details



        btn_scan_qr_sell_to_buyer.setOnClickListener {

            if(ContextCompat.checkSelfPermission(this@SellToBuyerActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
            {
                // Permission is not granted
                checkPermission(Manifest.permission.CAMERA,
                    Constants.CAMERA_PERMISSION_CODE)
            }



            // we need to create the object
            // of IntentIntegrator class
            // which is the class of QR library
            // we need to create the object
            // of IntentIntegrator class
            // which is the class of QR library
            val intentIntegrator = IntentIntegrator(this)
            intentIntegrator.setPrompt("Scan buyers QR Code")
            intentIntegrator.setOrientationLocked(true)
            intentIntegrator.initiateScan()
        }

    }


    /**
     * A function to call the firestore class function that will get the product details from cloud firestore based on the product id.
     */
    private fun getAnimalDetails() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreClass().getAnimalDetails(this@SellToBuyerActivity, mAnimalId)
    }

    /**
     * A function to notify the success result of the product details based on the product id.
     *
     * @param animal A model class with product details.
     */
    fun animalDetailsSuccess(animal: Animal) {

        mAnimalDetails = animal;

        // Hide Progress dialog.
        hideProgressDialog()

        animalImages = animal.images

        // Populate the product details in the UI.
       /* GlideLoader(this@SellToBuyerActivity).loadAnimalPicture(
            animal.image,
           iv_animal_image_sell_to_buyer
        )*/

        setImages()

        et_category_sell_to_buyer.setText(animal.category)
        et_animal_breed_sell_to_buyer.setText(animal.breed)
        et_animal_price_sell_to_buyer.setText(animal.price)
        et_animal_description_sell_to_buyer.setText(animal.description)



    }

    private fun setImages()
    {
        rv_animal_images_sell_to_buyer.isNestedScrollingEnabled = false
        val adapter = AnimalImageAdapter(
            this,
            animalImages
        )
        rv_animal_images_sell_to_buyer.adapter = adapter
        val rad = resources.getDimension(R.dimen.radius)
        val dotsHeight = resources.getDimensionPixelSize(R.dimen.dots_height)
        val inactiveColor = ContextCompat.getColor(this, R.color.colorLightGrey)
        val activeColor = ContextCompat.getColor(this, R.color.primary)
        val itemDecoration =
            DotsIndicatorDecoration(rad, rad * 4, dotsHeight, inactiveColor, activeColor)
        rv_animal_images_sell_to_buyer.addItemDecoration(itemDecoration)
        PagerSnapHelper().attachToRecyclerView(rv_animal_images_sell_to_buyer)

    }



    //actual animal selling part
    //todo 1.add this animal in sold animal list of owner
    //todo 2.add this animal in bought animal list of buyer
    //todo 3.delete this animal from animal list


    fun sellAnimal()
    {
        //getOwnerDetails()  //get owner details in onCreate
        getBuyerDetails()  //get buyer details

        //make chain of transaction
       // uploadSoldAnimalDetails()
       // uploadBoughtAnimalDetails()
        //deleteAnimalAfeterSell(mAnimalId)

    }




    //add animal in sold animal list of owner


    private fun uploadSoldAnimalDetails() {

        //make sure  Get the user details before you submit animal.

      //  showProgressDialog(resources.getString(R.string.please_wait))

        // Here we get the text from editText and trim the space
        val soldAnimal = SoldAnimals(
            FirestoreClass().getCurrentUserID(),
            mBuyer_id,
            mBuyerDetails.fullName,
            mBuyerDetails.mobile,
            mBuyerDetails.address,
            mAnimalDetails.price,
           System.currentTimeMillis(),
            mAnimalDetails.category,
            mAnimalDetails.breed,
            mAnimalDetails.description,
            mAnimalDetails.image,
            mAnimalDetails.images,
            mAnimalId

        )

        FirestoreClass().uploadSoldAnimalDetails(this@SellToBuyerActivity , soldAnimal)
    }

    /**
     * A function to return the successful result of Product upload.
     */
    fun animalSoldUploadSuccess() {

        // Hide the progress dialog
      //  hideProgressDialog()
        Toast.makeText(baseContext, "transaction in progress  "  , Toast.LENGTH_LONG).show()

        uploadBoughtAnimalDetails()

    }



    //add animal in sold animal list of owner


    private fun uploadBoughtAnimalDetails() {

        //make sure  Get the user details before you submit animal.

       // showProgressDialog(resources.getString(R.string.please_wait))

        // Here we get the text from editText and trim the space
        val boughtAnimal = BoughtAnimals(
            mBuyer_id,
            FirestoreClass().getCurrentUserID(),

            mOwnerDetails.fullName,
            mOwnerDetails.mobile,
            mOwnerDetails.address,

            mAnimalDetails.price,
            System.currentTimeMillis(),
            mAnimalDetails.category,
            mAnimalDetails.breed,
            mAnimalDetails.description,
            mAnimalDetails.image,
            mAnimalDetails.images,
            mAnimalId

        )

        FirestoreClass().uploadBoughtAnimalDetails(this@SellToBuyerActivity , boughtAnimal)
    }

    /**
     * A function to return the successful result of Product upload.
     */
    fun animalBoughtUploadSuccess() {

        // Hide the progress dialog
       // hideProgressDialog()
        Toast.makeText(baseContext, "Please wait transaction is in process "  , Toast.LENGTH_LONG).show()

            deleteAnimalAfeterSell(mAnimalId)
    }




    //delete animal from animal list so it wont be available for sell


    fun deleteAnimalAfeterSell(animalid:String)
    {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().deleteAnimalAfterSelling(this@SellToBuyerActivity,animalid)
    }


    fun animalDeleteSuccess()
    {
        hideProgressDialog()

        Toast.makeText(this@SellToBuyerActivity,"Transaction successful!",Toast.LENGTH_SHORT).show()
        finish()
    }





//getting owner details
    fun getOwnerDetails()
    {
       // showProgressDialog(resources.getString(R.string.please_wait))

        Toast.makeText(baseContext, "getting owner id ", Toast.LENGTH_SHORT).show()

        FirestoreClass().getUserDetails(this@SellToBuyerActivity)


    }

     fun ownerDetailsSuccess(owner:User)
    {
        Toast.makeText(baseContext, "owner id received $owner"  , Toast.LENGTH_SHORT).show()

       // hideProgressDialog()
        mOwnerDetails = owner


    }

    //getting buyer details

    fun getBuyerDetails()
    {
       // showProgressDialog(resources.getString(R.string.please_wait))
        Toast.makeText(baseContext, "getting buyer id ", Toast.LENGTH_SHORT).show()

        FirestoreClass().getUserDetailsBuyer(this@SellToBuyerActivity,mBuyer_id.toString().trim())

    }

    fun buyerDetailsSuccess(buyer:User)
    {

        //hideProgressDialog()
        Toast.makeText(baseContext, "Please wait transaction in process"  , Toast.LENGTH_SHORT).show()

        mBuyerDetails = buyer

        uploadSoldAnimalDetails()

    }





    private fun showAlertDialogToDeleteAnimal() {

        val builder = AlertDialog.Builder(this@SellToBuyerActivity)
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.sell_animal_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.sell_animal_dialog_message))
        builder.setIcon(R.drawable.ic_vector_payment)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->


            // Show the progress dialog.
            //showProgressDialog(resources.getString(R.string.please_wait))


                sellAnimal()
            // END

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }



//getting back buyer's id

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(baseContext, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                //messageText.setText(intentResult.contents)
               // messageFormat.setText(intentResult.formatName)

                Toast.makeText(baseContext, "Buyer id :  ${intentResult.contents}", Toast.LENGTH_SHORT).show()

                mBuyer_id = intentResult.contents.toString().trim()


                showAlertDialogToDeleteAnimal()


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    // Function to check and request permission.
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@SellToBuyerActivity, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@SellToBuyerActivity, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this@SellToBuyerActivity, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }


    // This function is called when the user accepts or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when the user is prompt for permission.
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@SellToBuyerActivity, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@SellToBuyerActivity, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    }
}