package com.devabhijeet.livestockbazaar.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.User
import com.devabhijeet.livestockbazaar.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUsrDetails : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        //removing status bar
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //if user instance is present then automatically go to dashboard activity.
        if (FirebaseAuth.getInstance().currentUser != null && FirebaseAuth.getInstance().currentUser?.isEmailVerified == true)
        {
            logInRegisteredUser()
        }

        btn_login_login_activity.setOnClickListener(this)

        tv_forgot_password_login_activity.setOnClickListener(this)

        tv_register_login_activity.setOnClickListener(this)


    }




    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.tv_forgot_password_login_activity -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                   // Toast.makeText(this@LoginActivity,"forgot password acivity will get fired!!",Toast.LENGTH_SHORT).show()

                }

                R.id.btn_login_login_activity -> {

                        logInRegisteredUser();

                }

                R.id.tv_register_login_activity -> {
                    // Launch the register screen when the user clicks on the text.
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }

    // A function to validate the login entries of a user.

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email_login_activity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password_login_activity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                 showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }

    /**
     * A function to Log-In. The user will be able to log in using the registered email and password with Firebase Authentication.
     */
    private fun logInRegisteredUser() {

        showProgressDialog(resources.getString(R.string.please_wait))

        if (FirebaseAuth.getInstance().currentUser != null)
        {
            FirestoreClass().getUserDetails(this@LoginActivity);
        }

        else if (validateLoginDetails()) {

            // Show the progress dialog.

            // Get the text from editText and trim the space
            val email = et_email_login_activity.text.toString().trim { it <= ' ' }
            val password = et_password_login_activity.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    // Hide the progress dialog
                    //hideProgressDialog()

                    val firebaseUser: FirebaseUser = task.result!!.user!! // !! means not null
                    if(firebaseUser.isEmailVerified) {

                        if (task.isSuccessful) {

                            //TODO: send user to main screen
                            // FirestoreClass().getUserDetails(this@LoginActivity);


                                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))

                            hideProgressDialog()
                            finish()

                        } else {
                            hideProgressDialog();
                            showErrorSnackBar(
                                task.exception!!.message.toString() + "Please validate your email address",
                                true
                            )
                        }
                    }

                    else
                    {
                        showErrorSnackBar("Please verify your email we have send the verification link to your email address!",true)
                    }



                }
        }
    }

    /**
     * A function to notify user that logged in success and get the user details from the FireStore database after authentication.
     */
    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        mUsrDetails = user

        hideProgressDialog()


        if(user.profileCompleted == 0)
        {
            // Redirect the user to Main Screen after log in.
         //   val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            startActivity(Intent(this@LoginActivity, SettingsActivity::class.java))
          //  intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
           // startActivity(intent)
        }
        else
        {
            // Redirect the user to Main Screen after log in.
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }

        finish()
    }


}