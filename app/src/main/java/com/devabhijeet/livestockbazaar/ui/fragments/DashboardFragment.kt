package com.devabhijeet.livestockbazaar.ui.fragments

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.Animal
import com.devabhijeet.livestockbazaar.ui.activities.SettingsActivity
import com.devabhijeet.livestockbazaar.ui.activities.ShowMyQRActivity
import com.devabhijeet.livestockbazaar.ui.activities.UserProfileActivity
import com.devabhijeet.livestockbazaar.ui.adapters.DashboardItemsListAdapter
import com.devabhijeet.livestockbazaar.utils.Constants
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_add_animal_for_sell.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*


class DashboardFragment : BaseFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val root = inflater.inflate(R.layout.fragment_dashboard,container,false)


        val animalCategories = resources.getStringArray(R.array.Animal_Categories_Dashboard)

        // working todo attach listener and filter animal list
        root.spinr_category_filter_dashboard_fragment.adapter =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, animalCategories) }




        root.spinr_category_filter_dashboard_fragment?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val category = parent?.getItemAtPosition(position).toString()
               // Toast.makeText(activity,category, Toast.LENGTH_LONG).show()

                if (position!=0 && position!= 1) {
                    println(category)
                    showProgressDialog(resources.getString(R.string.please_wait))

                    FirestoreClass().getDashboardAnimalsByCategory(this@DashboardFragment, category)
                }

                if (position == 1)
                {
                    onResume()
                }

            }

        }




        root.tv_action_setting_dashboard_fragment.setOnClickListener {

            goProfile()
        }

        root.tv_action_show_qr_dashboard_fragment.setOnClickListener {
            showMyQR()
        }

        return root
    }







    override fun onResume() {
        super.onResume()

        getDashboardItemsList()
    }

    /**
     * A function to get the dashboard items list from cloud firestore.
     */
    private fun getDashboardItemsList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getDashboardItemsList(this@DashboardFragment)
    }

    /**
     * A function to get the success result of the dashboard items from cloud firestore.
     *
     * @param dashboardItemsList
     */
    fun successDashboardItemsList(dashboardItemsList: ArrayList<Animal>) {

        // Hide the progress dialog.
        hideProgressDialog()

        if (dashboardItemsList.size > 0) {

            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            rv_dashboard_items.layoutManager = GridLayoutManager(activity, 2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapter = DashboardItemsListAdapter(requireActivity(), dashboardItemsList)
            rv_dashboard_items.adapter = adapter



        } else {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }
    }











    private fun goProfile()
    {
        startActivity(Intent(activity,SettingsActivity::class.java))
    }

    private fun showMyQR()
    {
        startActivity(Intent(activity,ShowMyQRActivity::class.java))
    }


    companion object {

        @JvmStatic
        fun newInstance() = DashboardFragment().apply {
            arguments = Bundle().apply {  }
        }
    }

}