package com.example.rubbishcommunity.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View

import androidx.recyclerview.widget.RecyclerView

private const val TAG = "EmptyRecyclerView"

class EmptyRecyclerView : RecyclerView {
	private var emptyView: View? = null

	private val observer = object : RecyclerView.AdapterDataObserver() {
		override fun onChanged() {
			checkIfEmpty()
		}
		override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
			checkIfEmpty()
		}
		override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
			checkIfEmpty()
		}
	}

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(
		context: Context, attrs: AttributeSet,
		defStyle: Int
	) : super(context, attrs, defStyle)

	private fun checkIfEmpty() {
		if (emptyView != null && adapter != null) {
			val emptyViewVisible = adapter!!.itemCount == 0
			emptyView!!.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
			visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
		}
	}

	override fun setAdapter(adapter: Adapter<*>?) {
		val oldAdapter = getAdapter()
		oldAdapter?.unregisterAdapterDataObserver(observer)
		super.setAdapter(adapter)
		adapter?.registerAdapterDataObserver(observer)
		checkIfEmpty()
	}

	//设置没有内容时，提示用户的空布局
	fun setEmptyView(emptyView: View) {
		this.emptyView = emptyView
		checkIfEmpty()
	}

}
