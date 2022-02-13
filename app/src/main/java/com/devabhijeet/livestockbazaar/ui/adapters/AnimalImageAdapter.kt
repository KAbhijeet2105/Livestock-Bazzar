package com.devabhijeet.livestockbazaar.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devabhijeet.livestockbazaar.R
import kotlinx.android.synthetic.main.images_item.view.*

class AnimalImageAdapter(private val context: Context, private val images: List<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.images_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {

        val imageUrl = images[position]
        val imgUrl = imageUrl.toUri().buildUpon().scheme("https").build()

        Glide.with(context)
            .asBitmap()
            .load(imgUrl)
            .into(holder.itemView.rc_image_view)
    }

    override fun getItemCount(): Int = images.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}