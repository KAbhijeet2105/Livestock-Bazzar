package com.devabhijeet.livestockbazaar.ui.activities

import android.os.Bundle
import android.view.Window
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.devabhijeet.livestockbazaar.R


class MainActivity : AppCompatActivity() {

   // private lateinit var binding: ActivityDashBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      //  binding = ActivityDashBoardBinding.inflate(layoutInflater)
       // setContentView(binding.root)

      //  val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_dash_board)


      /*  val appBarConfiguration = AppBarConfiguration(
            setOf(
                 R.id.navigation_dashboard, R.id.navigation_sell_animals,R.id.navigation_sold_animals,R.id.navigation_bought_animals

            )
        )*/
        //setupActionBarWithNavController(navController, appBarConfiguration)
     //   navView.setupWithNavController(navController)
    }
}