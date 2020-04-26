package com.example.rubbishcommunity.ui.utils

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R

fun RecyclerView.addDecoration(){
	//添加自定义分割线
	val divider =
		DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
	divider.setDrawable(ContextCompat.getDrawable(this.context, R.drawable.custom_divider)!!)
	addItemDecoration(divider)
}