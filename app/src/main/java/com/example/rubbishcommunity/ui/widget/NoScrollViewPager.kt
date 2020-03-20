package com.example.rubbishcommunity.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class NoScrollViewPager : ViewPager {
	private var isCanScroll = false
	
	constructor(context: Context) : super(context) { // TODO Auto-generated constructor stub
	}
	
	constructor(context: Context, attrs: AttributeSet?) : super(
		context,
		attrs
	) { // TODO Auto-generated constructor stub
	}
	
	fun setScanScroll(isCanScroll: Boolean) {
		this.isCanScroll = isCanScroll
	}
	
	override fun scrollTo(x: Int, y: Int) {
		super.scrollTo(x, y)
	}
	
	override fun onTouchEvent(arg0: MotionEvent): Boolean { // TODO Auto-generated method stub
		return if (isCanScroll) {
			super.onTouchEvent(arg0)
		} else {
			false
		}
	}
	
	override fun setCurrentItem(
		item: Int,
		smoothScroll: Boolean
	) { // TODO Auto-generated method stub
		super.setCurrentItem(item, smoothScroll)
	}
	
	override fun setCurrentItem(item: Int) { // TODO Auto-generated method stub
		super.setCurrentItem(item)
	}
	
	override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean { // TODO Auto-generated method stub
		return if (isCanScroll) {
			super.onInterceptTouchEvent(arg0)
		} else {
			false
		}
	}
}