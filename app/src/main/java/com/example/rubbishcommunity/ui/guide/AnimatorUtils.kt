package com.example.rubbishcommunity.ui.guide

import android.animation.*
import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import com.example.rubbishcommunity.ui.guide.login.JellyInterpolator

//动画相关
class AnimatorUtils(
	private val leftMargin: Int,
	private val rightMargin: Int,
	private val progressView: View,
	private val transView: View,
	private val btnReact: View


) {
	@SuppressLint("ObjectAnimatorBinding")
	public fun startTransAnimation() {
		val set = AnimatorSet()
		val animator = ValueAnimator.ofFloat(0F, transView.measuredWidth.toFloat())
		animator.addUpdateListener {
			val value = it.animatedValue as Float
			val params = transView
				.layoutParams as ViewGroup.MarginLayoutParams
			params.leftMargin = leftMargin
			params.rightMargin = rightMargin
			transView.layoutParams = params
		}
		
		
		val animator2 = ObjectAnimator.ofFloat(
			transView,
			"scaleX", 1f, 0f
		)
		set.duration = 600
		set.interpolator = AccelerateDecelerateInterpolator()
		set.playTogether(animator, animator2)
		set.start()
		
		set.addListener(onEnd = {
			/**
			 * 动画结束后，先显示加载的动画，然后再隐藏输入框
			 */
			progressView.visibility = View.VISIBLE
			progressAnimator(progressView)
			transView.visibility = View.INVISIBLE
			
		})
	}
	
	private fun progressAnimator(view: View) {
		val animator = PropertyValuesHolder.ofFloat(
			"scaleX",
			0.5f, 1f
		)
		val animator2 = PropertyValuesHolder.ofFloat(
			"scaleY",
			0.5f, 1f
		)
		val animator3 = ObjectAnimator.ofPropertyValuesHolder(
			view,
			animator, animator2
		)
		animator3.duration = 800
		animator3.interpolator = JellyInterpolator()
		animator3.start()
		
	}
	
	public fun complete() {
		progressView.visibility = View.GONE
		transView.visibility = View.VISIBLE
		
		btnReact.isEnabled = true
		
		val params = transView.layoutParams as ViewGroup.MarginLayoutParams
		params.leftMargin = leftMargin
		params.rightMargin = rightMargin
		transView.layoutParams = params
		
		
		val animator2 = ObjectAnimator.ofFloat(transView, "scaleX", 0.5f, 1f)
		animator2.duration = 200
		animator2.interpolator = AccelerateDecelerateInterpolator()
		animator2.start()
	}
	
	
}

