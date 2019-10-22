package com.example.rubbishcommunity.ui.release.dynamic

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

class MyGridLayoutManager(context: Context, spanCount: Int) :
	GridLayoutManager(context, spanCount) {

	private val measuredSize = IntArray(2)

	override fun onMeasure(
		recycler: RecyclerView.Recycler,
		state: RecyclerView.State,
		widthSpec: Int,
		heightSpec: Int
	) {
		val widthMode = View.MeasureSpec.getMode(widthSpec)
		val heightMode = View.MeasureSpec.getMode(heightSpec)
		val widthSize = View.MeasureSpec.getSize(widthSpec)
		val heightSize = View.MeasureSpec.getSize(heightSpec)

		var spanWidth = 0
		var spanHeight = 0

		var viewWidth = 0
		var viewHeight = 0

		val spanCount = spanCount

		for (i in 0 until state.itemCount) {

			measureScrapChild(
				recycler,
				i,
				View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
				measuredSize
			)

			if (i % spanCount == 0) {
				spanWidth = measuredSize[0]
				spanHeight = measuredSize[1]
			} else {
				if (orientation == LinearLayoutManager.VERTICAL) {
					spanWidth += measuredSize[0]
					spanHeight = max(spanHeight, measuredSize[1])
				} else {
					spanWidth = max(spanWidth, measuredSize[0])
					spanHeight += measuredSize[1]
				}
			}

			if (i % spanCount == spanCount - 1 || i == itemCount - 1) {
				if (orientation == LinearLayoutManager.VERTICAL) {
					viewWidth = max(viewWidth, spanWidth)
					viewHeight += spanHeight
				} else {
					viewWidth += spanWidth
					viewHeight = max(viewHeight, spanHeight)
				}
			}
		}

		val finalWidth: Int
		val finalHeight: Int
		
		finalWidth = when (widthMode) {
			View.MeasureSpec.EXACTLY -> widthSize
			View.MeasureSpec.AT_MOST -> min(widthSize, viewWidth)
			View.MeasureSpec.UNSPECIFIED -> viewWidth
			else -> widthSize
		}
		
		finalHeight = when (heightMode) {
			View.MeasureSpec.EXACTLY -> heightSize
			View.MeasureSpec.AT_MOST -> min(heightSize, viewHeight)
			View.MeasureSpec.UNSPECIFIED -> viewHeight
			else -> heightSize
		}

		setMeasuredDimension(finalWidth, finalHeight)
	}


	private fun measureScrapChild(
		recycler: RecyclerView.Recycler,
		position: Int,
		widthSpec: Int,
		heightSpec: Int,
		measuredDimension: IntArray
	) {

		val view = recycler.getViewForPosition(position)
		
		val p = view.layoutParams as RecyclerView.LayoutParams
		
		val childWidthSpec =
			ViewGroup.getChildMeasureSpec(widthSpec, paddingLeft + paddingRight, p.width)
		val childHeightSpec =
			ViewGroup.getChildMeasureSpec(heightSpec, paddingTop + paddingBottom, p.height)
		
		view.measure(childWidthSpec, childHeightSpec)
		
		measuredDimension[0] = view.measuredWidth + p.leftMargin + p.rightMargin
		measuredDimension[1] = view.measuredHeight + p.bottomMargin + p.topMargin
		
		val decoratorRect = Rect()
		calculateItemDecorationsForChild(view, decoratorRect)
		measuredDimension[0] += decoratorRect.left
		measuredDimension[0] += decoratorRect.right
		measuredDimension[1] += decoratorRect.top
		measuredDimension[1] += decoratorRect.bottom
		
		recycler.recycleView(view)
	}
	
	override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
		try {
			super.onLayoutChildren(recycler, state);
		} catch (e:IndexOutOfBoundsException) {
			Log.e("probe", "meet a IOOBE in RecyclerView");
		}
		super.onLayoutChildren(recycler, state)
	}
	
}