package com.devabhijeet.livestockbazaar.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.PagerSnapHelper
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.Animal
import com.devabhijeet.livestockbazaar.models.BoughtAnimals
import com.devabhijeet.livestockbazaar.ui.adapters.AnimalImageAdapter
import com.devabhijeet.livestockbazaar.utils.Constants
import com.devabhijeet.livestockbazaar.utils.DotsIndicatorDecoration
import com.devabhijeet.livestockbazaar.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_show_bought_animal_details.*
import kotlinx.android.synthetic.main.activity_show_sold_animal_details.*
import java.text.SimpleDateFormat
import java.util.*

class ShowBoughtAnimalDetailsActivity : BaseActivity() {

    private var mBoughtAnimalId: String = ""

    private lateinit var mBoughtAnimalDetails: BoughtAnimals

    var animalImages: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_bought_animal_details)


        if (intent.hasExtra(Constants.EXTRA_BOUGHT_ANIMAL_ID)) {
            mBoughtAnimalId = intent.getStringExtra(Constants.EXTRA_BOUGHT_ANIMAL_ID)!!

            getBoughtAnimalDetail()

        }
        else
        {
            showErrorSnackBar("error while fetching bought animal",true)
        }


        btn_ok_bought_animal_details.setOnClickListener {
            finish()
        }

    }



    /**
     * A function to call the firestore class function that will get the bought details from cloud firestore based on the product id.
     */
    private fun getBoughtAnimalDetail() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreClass().getBoughtAnimalDetails(this@ShowBoughtAnimalDetailsActivity, mBoughtAnimalId)
    }

    /**
     * A function to notify the success result of the bought animal details based on the product id.
     *
     * @param bought_animal A model class with product details.
     */
    fun boughtAnimalDetailsSuccess(boughtAnimal: BoughtAnimals) {

        mBoughtAnimalDetails = boughtAnimal;

        // Hide Progress dialog.
        hideProgressDialog()

        // Populate the product details in the UI.
      /*  GlideLoader(this@ShowBoughtAnimalDetailsActivity).loadAnimalPicture(
            boughtAnimal.image,
            iv_animal_image_bought_animal_details
        )*/

        animalImages = boughtAnimal.images

        setImages()

        //setting date

        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = boughtAnimal.buy_date

        val buyDateTime = formatter.format(calendar.time)
        tv_bought_animal_details_buy_date.text = buyDateTime



        et_category_bought_animal_details.setText(boughtAnimal.category)
        et_animal_breed_bought_animal_details.setText(boughtAnimal.breed)
        et_animal_price_bought_animal_details.setText(boughtAnimal.buy_amount)
        et_animal_description_bought_animal_details.setText(boughtAnimal.description)

        tv_seller_name_bought_animal_details_buy_date.text = boughtAnimal.seller_name
        tv_seller_mobile_bought_animal_details_buy_date.text = boughtAnimal.seller_mobile.toString().trim()


    }

    private fun setImages()
    {
        rv_animal_images_bought_animal_details.isNestedScrollingEnabled = false
        val adapter = AnimalImageAdapter(
            this,
            animalImages
        )
        rv_animal_images_bought_animal_details.adapter = adapter
        val rad = resources.getDimension(R.dimen.radius)
        val dotsHeight = resources.getDimensionPixelSize(R.dimen.dots_height)
        val inactiveColor = ContextCompat.getColor(this, R.color.colorLightGrey)
        val activeColor = ContextCompat.getColor(this, R.color.primary)
        val itemDecoration =
            DotsIndicatorDecoration(rad, rad * 4, dotsHeight, inactiveColor, activeColor)
        rv_animal_images_bought_animal_details.addItemDecoration(itemDecoration)
        PagerSnapHelper().attachToRecyclerView(rv_animal_images_bought_animal_details)

    }

}