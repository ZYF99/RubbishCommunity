package com.example.rubbishcommunity.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.drawerlayout.widget.DrawerLayout


class CustomDrawerLayout : DrawerLayout {
	
	constructor(context: Context) : super(context) {}
	
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
	
	constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
		context,
		attrs,
		defStyle
	)
	
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		var widthMeasureSpec = widthMeasureSpec
		var heightMeasureSpec = heightMeasureSpec
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(
			MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY
		)
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(
			MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY
		)
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
	}
	
}