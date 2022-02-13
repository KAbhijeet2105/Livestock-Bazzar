package com.devabhijeet.livestockbazaar.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.firestore.FirestoreClass
import com.devabhijeet.livestockbazaar.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        tv_sign_in_register_acivity.setOnClickListener {
            onBackPressed();
            //finish();
        }

        //button for registering user

        btn_create_account_register_acivity.setOnClickListener {

            registerUser();
        }
    }



    /**
     * A function to validate the entries of a new user.
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_fullname_register_acivity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_full_name), true)
                false
            }

            TextUtils.isEmpty(et_email_register_acivity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_password_register_acivity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }


            else -> {
                // showErrorSnackBar(resources.getString(R.string.register_success), false)
                true
            }
        }
    }



    //register  user

    private fun registerUser() {

        // Check with validate function if the entries are valid or not.
        if (validateRegisterDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait));

            val email: String = et_email_register_acivity.text.toString().trim { it <= ' ' }
            val password: String = et_password_register_acivity.text.toString().trim { it <= ' ' }

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!! // !! means not null

                                firebaseUser.sendEmailVerification().addOnSuccessListener {

                                    showErrorSnackBar(
                               " verification link sent on your email please verify your email address ", false)

                                    val user = User(
                                        firebaseUser.uid,
                                        et_fullname_register_acivity.text.toString().trim { it <= ' '},
                                        et_email_register_acivity.text.toString().trim { it <= ' '},

                                        )


                                    FirestoreClass().registerUser(this@RegisterActivity,user);


                                }
                                    .addOnFailureListener { e->

                                        showErrorSnackBar(
                                            " unable to send varification email $e ",
                                            true)
                                    }

                            //create user entry in database



//
//                            showErrorSnackBar(
//                                "You are registered successfully. Your user id is ${firebaseUser.uid}",
//                                false
//                            )

                            //signing new user and redirecting user back to login screen
                            //FirebaseAuth.getInstance().signOut();
                            //finish();


                        } else {
                            // If the registering is not successful then show error message.
                            hideProgressDialog();
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })


        }//if end

    }//function register user end


    /**
     * A function to notify the success result of Firestore entry when the user is registered successfully.
     */
    fun userRegistrationSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        // TODO Step 5: Replace the success message to the Toast instead of Snackbar.
        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()


        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        // Finish the Register Screen
        finish()
    }



}