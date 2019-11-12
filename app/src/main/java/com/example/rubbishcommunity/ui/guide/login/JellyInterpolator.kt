package com.example.rubbishcommunity.ui.guide.login

import android.view.animation.LinearInterpolator
import kotlin.math.pow
import kotlin.math.sin

class JellyInterpolator : LinearInterpolator() {
	private val factor = 0.15f
	
	override fun getInterpolation(input: Float): Float {
		return ((2.toDouble().pow((-10 * input).toDouble())
				* sin((input - factor / 4) * (2 * Math.PI) / factor) + 1)).toFloat()
	}
	
	
}
