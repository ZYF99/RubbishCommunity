package com.example.rubbishcommunity.ui.base

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setOnLoadMoreListener(action:()->Unit){
		addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				action.invoke()
			}
		})
	
}