package com.example.rubbishcommunity.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.abs


class DragBounceView : ConstraintLayout {
	private var downX: Float = 0.toFloat() //点击时的x坐标
	private var downY: Float = 0.toFloat()  // 点击时的y坐标
	
	private var originLeft: Int = 0 //点击时的绝对xleft坐标
	private var originTop: Int = 0  // 点击时的绝对top坐标
	private var originRight: Int = 0  // 点击时的绝对right坐标
	private var originBottom: Int = 0  // 点击时的绝对bottom坐标
	private var bounceDragListener:BounceDragListener? = null
	
	//是否拖动标识
	private var isDrag = false
	
	constructor(context: Context) : super(context)
	
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	
	constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
		context,
		attrs,
		defStyle
	)
	fun setBouncedragListener(bounceDragListener: BounceDragListener){
		this.bounceDragListener = bounceDragListener
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean {
		super.onTouchEvent(event)
		if (this.isEnabled) {
			when (event.action) {
				MotionEvent.ACTION_DOWN // 点击动作处理 每次点击时将拖动状态改为 false 并且记录下点击时的坐标 downX downY
				-> {
					isDrag = false
					downX = event.x // 点击触屏时的x坐标 用于离开屏幕时的x坐标作计算
					downY = event.y // 点击触屏时的y坐标 用于离开屏幕时的y坐标作计算
					originLeft = left
					originTop = top
					originRight = right
					originBottom = bottom
				}
				MotionEvent.ACTION_MOVE // 滑动动作处理 记录离开屏幕时的 moveX  moveY 用于计算距离 和 判断滑动事件和点击事件 并作出响应
				-> {
					val moveX = event.x - downX
					val moveY = event.y - downY
					var l: Int
					var r: Int
					var t: Int
					var b: Int // 上下左右四点移动后的偏移量
					//计算偏移量 设置偏移量 = 3 时 为判断点击事件和滑动事件的峰值
					if (abs(moveX) > 3 || abs(moveY) > 3) { // 偏移量的绝对值大于 3 为 滑动时间 并根据偏移量计算四点移动后的位置
						l = (left + moveX).toInt()
						r = l + width
						t = (top + moveY).toInt()
						b = t + height
						//不划出边界判断,最大值为边界值
						// 如果你的需求是可以划出边界 此时你要计算可以划出边界的偏移量 最大不能超过自身宽度或者是高度  如果超过自身的宽度和高度 view 划出边界后 就无法再拖动到界面内了 注意
						if (l < originLeft - 50) { // left 小于 0 就是滑出边界 赋值为 0 ; right 右边的坐标就是自身宽度 如果可以划出边界 left right top bottom 最小值的绝对值 不能大于自身的宽高
							l = originLeft - 50
							bounceDragListener?.onDragMaxTrigger()
						} else if (r > maxWidth) { // 判断 right 并赋值
							r = maxWidth
							l = r - width
						}
						if (t < 0) { // top
							t = 0
							b = t + height
						} else if (b > maxHeight) { // bottom
							b = maxHeight
							t = b - height
						}
						this.layout(l, originTop, originRight, originBottom) // 重置view在layout 中位置
						isDrag = true  // 重置 拖动为 true
					} else {
						isDrag = false // 小于峰值3时 为点击事件
					}
				}
				MotionEvent.ACTION_UP // 不处理
				-> {
					this.layout(originLeft, originTop, originRight, originBottom)
					bounceDragListener?.onDragBounceComplete()
					isPressed = false
				}
				MotionEvent.ACTION_CANCEL // 不处理
				-> isPressed = false
			}
			return true
		}
		return false
	}
	interface BounceDragListener{
		fun onDragMaxTrigger()
		fun onDragBounceComplete()
	}
}
