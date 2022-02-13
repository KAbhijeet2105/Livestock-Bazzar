package com.devabhijeet.livestockbazaar.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.BoughtAnimals
import com.devabhijeet.livestockbazaar.ui.adapters.BoughtAnimalListAdapter
import kotlinx.android.synthetic.main.fragment_bought_animal.*


/**
 * A simple [Fragment] subclass.
 * Use the [BoughtAnimalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BoughtAnimalFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val root = inflater.inflate(R.layout.fragment_bought_animal,container,false)

       // Toast.makeText(this.context,"bought animal ",Toast.LENGTH_SHORT).show()

        return root
    }

    override fun onResume() {
        super.onResume()

        getBoughtAnimalListFromFireStore()
    }

    private fun getBoughtAnimalListFromFireStore() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getBoughtAnimalsList(this@BoughtAnimalFragment)
    }



    /**
     * A function to get the successful product list from cloud firestore.
     *
     * @param animal List Will receive the product list from cloud firestore.
     */
    fun successBoughtAnimalsListFromFireStore(animalsList: ArrayList<BoughtAnimals>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (animalsList.size > 0) {
            rv_bought_animal_items.visibility = View.VISIBLE
            tv_no_bought_animals_found.visibility = View.GONE

            rv_bought_animal_items.layoutManager = LinearLayoutManager(activity)
            rv_bought_animal_items.setHasFixedSize(true)

            val adapterAnimals =
                BoughtAnimalListAdapter(requireActivity(), animalsList, this@BoughtAnimalFragment)
            rv_bought_animal_items.adapter = adapterAnimals
        } else {
            rv_bought_animal_items.visibility = View.GONE
            tv_no_bought_animals_found.visibility = View.VISIBLE
        }
    }















    companion object {

        @JvmStatic
        fun newInstance() = BoughtAnimalFragment().apply {
            arguments = Bundle().apply {  }
        }
    }

}