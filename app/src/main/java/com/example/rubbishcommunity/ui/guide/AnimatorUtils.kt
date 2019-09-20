package com.example.rubbishcommunity.ui.guide

import android.animation.*
import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import com.example.rubbishcommunity.ui.guide.login.JellyInterpolator
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

//动画相关
class AnimatorUtils(
	private val leftMargin: Int,
	private val rightMargin: Int,
	private val progressView: View,
	private val transView: View,
	private val btnReact: View


) {
	@SuppressLint("ObjectAnimatorBinding")
	fun startTransAnimation() {
		btnReact.isEnabled = false
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
		val animator3 = ObjectAnimator.ofFloat(
			transView,
			"scaleY", 1f, 0f
		)
		
		set.duration = 100
		set.interpolator = AccelerateDecelerateInterpolator()
		set.playTogether(animator, animator2, animator3)
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
		animator3.duration = 100
		animator3.interpolator = JellyInterpolator()
		animator3.start()
		
	}
	
	fun complete() {
		
		Single.just(1).doOnSuccess {
			
			progressView.visibility = View.GONE
			transView.visibility = View.VISIBLE
			
			btnReact.isEnabled = true
			
			val params = transView.layoutParams as ViewGroup.MarginLayoutParams
			params.leftMargin = leftMargin
			params.rightMargin = rightMargin
			transView.layoutParams = params
			
			
			val completedAnimator = ObjectAnimator.ofPropertyValuesHolder(
				transView,
				PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f),
				PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f)
			)
			
			completedAnimator.duration = 100
			completedAnimator.interpolator = AccelerateDecelerateInterpolator()
			completedAnimator.start()
			
		}.subscribeOn(AndroidSchedulers.mainThread()).subscribe()
		
		
	}
	
	
}

