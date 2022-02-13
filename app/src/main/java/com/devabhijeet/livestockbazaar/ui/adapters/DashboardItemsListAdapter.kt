package com.devabhijeet.livestockbazaar.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.models.Animal
import com.devabhijeet.livestockbazaar.ui.activities.AnimalBuyAndDetailsActivity
import com.devabhijeet.livestockbazaar.utils.Constants
import com.devabhijeet.livestockbazaar.utils.GlideLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*


/**
 * A adapter class for dashboard items list.
 */
open class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Animal>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadAnimalPicture(
                model.image,
                holder.itemView.iv_dashboard_item_image
            )
            holder.itemView.tv_dashboard_item_category.text = model.category
            holder.itemView.tv_dashboard_item_breed.text = model.breed
            holder.itemView.tv_dashboard_item_price.text = "${model.price}"


              //  todo: here pass intent to buy animal activity where user can contact owner and chat or call

            holder.itemView.setOnClickListener{
                val intent = Intent(context,AnimalBuyAndDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_ANIMAL_ID,model.animal_id)
                intent.putExtra(Constants.EXTRA_ANIMAL_OWNER_ID,model.owner_id)
                context.startActivity(intent)
            }



        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function for OnClickListener where the Interface is the expected parameter and assigned to the global variable.
     *
     * @param onClickListener
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    // END

    // TODO Step 1: Create an interface for OnClickListener.
    /**
     * An interface for onclick items.
     */

    interface OnClickListener {

        // TODO Step 4: Define a function to get the required params when user clicks on the item view in the interface.
        // START
        fun onClick(position: Int, animal: Animal)
        // END
    }


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}