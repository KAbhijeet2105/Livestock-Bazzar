package com.devabhijeet.livestockbazaar.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.Animal
import com.devabhijeet.livestockbazaar.models.SoldAnimals
import com.devabhijeet.livestockbazaar.ui.adapters.MyAnimalsListAdapter
import com.devabhijeet.livestockbazaar.ui.adapters.SoldAnimalListAdapter
import kotlinx.android.synthetic.main.fragment_sell_animals.*
import kotlinx.android.synthetic.main.fragment_sold_animals.*


class SoldAnimalFragment : BaseFragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_sold_animals,container,false)



        return root
    }


    override fun onResume() {
        super.onResume()

        getSoldAnimalListFromFireStore()
    }

    private fun getSoldAnimalListFromFireStore() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getSoldAnimalsList(this@SoldAnimalFragment)
    }



    /**
     * A function to get the successful product list from cloud firestore.
     *
     * @param animal List Will receive the product list from cloud firestore.
     */
    fun successSoldAnimalsListFromFireStore(animalsList: ArrayList<SoldAnimals>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (animalsList.size > 0) {
            rv_sold_animal_items.visibility = View.VISIBLE
            tv_no_sold_animals_found.visibility = View.GONE

            rv_sold_animal_items.layoutManager = LinearLayoutManager(activity)
            rv_sold_animal_items.setHasFixedSize(true)

            val adapterAnimals =
                SoldAnimalListAdapter(requireActivity(), animalsList, this@SoldAnimalFragment)
            rv_sold_animal_items.adapter = adapterAnimals
        } else {
            rv_sold_animal_items.visibility = View.GONE
            tv_no_sold_animals_found.visibility = View.VISIBLE
        }
    }







    companion object {

        @JvmStatic
        fun newInstance() = SoldAnimalFragment().apply {
            arguments = Bundle().apply {  }
        }
    }
}