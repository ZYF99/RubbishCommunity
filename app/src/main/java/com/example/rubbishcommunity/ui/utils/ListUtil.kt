package com.example.rubbishcommunity.ui.utils

fun  <T>List<T>.equalsList(newList: List<T>): Boolean {
	if (this.size != newList.size)
		return false
	val pairList = this.zip(newList)
	return pairList.all { (elt1, elt2) ->
		elt1 == elt2
	}
}