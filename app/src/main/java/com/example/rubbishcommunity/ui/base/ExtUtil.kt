package com.example.rubbishcommunity.ui.base

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.setOnLoadMoreListener(action: () -> Unit) {
	
	addOnScrollListener(object : RecyclerView.OnScrollListener() {
		override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
			if (!recyclerView.canScrollVertically(1))
				action.invoke()
		}
	})
	
}

fun isVisBottom(recyclerView: RecyclerView): Boolean {
	val layoutManager =
		recyclerView.layoutManager as LinearLayoutManager?
	//屏幕中最后一个可见子项的position
	val lastVisibleItemPosition = layoutManager!!.findLastVisibleItemPosition()
	//当前屏幕所看到的子项个数
	val visibleItemCount = layoutManager.childCount
	//当前RecyclerView的所有子项个数
	val totalItemCount = layoutManager.itemCount
	//RecyclerView的滑动状态
	val state = recyclerView.scrollState
	return visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == RecyclerView.SCROLL_STATE_IDLE
}















