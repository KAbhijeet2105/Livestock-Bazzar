package com.devabhijeet.livestockbazaar.ui.activities

import android.os.Bundle
import android.view.View
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.Animal
import com.devabhijeet.livestockbazaar.utils.Constants
import com.devabhijeet.livestockbazaar.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_animal_buy_and_details.*
import android.content.pm.PackageManager
import android.widget.Toast

import android.content.Intent

import android.telephony.PhoneNumberUtils

import android.content.ComponentName
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.PagerSnapHelper
import com.devabhijeet.livestockbazaar.ui.adapters.AnimalImageAdapter
import com.devabhijeet.livestockbazaar.utils.DotsIndicatorDecoration


class AnimalBuyAndDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mAnimalId: String = ""

    private lateinit var mAnimalDetails:Animal

    private var mAnimalOwnerId: String = ""

    var animalImages: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_buy_and_details)

        if (intent.hasExtra(Constants.EXTRA_ANIMAL_ID)) {
            mAnimalId = intent.getStringExtra(Constants.EXTRA_ANIMAL_ID)!!


        }

        //var productOwnerId : String = ""

        if (intent.hasExtra(Constants.EXTRA_ANIMAL_OWNER_ID)) {
            mAnimalOwnerId = intent.getStringExtra(Constants.EXTRA_ANIMAL_OWNER_ID)!!

        }



        if (FirestoreClass().getCurrentUserID() == mAnimalOwnerId)
        {
            ll_owner_details_buy_and_details.visibility = View.GONE

        }
        else
        {
            ll_owner_details_buy_and_details.visibility = View.VISIBLE
        }



        getAnimalDetails()
        btn_call_owner_buy_and_details.setOnClickListener(this)
        btn_chat_owner_buy_and_details.setOnClickListener(this)
        btn_ok_buy_and_details.setOnClickListener(this)

    }




    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                R.id.btn_call_owner_buy_and_details -> {
                   // addToCart()

                    var callNo = mAnimalDetails.owner_mobile.toString()

                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$callNo")
                    //intent.data = Uri.parse(callNo)
                    startActivity(intent)

                }

                R.id.btn_chat_owner_buy_and_details->{

                   // whatsApp(mAnimalDetails.owner_mobile.toString())




                    openSMS(mAnimalDetails.owner_mobile.toString())

                }



                R.id.btn_ok_buy_and_details->{
                    finish()
                }

            }
        }

    }



    /**
     * A function to call the firestore class function that will get the product details from cloud firestore based on the product id.
     */
    private fun getAnimalDetails() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreClass().getAnimalDetails(this@AnimalBuyAndDetailsActivity, mAnimalId)
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

        // Populate the product details in the UI.
        GlideLoader(this@AnimalBuyAndDetailsActivity).loadAnimalPicture(
            animal.image,
            iv_animal_image_buy_and_details
        )

        animalImages = animal.images

        setImages()

        et_category_buy_and_details.setText(animal.category)
        et_animal_breed_buy_and_details.setText(animal.breed)
        et_animal_price_buy_and_details.setText(animal.price)
        et_animal_description_buy_and_details.setText(animal.description)
        et_animal_owner_name_buy_and_details.setText(animal.owner_name)



    }


    private fun setImages()
    {
        rv_dashboard_animal_images.isNestedScrollingEnabled = false
        val adapter = AnimalImageAdapter(
            this,
            animalImages
        )
        rv_dashboard_animal_images.adapter = adapter
        val rad = resources.getDimension(R.dimen.radius)
        val dotsHeight = resources.getDimensionPixelSize(R.dimen.dots_height)
        val inactiveColor = ContextCompat.getColor(this, R.color.colorLightGrey)
        val activeColor = ContextCompat.getColor(this, R.color.primary)
        val itemDecoration =
            DotsIndicatorDecoration(rad, rad * 4, dotsHeight, inactiveColor, activeColor)
        rv_dashboard_animal_images.addItemDecoration(itemDecoration)
        PagerSnapHelper().attachToRecyclerView(rv_dashboard_animal_images)

    }


    private fun openSMS(no:String) {
        val message = "message here"
         val phone = "91$no" //91 India code.

        val uri = Uri.parse("smsto:+$phone")
        val intent = Intent(Intent.ACTION_SENDTO, uri)

        with(intent) {
            putExtra("address", "+$phone")
            //putExtra("sms_body", message)
        }

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                //Getting the default sms app.
                val defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(applicationContext)

                // Can be null in case that there is no default, then the user would be able to choose
                // any app that support this intent.
                if (defaultSmsPackageName != null) intent.setPackage(defaultSmsPackageName)

                startActivity(intent)
            }
            else -> startActivity(intent)
        }
    }

}