package com.devabhijeet.livestockbazaar.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devabhijeet.livestockbazaar.R
import java.io.IOException
import kotlin.math.max


/**
 * A custom object to create a common functions for Glide which can be used in whole application.
 */
class GlideLoader(val context: Context) {

    // TODO Step 3: Create a function to load image from URI for the user profile picture.
    // START
    /**
     * A function to load image from URI for the user profile picture.
     */
    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            // Load the user image in the ImageView.
            Glide
                .with(context)
                .load(image) // URI of the image
                .centerCrop() // Scale type of the image.
                .placeholder(R.drawable.ic_vector_account) // A default place holder if image is failed to load.
                .into(imageView) // the view in which the image will be loaded.
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    // END

    /**
     * A function to load image from Uri or URL for the product image.
     */
    fun loadAnimalPicture(image: Any, imageView: ImageView) {
        try {
            // Load the user image in the ImageView.
            Glide
                .with(context)
                .load(image) // Uri or URL of the image
                .centerCrop() // Scale type of the image.
                .into(imageView) // the view in which the image will be loaded.
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}



class DotsIndicatorDecoration(
    private val radius: Float,
    private val indicatorItemPadding: Float,
    private val indicatorHeight: Int,
    @ColorInt private val colorInactive: Int,
    @ColorInt private val colorActive: Int
) : RecyclerView.ItemDecoration() {

    private val inactivePaint = Paint()
    private val activePaint = Paint()

    init {
        val width = Resources.getSystem().displayMetrics.density * 1
        inactivePaint.apply {
            strokeCap = Paint.Cap.ROUND
            strokeWidth = width
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = colorInactive
        }

        activePaint.apply {
            strokeCap = Paint.Cap.ROUND
            strokeWidth = width
            style = Paint.Style.FILL_AND_STROKE
            isAntiAlias = true
            color = colorActive
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val adapter = parent.adapter ?: return

        val itemCount = adapter.itemCount

        val totalLength: Float = (radius * 2 * itemCount)
        val padBWItems = max(0, itemCount - 1) * indicatorItemPadding
        val indicatorTotalWidth = totalLength + padBWItems
        val indicatorStartX = (parent.width - indicatorTotalWidth) / 2F

        val indicatorPosY = parent.height - indicatorHeight / 2F

        drawInactiveDots(c, indicatorStartX, indicatorPosY, itemCount)

        val activePos: Int =
            (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (activePos == RecyclerView.NO_POSITION) {
            return
        }

        val activeChild =
            (parent.layoutManager as LinearLayoutManager).findViewByPosition(activePos)
                ?: return

        drawActiveDot(c, indicatorStartX, indicatorPosY, activePos)


    }

    private fun drawInactiveDots(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        itemCount: Int
    ) {
        val w = radius * 2 + indicatorItemPadding
        var st = indicatorStartX + radius
        for (i in 1..itemCount) {
            c.drawCircle(st, indicatorPosY, radius, inactivePaint)
            st += w
        }
    }

    private fun drawActiveDot(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        highlightPos: Int
    ) {
        val w = radius * 2 + indicatorItemPadding
        val highStart = indicatorStartX + radius + w * highlightPos
        c.drawCircle(highStart, indicatorPosY, radius, activePaint)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = indicatorHeight
    }

}
