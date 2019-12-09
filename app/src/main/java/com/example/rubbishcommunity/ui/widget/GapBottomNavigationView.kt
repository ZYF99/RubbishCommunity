//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.rubbishcommunity.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Build.VERSION
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuBuilder.Callback
import androidx.appcompat.widget.TintTypedArray
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.customview.view.AbsSavedState
import com.google.android.material.R.attr
import com.google.android.material.R.color
import com.google.android.material.R.dimen
import com.google.android.material.R.style
import com.google.android.material.R.styleable
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationPresenter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ThemeEnforcement
import kotlin.math.sqrt

@SuppressLint("RestrictedApi")
class GapBottomNavigationView : BottomNavigationView {
	
	
	constructor(context: Context) : super(context)
	
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
		context,
		attrs,
		defStyleAttr
	)
	
	
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		
		val centerRadius = height * 3 / 4
		val shadowLength = 5f
		
		val cornerRadius = 16f
		
		val paint = Paint()
		
		paint.isAntiAlias = true
		
		
		val path = Path()
		
		//左边的半圆
		val rectL = RectF(0.toFloat(), 0.toFloat(), height.toFloat(), height.toFloat())
		path.arcTo(rectL, 90.toFloat(), 180.toFloat(), false)
		path.lineTo(width.toFloat() / 2 - centerRadius, 0.toFloat())
		
		
		path.quadTo(
			width/2-centerRadius + cornerRadius,
			0.toFloat(),
			width/2-centerRadius + cornerRadius + cornerRadius/ sqrt(2.toFloat()),
			cornerRadius/ sqrt(2.toFloat())
		)
		
		//中间凹陷的半圆
		
		path.quadTo(
			width.toFloat() / 2,
			height.toFloat(),
			width/2 + centerRadius - cornerRadius - cornerRadius/ sqrt(2.toFloat()),
			cornerRadius/ sqrt(2.toFloat())
		)
		
		path.quadTo(
			width/2 + centerRadius - cornerRadius,
			0.toFloat(),
			width/2 + centerRadius - cornerRadius + cornerRadius/ sqrt(2.toFloat()),
			0.toFloat()
		)
		
		
		path.lineTo((width - height / 2).toFloat(), 0.toFloat())
		
		
		//右边的半圆
		val rectR = RectF(width.toFloat() - height, 0.toFloat(), width.toFloat(), height.toFloat())
		path.arcTo(rectR, 270.toFloat(), 180.toFloat(), false)
		
		//最后直线
		path.moveTo((width - height / 2).toFloat(), height.toFloat())
		path.lineTo(height / 2.toFloat(), height.toFloat())
		
		path.close()
		
		//填充白色
		paint.style = Paint.Style.FILL
		paint.color = Color.WHITE
		paint.strokeWidth = 2f
		paint.maskFilter = null
		paint.strokeCap = Paint.Cap.ROUND
		//paint.pathEffect = CornerPathEffect(30.toFloat())
		
		canvas.drawPath(path, paint)


/*		//画阴影
		outlineProvider = object : ViewOutlineProvider() {
			override fun getOutline(view: View?, outline: Outline?) {
				outline?.setConvexPath(path)
			}
		}*/
		
		
	}
	
}
