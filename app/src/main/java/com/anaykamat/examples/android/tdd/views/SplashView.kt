package com.anaykamat.examples.android.tdd.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.anaykamat.examples.android.tdd.R

/**
 * Created by anay on 07/08/18.
 */
class SplashView:FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)

    init {
        android.view.LayoutInflater.from(context).inflate(R.layout.view_splash, this, true)
        this.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        this.setBackgroundColor(0x0099cc)
    }


}