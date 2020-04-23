package com.example.rubbishcommunity.ui.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.ui.utils.dp2px

class LoadingView(
	context: Context,
	attributeSet: AttributeSet
) : LinearLayout(
	context,
	attributeSet
) {
	
	val CIRCLE_RADIUS = 8f
	val MARGIN_START = 12f
	val ONECE_DURATION: Long = 1
	val DIRECTION_FOREGROUND = 0
	val DIRECTION_BACK = 1
	
	var circle1: View? = null
	var circle2: View? = null
	var circle3: View? = null
	var circle4: View? = null
	var circle5: View? = null
	var circle6: View? = null
	
	//var direction = DIRECTION_FOREGROUND
	
	init {
		initView()
	}
	
	private fun initView() {
		
		
		LayoutInflater.from(context).inflate(R.layout.loading_view, this, true)
		val ll = getChildAt(0) as LinearLayout
		circle1 = ll.getChildAt(0)
		circle2 = ll.getChildAt(1)
		circle3 = ll.getChildAt(2)
		circle4 = ll.getChildAt(3)
		circle5 = ll.getChildAt(4)
		circle6 = ll.getChildAt(5)
		
		val animatorSet = AnimatorSet()
		
		
		val rotateAnimatorSet1 = AnimatorSet()
		val rotateAnimatorSet2 = AnimatorSet()
		
		
		val aniSet1 = AnimatorSet()
		aniSet1.apply {
			playTogether(
				circle1?.translateX(DIRECTION_FOREGROUND),
				rotateAnimatorSet1.apply {
					playSequentially(
						circle2?.rotateAroundPoint(DIRECTION_FOREGROUND),
						circle3?.rotateAroundPoint(DIRECTION_FOREGROUND),
						circle4?.rotateAroundPoint(DIRECTION_FOREGROUND),
						circle5?.rotateAroundPoint(DIRECTION_FOREGROUND),
						circle6?.rotateAroundPoint(DIRECTION_FOREGROUND)
					)
				}
			)
		}
		
		val aniSet2 = AnimatorSet()
		aniSet2.apply {
			playTogether(
				circle1?.translateX(DIRECTION_BACK),
				rotateAnimatorSet2.apply {
					playSequentially(
						circle6?.rotateAroundPoint(DIRECTION_BACK),
						circle5?.rotateAroundPoint(DIRECTION_BACK),
						circle4?.rotateAroundPoint(DIRECTION_BACK),
						circle3?.rotateAroundPoint(DIRECTION_BACK),
						circle2?.rotateAroundPoint(DIRECTION_BACK)
					)
				}
			)
		}
		
		animatorSet.apply {
			playSequentially(
				aniSet1,
				aniSet2
			)
			repeat(Int.MAX_VALUE) {}
			start()
		}
		
	}
	
	private fun View.translateX(directionTemp: Int): ValueAnimator {
		val startX = this.x
		val deltaX = context.dp2px(5 * MARGIN_START + 10 * CIRCLE_RADIUS)
		
		return ObjectAnimator.ofFloat(
			this,
			"translationX",
			if (directionTemp == DIRECTION_FOREGROUND) startX else startX + deltaX.toFloat(),
			if (directionTemp == DIRECTION_FOREGROUND) startX + deltaX.toFloat() else startX
		).apply {
			duration = ONECE_DURATION * 5 * 1000
		}
	}
	
	
	private fun View.rotateAroundPoint(directionTemp: Int): ObjectAnimator {
		this.pivotX =
			if (directionTemp == DIRECTION_FOREGROUND) this.x - context.dp2px(MARGIN_START) / 2 else this.x - context.dp2px(
				MARGIN_START
			) / 2
		this.pivotY = this.y + context.dp2px(CIRCLE_RADIUS)
		val rotateAnimator = ObjectAnimator.ofFloat(
			this,
			"rotation",
			if (directionTemp == DIRECTION_FOREGROUND) 0f else 180f,
			if (directionTemp == DIRECTION_FOREGROUND) -180f else 0f
		)
		rotateAnimator.duration = ONECE_DURATION * 1000 //持续时间
		//rotateAnimator.repeatCount = 0 //循环次数（-1 为无限循环）
		
		return rotateAnimator
	}
	
	
}