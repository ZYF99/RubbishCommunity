package com.example.rubbishcommunity.ui.adapter

import android.view.View
import androidx.viewpager.widget.ViewPager

class PagerScaleTransformer : ViewPager.PageTransformer {
	
	/**
	 * Y方向最小缩放值
	 */
	private val MIN_SCALE_Y = 0.8f

	override fun transformPage(page:View, position:Float) {
		if(page.isEnabled){
			if (position >= 1 || position <= -1) {
				page.scaleY = MIN_SCALE_Y
			} else if (position < 0) {
				//  -1 < position < 0
				//View 在再从中间往左边移动，或者从左边往中间移动
				val scaleY = MIN_SCALE_Y + (1 + position) * (1 - MIN_SCALE_Y)
				page.scaleY = scaleY;
			} else {
				// 0 <= positin < 1
				//View 在从中间往右边移动 或者从右边往中间移动
				val scaleY = (1 - MIN_SCALE_Y) * (1 - position) + MIN_SCALE_Y
				page.scaleY = scaleY
			}
		}
	}
}
