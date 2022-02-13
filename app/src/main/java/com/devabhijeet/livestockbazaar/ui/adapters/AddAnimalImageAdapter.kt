package com.devabhijeet.livestockbazaar.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devabhijeet.livestockbazaar.R
import kotlinx.android.synthetic.main.add_images_item.view.*

class AddAnimalImageAdapter(private val context: Context, images: List<Uri>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: MutableList<Uri> = images as MutableList<Uri>



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        return MyViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.add_images_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val imageUrl = data[position]


        if (holder is MyViewHolder) {


            holder.itemView.iv_add_img_close_btn.setOnClickListener {
                deleteItem(position)
            }

            if (imageUrl.toString().contains("https://")) {
                Glide.with(context)
                    .asBitmap()
                    .load(imageUrl.buildUpon().scheme("https").build())
                    .into(holder.itemView.iv_add_animal_multi_images)
            } else {
                holder.itemView.iv_add_animal_multi_images.setImageURI(imageUrl)
            }

        }


    }

    override fun getItemCount(): Int = data.size

    private fun deleteItem(index: Int) {
        data.removeAt(index)
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}