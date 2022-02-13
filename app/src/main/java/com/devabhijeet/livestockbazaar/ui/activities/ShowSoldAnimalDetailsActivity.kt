package com.devabhijeet.livestockbazaar.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.PagerSnapHelper
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.SoldAnimals
import com.devabhijeet.livestockbazaar.ui.adapters.AnimalImageAdapter
import com.devabhijeet.livestockbazaar.utils.Constants
import com.devabhijeet.livestockbazaar.utils.DotsIndicatorDecoration
import com.devabhijeet.livestockbazaar.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_sell_to_buyer.*
import kotlinx.android.synthetic.main.activity_show_sold_animal_details.*
import java.text.SimpleDateFormat
import java.util.*

class ShowSoldAnimalDetailsActivity : BaseActivity() {

    private var mSoldAnimalId: String = ""

    private lateinit var mSoldAnimalDetails: SoldAnimals

    var animalImages: List<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_sold_animal_details)

        if (intent.hasExtra(Constants.EXTRA_SOLD_ANIMAL_ID)) {
            mSoldAnimalId = intent.getStringExtra(Constants.EXTRA_SOLD_ANIMAL_ID)!!

            getSoldAnimalDetails()

        }
        else
        {
            showErrorSnackBar("error while fetching bought animal",true)
        }

        btn_ok_sold_animal_details.setOnClickListener {
            finish()
        }

    }

    /**
     * A function to call the firestore class function that will get the product details from cloud firestore based on the product id.
     */
    private fun getSoldAnimalDetails() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreClass().getSoldAnimalDetails(this@ShowSoldAnimalDetailsActivity, mSoldAnimalId)

    }



    /**
     * A function to notify the success result of the sold animal details based on the product id.
     *
     * @param sold_animal A model class with product details.
     */
    fun soldAnimalDetailsSuccess(soldAnimal: SoldAnimals) {

        mSoldAnimalDetails = soldAnimal;

        // Hide Progress dialog.
        hideProgressDialog()

        // Populate the product details in the UI.
       /* GlideLoader(this@ShowSoldAnimalDetailsActivity).loadAnimalPicture(
            soldAnimal.image,
            iv_animal_image_sold_animal_details
        )*/

        animalImages = soldAnimal.images

        setImages()
        //setting date

        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = soldAnimal.sell_date

        val soldDateTime = formatter.format(calendar.time)
        tv_sold_animal_details_sold_date.text = soldDateTime



        et_category_sold_animal_details.setText(soldAnimal.category)
        et_animal_breed_sold_animal_details.setText(soldAnimal.breed)
        et_animal_price_sold_animal_details.setText(soldAnimal.sell_amount)
        et_animal_description_sold_animal_details.setText(soldAnimal.description)

        tv_customer_name_sold_animal_details_buy_date.text = soldAnimal.buyer_name
        tv_customer_mobile_sold_animal_details.text = soldAnimal.buyer_mobile.toString().trim()


    }

    private fun setImages()
    {
        rv_animal_images_sold_animal_details.isNestedScrollingEnabled = false
        val adapter = AnimalImageAdapter(
            this,
            animalImages
        )
        rv_animal_images_sold_animal_details.adapter = adapter
        val rad = resources.getDimension(R.dimen.radius)
        val dotsHeight = resources.getDimensionPixelSize(R.dimen.dots_height)
        val inactiveColor = ContextCompat.getColor(this, R.color.colorLightGrey)
        val activeColor = ContextCompat.getColor(this, R.color.primary)
        val itemDecoration =
            DotsIndicatorDecoration(rad, rad * 4, dotsHeight, inactiveColor, activeColor)
        rv_animal_images_sold_animal_details.addItemDecoration(itemDecoration)
        PagerSnapHelper().attachToRecyclerView(rv_animal_images_sold_animal_details)

    }

}