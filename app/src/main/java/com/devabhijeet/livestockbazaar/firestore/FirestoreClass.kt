package com.devabhijeet.livestockbazaar.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.devabhijeet.livestockbazaar.models.Animal
import com.devabhijeet.livestockbazaar.models.BoughtAnimals
import com.devabhijeet.livestockbazaar.models.SoldAnimals
import com.devabhijeet.livestockbazaar.models.User
import com.devabhijeet.livestockbazaar.ui.activities.*
import com.devabhijeet.livestockbazaar.ui.fragments.BoughtAnimalFragment
import com.devabhijeet.livestockbazaar.ui.fragments.DashboardFragment
import com.devabhijeet.livestockbazaar.ui.fragments.SellAnimalFragment
import com.devabhijeet.livestockbazaar.ui.fragments.SoldAnimalFragment
import com.devabhijeet.livestockbazaar.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    /**
     * A function to make an entry of the registered user in the FireStore database.
     */
    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }
    // END



    ///get current user id function

    fun getCurrentUserID():String{

        val currentUser= FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if(currentUser!=null)
            currentUserID = currentUser.uid
        else
            Log.d("fireerror", "current user id : =$currentUserID")

        return currentUserID
    }


    /**
     * A function to get the logged user details from from FireStore Database.
     */
    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(Constants.LIVESTOCK_BAZAAR_PREFRENCES,
                    Context.MODE_PRIVATE)

                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.fullName}"
                )
                editor.apply()


                // START
                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)
                    }

                    is SettingsActivity ->{
                        // TODO Step 7: Call the function of base class.
                        // Call a function of base activity for transferring the result to it.
                        activity.userDetailsSuccess(user)
                        // END
                    }

                    is AddAnimalForSellActivity ->{
                        // TODO Step 7: Call the function of base class.
                        // Call a function of base activity for transferring the result to it.
                        activity.userDetailsSuccess(user)
                        // END
                    }

                    is SellToBuyerActivity ->{

                        activity.ownerDetailsSuccess(user)
                        // END
                    }

                }
                // END

            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }

                    is SettingsActivity ->{

                       activity.hideProgressDialog()
                    }

                    is AddAnimalForSellActivity ->{
                        activity.hideProgressDialog()
                    }

                    is SellToBuyerActivity ->{

                        activity.hideProgressDialog()
                        // END
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }

    }




    fun updateUserProfileData(activity: Activity,userHashMap: HashMap<String,Any>){

        mFireStore.collection(Constants.USERS).document(getCurrentUserID()).update(userHashMap)
            .addOnSuccessListener {
                when(activity)
                {
                    is UserProfileActivity -> {

                        activity.userProfileUpdateSuccess()

                    }
                }


            }
            .addOnFailureListener{
                    e->
                when(activity)
                {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }

                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details. ",
                    e
                )
            }

    }




    // START
    // A function to upload the image to the cloud storage.
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                Toast.makeText(activity.applicationContext," image uploaded successfully!",
                    Toast.LENGTH_SHORT).show()

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        // TODO Step 8: Pass the success result to base class.
                        // START
                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            // todo: uncomment this after addign addanimal activity
                            is AddAnimalForSellActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                        // END
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    // todo: uncomment this after addign addanimal activity
                     is AddAnimalForSellActivity -> {
                         activity.hideProgressDialog()
                     }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
    // END




    /**
     * A function to make an entry of the user's animal in the cloud firestore database.
     */
//    fun uploadAnimalDetails(activity: AddAnimalForSellActivity, animalInfo: Animal) {
//
//        mFireStore.collection(Constants.ANIMALS)
//            .document(mFireStore.collection(Constants.ANIMALS).document().id)
//            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
//            .set(animalInfo, SetOptions.merge())
//            .addOnSuccessListener {
//
//                // Here call a function of base activity for transferring the result to it.
//                activity.animalUploadSuccess()
//            }
//            .addOnFailureListener { e ->
//
//                activity.hideProgressDialog()
//
//                Log.e(
//                    activity.javaClass.simpleName,
//                    "Error while uploading the animal details.",
//                    e
//                )
//            }
//    }


    /**
     * A function to make an entry of the user's animal in the cloud firestore database.
     */
    fun uploadAnimalDetails(activity: AddAnimalForSellActivity, animalInfo: Animal) {

        mFireStore.collection(Constants.ANIMALS)
            .add(animalInfo)
            .addOnSuccessListener { documentReference ->

                animalInfo.animal_id = documentReference.id

                mFireStore.collection(Constants.ANIMALS).document(documentReference.id).set(animalInfo, SetOptions.merge())
                // Here call a function of base activity for transferring the result to it.
                activity.animalUploadSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the animal details.",
                    e
                )
            }
    }


    /**
     * A function to get the animal list from cloud firestore.
     *
     * @param fragment The fragment is passed as parameter as the function is called from fragment and need to the success result.
     */
    //todo working..........
    fun getMyAnimalsList(fragment: Fragment) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.ANIMALS)
            .whereEqualTo(Constants.OWNER_ID, getCurrentUserID()) //here was the error user id -> owner id
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                //todo getting error here



                Log.e("Animals List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val animalsList: ArrayList<Animal> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val animal = i.toObject(Animal::class.java)
                    animal!!.animal_id = i.id

                    animalsList.add(animal)
                }

                when (fragment) {
                    is SellAnimalFragment -> {
                        //todo uncomment below
                        fragment.successAnimalsListFromFireStore(animalsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is SellAnimalFragment -> {
                        //todo uncomment below
                        fragment.hideProgressDialog()
                    }
                }
                Log.e("Get Animal List", "Error while getting animal list.", e)
            }
    }



    /**
     * A function to delete the product from the cloud firestore.
     */
    fun deleteAnimal(fragment: SellAnimalFragment, animalId: String) {

        mFireStore.collection(Constants.ANIMALS)
            .document(animalId)
            .delete()
            .addOnSuccessListener {

                // TODO Step 4: Notify the success result to the base class.
                // START
                //todo  Notify the success result to the base class.
                //todo uncomment below
                fragment.animalDeleteSuccess()
                // END
            }
            .addOnFailureListener { e ->

                //todo Hide the progress dialog if there is an error.

                fragment.hideProgressDialog()

                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the animal.",
                    e
                )
            }
    }
    // END

