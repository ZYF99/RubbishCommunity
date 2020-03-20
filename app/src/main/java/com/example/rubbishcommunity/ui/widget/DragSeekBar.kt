package com.example.rubbishcommunity.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSeekBar


class DragSeekBar : AppCompatSeekBar {
	//这两个值为用算法使用的2空间复杂度
	private val index = 150
	private var k = true
	var onProgressMax: ((DragSeekBar?) -> Unit)? = null
	
	constructor(context: Context?) : super(context) {}
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
		context,
		attrs,
		defStyleAttr
	) {
	}
	
	override fun dispatchTouchEvent(event: MotionEvent): Boolean {
		val x = event.x.toInt()
		if (event.action == MotionEvent.ACTION_DOWN) {
			k = true
			if (x - index > 20) {
				k = false
				return true
			}
		}
		if (event.action == MotionEvent.ACTION_MOVE) {
			if (!k) {
				return true
			}
		}
		return super.dispatchTouchEvent(event)
	}

}