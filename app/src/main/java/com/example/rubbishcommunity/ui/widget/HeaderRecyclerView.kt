package com.example.rubbishcommunity.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class HeaderRecyclerView : RecyclerView {
	
	private val observer = HeaderRecyclerDataObserver(1)
	
	constructor(context: Context) : super(context)
	
	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
	
	constructor(
		context: Context, attrs: AttributeSet,
		defStyle: Int
	) : super(context, attrs, defStyle)

/*	private fun checkIfEmpty() {
		if (emptyView != null && adapter != null) {
			val emptyViewVisible = adapter!!.itemCount == 0
			emptyView!!.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
			visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
		}
	}*/
	
	override fun setAdapter(adapter: Adapter<*>?) {
		getAdapter()?.unregisterAdapterDataObserver(observer)
		super.setAdapter(adapter)
		adapter?.registerAdapterDataObserver(observer)
	}

/*	//设置没有内容时，提示用户的空布局
	fun setEmptyView(emptyView: View) {
		this.emptyView = emptyView
		checkIfEmpty()
	}*/
	
}

class HeaderRecyclerDataObserver(
	val headerSize: Int
) : RecyclerView.AdapterDataObserver() {
	override fun onChanged() {
		super.onChanged()
	}
	
	override fun onItemRangeChanged(positionStart: Int, itemCount: Int) { // do nothing
		super.onItemRangeChanged(positionStart + headerSize, itemCount)
	}
	
	override fun onItemRangeChanged(
		positionStart: Int,
		itemCount: Int,
		payload: Any?
	) {
		onItemRangeChanged(positionStart, itemCount)
	}
	
	override fun onItemRangeInserted(
		positionStart: Int,
		itemCount: Int
	) {
		super.onItemRangeInserted(positionStart + headerSize, itemCount)
	}
	
	override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) { // do nothing
		super.onItemRangeInserted(positionStart + headerSize, itemCount)
	}
	
	override fun onItemRangeMoved(
		fromPosition: Int,
		toPosition: Int,
		itemCount: Int
	) {
		super.onItemRangeMoved(fromPosition + headerSize, toPosition + headerSize, itemCount)
	}
}
