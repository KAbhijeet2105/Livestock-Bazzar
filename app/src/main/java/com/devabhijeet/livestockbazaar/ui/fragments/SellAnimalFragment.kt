package com.devabhijeet.livestockbazaar.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.Animal
import com.devabhijeet.livestockbazaar.ui.activities.AddAnimalForSellActivity
import com.devabhijeet.livestockbazaar.ui.adapters.MyAnimalsListAdapter
import kotlinx.android.synthetic.main.fragment_sell_animals.*
import kotlinx.android.synthetic.main.fragment_sell_animals.view.*


class SellAnimalFragment : BaseFragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_sell_animals,container,false)

        root.fab_action_add_animal.setOnClickListener {

            startActivity(Intent(activity,AddAnimalForSellActivity::class.java))
        }

        return root
    }






    override fun onResume() {
        super.onResume()

        getAnimalListFromFireStore()
    }

    private fun getAnimalListFromFireStore() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getMyAnimalsList(this@SellAnimalFragment)
    }



    /**
     * A function to get the successful product list from cloud firestore.
     *
     * @param animal List Will receive the product list from cloud firestore.
     */
    fun successAnimalsListFromFireStore(animalsList: ArrayList<Animal>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (animalsList.size > 0) {
            rv_my_animal_items.visibility = View.VISIBLE
            tv_no_animals_found.visibility = View.GONE

            rv_my_animal_items.layoutManager = LinearLayoutManager(activity)
            rv_my_animal_items.setHasFixedSize(true)

            val adapterAnimals =
                MyAnimalsListAdapter(requireActivity(), animalsList, this@SellAnimalFragment)
            rv_my_animal_items.adapter = adapterAnimals
        } else {
            rv_my_animal_items.visibility = View.GONE
            tv_no_animals_found.visibility = View.VISIBLE
        }
    }




    /**
     * A function that will call the delete function of FirestoreClass that will delete the product added by the user.
     *
     * @param productID To specify which product need to be deleted.
     */
    fun deleteAnimal(animalID: String) {

        // TODO Step 6: Remove the toast message and call the function to ask for confirmation to delete the product.
        // START
        // Here we will call the delete function of the FirestoreClass. But, for now lets display the Toast message and call this function from adapter class.



        showAlertDialogToDeleteAnimal(animalID)
        // END
    }






    /**
     * A function to notify the success result of product deleted from cloud firestore.
     */
    fun animalDeleteSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.product_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()

        // Get the latest products list from cloud firestore.
        getAnimalListFromFireStore()
    }
    // END


    /**
     * A function to show the alert dialog for the confirmation of delete product from cloud firestore.
     */
    private fun showAlertDialogToDeleteAnimal(animalID: String) {

        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            // TODO Step 7: Call the function to delete the product from cloud firestore.
            // START
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Call the function of Firestore class.
            FirestoreClass().deleteAnimal(this@SellAnimalFragment , animalID)
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
    // END













    companion object {

        @JvmStatic
        fun newInstance() = SellAnimalFragment().apply {
            arguments = Bundle().apply {  }
        }
    }
}