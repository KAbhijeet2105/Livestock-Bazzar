package com.devabhijeet.livestockbazaar.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class LBEditText(context: Context, attrs: AttributeSet): AppCompatEditText(context,attrs) {

    init {

        applyFont();
    }

    private fun applyFont()
    {
        val typeface: Typeface = Typeface.createFromAsset(context.assets,"mandali.ttf");
        setTypeface(typeface);
    }

}