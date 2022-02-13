package com.devabhijeet.livestockbazaar.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devabhijeet.livestockbazaar.R
import com.devabhijeet.livestockbazaar.models.Animal
import com.devabhijeet.livestockbazaar.ui.activities.SellToBuyerActivity
import com.devabhijeet.livestockbazaar.ui.fragments.SellAnimalFragment
import com.devabhijeet.livestockbazaar.utils.Constants
import com.devabhijeet.livestockbazaar.utils.GlideLoader
import kotlinx.android.synthetic.main.item_my_animal_list_layout.view.*


/**
 * A adapter class for animals list items.
 */
open class MyAnimalsListAdapter (
    private val context: Context,
    private var list: ArrayList<Animal>,
    private val fragment: SellAnimalFragment
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        /**
         * Inflates the item views which is designed in xml layout file
         *
         * create a new
         * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return MyViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_my_animal_list_layout,
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
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val model = list[position]

            if (holder is MyViewHolder) {

                GlideLoader(context).loadAnimalPicture(model.image, holder.itemView.iv_item_image)

                holder.itemView.tv_item_animal_category.text = model.category
                holder.itemView.tv_item_animal_breed.text = model.breed
                holder.itemView.tv_item_animal_price.text = "${model.price}"

                holder.itemView.ib_delete_product.setOnClickListener {

                    fragment.deleteAnimal(model.animal_id)
                }

              // This will go to sell my animal acivity where user can sell his animal my scanning buyers  qr code
              holder.itemView.setOnClickListener{
                    val intent = Intent(context,SellToBuyerActivity::class.java)
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
         * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
         */
        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}