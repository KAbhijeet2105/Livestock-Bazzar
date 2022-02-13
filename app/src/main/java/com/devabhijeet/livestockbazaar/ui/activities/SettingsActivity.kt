package com.devabhijeet.livestockbazaar.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.User
import com.devabhijeet.livestockbazaar.utils.Constants
import com.devabhijeet.livestockbazaar.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(),View.OnClickListener {

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        btn_logout_settings.setOnClickListener(this)
        tv_edit_settings.setOnClickListener(this)

    }
    override fun onResume() {
        super.onResume()

        getUserDetails()
    }

    /**
     * A function to get the user details from firestore.
     */
    private fun getUserDetails() {

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this@SettingsActivity)
    }
    // END

    // START
    /**
     * A function to receive the user details and populate it in the UI.
     */
    fun userDetailsSuccess(user: User) {

        mUserDetails = user

        // TODO Step 9: Set the user details to UI.
        // START
        // Hide the progress dialog
        hideProgressDialog()

        // Load the image using the Glide Loader class.
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, iv_user_photo_settings)

        tv_full_name_settings.text = "${user.fullName}"
        tv_gender_settings.text = user.gender
        tv_email_settings.text = user.email
        tv_mobile_number_settings.text = "${user.mobile}"
        tv_address_settings.text = "${user.address}"
        // END
    }

    override fun onClick(v: View?) {

        if(v!= null)
        {
            when(v.id)
            {
                R.id.btn_logout_settings ->{
                    FirebaseAuth.getInstance().signOut();
                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                R.id.tv_edit_settings ->  {

                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
                    startActivity(intent)

                }




            }



        }

    }
    // END



}