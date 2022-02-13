package com.devabhijeet.livestockbazaar.ui.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.ui.fragments.BoughtAnimalFragment
import com.devabhijeet.livestockbazaar.ui.fragments.DashboardFragment
import com.devabhijeet.livestockbazaar.ui.fragments.SellAnimalFragment
import com.devabhijeet.livestockbazaar.ui.fragments.SoldAnimalFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_main.*



class DashboardActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)



        addFragment(DashboardFragment.newInstance())
        bottom_navigation_meow.show(0)
        bottom_navigation_meow.add(MeowBottomNavigation.Model(0,R.drawable.dashboard_icon))
        bottom_navigation_meow.add(MeowBottomNavigation.Model(1,R.drawable.icon_sell))
        bottom_navigation_meow.add(MeowBottomNavigation.Model(2,R.drawable.icon_check))
        bottom_navigation_meow.add(MeowBottomNavigation.Model(3,R.drawable.ic_vector_cart))


        bottom_navigation_meow.setOnClickMenuListener {

            when(it.id)
            {
                0 -> {
                    replaceFragment(DashboardFragment.newInstance())
                }

                1 -> {
                    replaceFragment(SellAnimalFragment.newInstance())
                }
                2 -> {
                    replaceFragment(SoldAnimalFragment.newInstance())
                }
                3 -> {
                    replaceFragment(BoughtAnimalFragment.newInstance())
                }



            }
        }

    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.dashboard_activity_frame_layout,fragment).commit()
    }

    private fun addFragment(fragment:Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.dashboard_activity_frame_layout,fragment).commit()
    }

    override fun onBackPressed() {
        doubleBackToExit();
    }
}