////////

    /**
     * A function to get the dashboard items list. The list will be an overall items list, not based on the user's id.
     */
    fun getDashboardItemsList(fragment: DashboardFragment) {
        // The collection name for ANIMALS
        mFireStore.collection(Constants.ANIMALS)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Animals ArrayList.
                val animalsList: ArrayList<Animal> = ArrayList()

                // A for loop as per the list of documents to convert them into Animals ArrayList.
                for (i in document.documents) {

                    val animal = i.toObject(Animal::class.java)!!
                    animal.animal_id = i.id
                    animalsList.add(animal)
                }

                // Pass the success result to the base fragment.
                //todo uncomment below
                fragment.successDashboardItemsList(animalsList)
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error which getting the dashboard items list.
                //todo uncomment below
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items list.", e)
            }
    }

///get dashboard animal by category

    /**
     * A function to get the products list from cloud firestore.
     *
     * @param fragment The fragment is passed as parameter as the function is called from fragment and need to the success result.
     */
    //todo working..........
    fun getDashboardAnimalsByCategory(fragment: DashboardFragment,category:String) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.ANIMALS)
            .whereEqualTo(Constants.CATEGORY, category) //here was the error user id -> owner id
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                //todo getting error here


                // Here we get the list of boards in the form of documents.
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                // Here we have created a new instance for Animals ArrayList.
                val animalsList: ArrayList<Animal> = ArrayList()

                // A for loop as per the list of documents to convert them into Animals ArrayList.
                for (i in document.documents) {

                    val animal = i.toObject(Animal::class.java)!!
                    animal.animal_id = i.id
                    animalsList.add(animal)
                }

                // Pass the success result to the base fragment.
                //todo uncomment below
                fragment.successDashboardItemsList(animalsList)

            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error which getting the dashboard items list.
                //todo uncomment below
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items list.", e)
            }
    }




    /**
     * A function to get the animal details based on the animal id.
     */
    fun getAnimalDetails(activity: Activity, animalId : String) {

        // The collection name for PRODUCTS
        mFireStore.collection(Constants.ANIMALS)
            .document(animalId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                // Here we get the product details in the form of document.
                Log.e(activity.javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Product data model class.
                val animal = document.toObject(Animal::class.java)!!

                when (activity) {
                    is AnimalBuyAndDetailsActivity -> {
                        activity.animalDetailsSuccess(animal)
                    }

                    is SellToBuyerActivity -> {
                        activity.animalDetailsSuccess(animal)
                    }

                }
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.

                when (activity) {
                    is AnimalBuyAndDetailsActivity -> {
                        activity.hideProgressDialog()
                        Log.e(activity.javaClass.simpleName, "Error while getting the animal details.", e)

                    }

                    is SellToBuyerActivity -> {
                        activity.hideProgressDialog()
                        Log.e(activity.javaClass.simpleName, "Error while getting the animal details.", e)

                    }
                }
            }
    }


    /**
     * A function to make an entry of the user's sold animals in the cloud firestore database.
     */
    fun uploadSoldAnimalDetails(activity: SellToBuyerActivity, soldAnimalInfo: SoldAnimals) {

        mFireStore.collection(Constants.SOLD_ANIMALS)
            .add(soldAnimalInfo)
            .addOnSuccessListener {

                    documentReference ->

                soldAnimalInfo.id = documentReference.id

                mFireStore.collection(Constants.SOLD_ANIMALS).document(documentReference.id).set(soldAnimalInfo, SetOptions.merge())
                // Here call a function of base activity for transferring the result to it.
                activity.animalSoldUploadSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the sold animal details.",
                    e
                )
            }
    }

    /**
     * A function to make an entry of the buyer's bought animals in the cloud firestore database.
     */
    fun uploadBoughtAnimalDetails(activity: SellToBuyerActivity, boughtAnimalInfo: BoughtAnimals) {

        mFireStore.collection(Constants.BOUGHT_ANIMALS)
            .add(boughtAnimalInfo)
            .addOnSuccessListener {
                    documentReference ->

                boughtAnimalInfo.id = documentReference.id

                mFireStore.collection(Constants.BOUGHT_ANIMALS).document(documentReference.id).set(boughtAnimalInfo, SetOptions.merge())
                // Here call a function of base activity for transferring the result to it.
                activity.animalBoughtUploadSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the sold animal details.",
                    e
                )
            }
    }



    /**
     * A function to delete the product from the cloud firestore.
     */
    fun deleteAnimalAfterSelling(activity: SellToBuyerActivity, animalId: String) {

        mFireStore.collection(Constants.ANIMALS)
            .document(animalId)
            .delete()
            .addOnSuccessListener {

                // TODO Step 4: Notify the success result to the base class.
                // START
                //todo  Notify the success result to the base class.
                //todo uncomment below
                activity.animalDeleteSuccess()
                // END
            }
            .addOnFailureListener { e ->

                //todo Hide the progress dialog if there is an error.

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while deleting the animal.",
                    e
                )
            }
    }

    /**
     * get user details just for transactional purpose
     */

    /**
     * A function to get the logged user details from from FireStore Database.
     */
    fun getUserDetailsBuyer(activity: Activity,userID:String) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(userID)
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!


                // START
                when (activity) {
                    is SellToBuyerActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.buyerDetailsSuccess(user)
                    }



                }
                // END

            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is SellToBuyerActivity -> {

                        activity.hideProgressDialog()
                    }



                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting buyer details.",
                    e
                )
            }

    }


    fun getSoldAnimalsList(fragment: Fragment) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.SOLD_ANIMALS)
            .whereEqualTo(Constants.OWNER_ID, getCurrentUserID()) //here was the error user id -> owner id
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                //todo getting error here



                Log.e("Animals List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val soldAnimalsList: ArrayList<SoldAnimals> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val soldAnimal = i.toObject(SoldAnimals::class.java)
                    soldAnimal!!.animal_id = i.id

                    soldAnimalsList.add(soldAnimal)
                }

                when (fragment) {
                    is SoldAnimalFragment -> {
                        //todo uncomment below
                        fragment.successSoldAnimalsListFromFireStore(soldAnimalsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is SoldAnimalFragment -> {
                        //todo uncomment below
                        fragment.hideProgressDialog()
                    }
                }
                Log.e("Get Animal List", "Error while getting animal list.", e)
            }
    }



    fun getBoughtAnimalsList(fragment: Fragment) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.BOUGHT_ANIMALS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID()) //here was the error user id -> owner id
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                //todo getting error here



                Log.e("Animals List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val boughtAnimalsList: ArrayList<BoughtAnimals> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val boughtAnimal = i.toObject(BoughtAnimals::class.java)
                    boughtAnimal!!.animal_id = i.id

                    boughtAnimalsList.add(boughtAnimal)
                }

                when (fragment) {
                    is BoughtAnimalFragment -> {
                        //todo uncomment below
                        fragment.successBoughtAnimalsListFromFireStore(boughtAnimalsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is BoughtAnimalFragment -> {
                        //todo uncomment below
                        fragment.hideProgressDialog()
                    }
                }
                Log.e("Get Animal List", "Error while getting animal list.", e)
            }
    }





    /**
     * A function to get the sold animal details based on the sold animal id.
     */
    fun getSoldAnimalDetails(activity: ShowSoldAnimalDetailsActivity, soldAnimalId : String) {

        // The collection name for PRODUCTS
        mFireStore.collection(Constants.SOLD_ANIMALS)
            .document(soldAnimalId)
            .get() // Will get the document snapshots.
            .addOnSuccessListener { document ->

                // Here we get the product details in the form of document.
                Log.e(activity.javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Product data model class.
                val soldAnimal = document.toObject(SoldAnimals::class.java)!!


                        activity.soldAnimalDetailsSuccess(soldAnimal)


            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.

                       // activity.hideProgressDialog()
                        Log.e(activity.javaClass.simpleName, "Error while getting the sold animal details.", e)


                }

    }




/**
 * A function to get the bought animal details based on the bought animal id.
 */
fun getBoughtAnimalDetails(activity: ShowBoughtAnimalDetailsActivity, boughtAnimalId : String) {

    // The collection name for PRODUCTS
    mFireStore.collection(Constants.BOUGHT_ANIMALS)
        .document(boughtAnimalId.trim())
        .get() // Will get the document snapshots.
        .addOnSuccessListener { document ->

            // Here we get the product details in the form of document.
            Log.e(activity.javaClass.simpleName, document.toString())

            // Convert the snapshot to the object of Product data model class.
            val boughtAnimal = document.toObject(BoughtAnimals::class.java)!!

            activity.boughtAnimalDetailsSuccess(boughtAnimal)

        }
        .addOnFailureListener { e ->

            // Hide the progress dialog if there is an error.

            activity.hideProgressDialog()
            Log.e(activity.javaClass.simpleName, "Error while getting the bought animal details.", e)

        }

}






}