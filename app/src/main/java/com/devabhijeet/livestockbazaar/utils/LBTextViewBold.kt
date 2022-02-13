package com.devabhijeet.livestockbazaar.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class LBTextViewBold(context: Context, attrs: AttributeSet): AppCompatTextView(context,attrs) {

    init {
        applyFont();
    }

    private fun applyFont(){

        val typeface: Typeface = Typeface.createFromAsset(context.assets,"mandali.ttf");
        setTypeface(typeface,BOLD);
    }


}