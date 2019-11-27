package com.example.rubbishcommunity.ui.home.mine


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import com.example.rubbishcommunity.ui.utils.dp2px


class TransferHeaderBehavior(
	val context: Context,
	attrs: AttributeSet) :
	CoordinatorLayout.Behavior<ImageView>(context, attrs) {
	
	
	/**
	 * 原始X大小
	 */
	private var originalScaleX:Float = 0f
	
	/**
	 * 原始Y大小
	 */
	private var originalScaleY:Float = 0f
	
	/**
	 * 处于中心时候原始X轴
	 */
	private var originalX = 0
	
	/**
	 * 依附的 NestedScrollView 的原始Y轴坐标
	 */
	private var originalDependencyY = 0
	
	/**
	 * 自身View的原始Y轴坐标
	 */
	private var origionalY = 0
	
	/**
	 * 自身Y与依附的 NestedScrollView 的Y的比例
	 */
	var percentY:Float = 0f
	
	override fun layoutDependsOn(
		parent: CoordinatorLayout,
		child: ImageView,
		dependency: View
	): Boolean {
		return dependency is NestedScrollView
	}


	override fun onDependentViewChanged(
		parent: CoordinatorLayout,
		child: ImageView,
		dependency: View
	): Boolean {

		Log.d("~~NestedScrollView", "nestedScrollViewY" + dependency.y)
		
		
		if (originalScaleX == 0f) {
			// 计算X
			this.originalScaleX = child.scaleX
		}
		
		if (originalScaleY == 0f) {
			// 计算Y
			this.originalScaleY = child.scaleY
		}
		
		
		if (originalX == 0) {
			// 计算X轴坐标
			this.originalX = child.x.toInt() - child.width
		}

		if (originalDependencyY == 0) {
			// 计算依附的 NestedScrollView 的Y轴坐标
			originalDependencyY = dependency.y.toInt()
			Log.d("~~~~~", "originalDependencyY" + originalDependencyY+"\n")
		}
		
		if (origionalY == 0) {
			// 计算自身Y轴坐标
			origionalY = child.y.toInt()
			Log.d("~~~~~", "origionalY" + origionalY+"\n")
			
			percentY = origionalY.toFloat()/originalDependencyY.toFloat()
			
			Log.d("~~~~~", "percentY" + percentY+"\n")
		}
		
		
		val deltaY = originalDependencyY - dependency.y
		
		Log.d("~~~~~", "percentY" + percentY+"\n")
		
		Log.d("~~~~~", "deltaY" + deltaY+"\n")
	
		
		Log.d("~~~~~", "shouldDeltaY" + (percentY * deltaY)+"\n")
		
		
		
		
		var y = origionalY.toFloat() - (percentY * deltaY)
		if(y < context.dp2px(24f)){
			y = context.dp2px(24f).toFloat()
		}
		
		Log.d("~~~~~", "y" + y + "\n")
		
		child.y = y
		
		var deltaScale = deltaY/originalDependencyY
		if(deltaScale >= 0.2) deltaScale = 0.2f
		
		child.scaleX =  originalScaleX - deltaScale * originalScaleX
		child.scaleY =  originalScaleY - deltaScale * originalScaleY



		return true
	}
}