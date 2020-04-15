package com.example.rubbishcommunity.ui.home.mine

import androidx.recyclerview.widget.DiffUtil
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ItemRecentMomentBinding
import com.example.rubbishcommunity.model.api.moments.MomentContent
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class RecentMomentsAdapter:BaseRecyclerAdapter<MomentContent, ItemRecentMomentBinding>(
	R.layout.item_recent_moment,
	{}
){
	override fun bindData(binding: ItemRecentMomentBinding, position: Int) {
		binding.moment = baseList[position]
	}
	
/*	override fun replaceData(newList: List<MomentContent>) {
		if(newList.isNotEmpty()){
			val diffResult = DiffUtil.calculateDiff(MomentContentDiffCallBack(baseList, newList), true)
			baseList = newList
			diffResult.dispatchUpdatesTo(this)
		}
	}*/
}

/*
class MomentContentDiffCallBack(
	val oldDatas:List<MomentContent>,
	val newDatas:List<MomentContent>
	
): DiffUtil.Callback() {
	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldDatas[oldItemPosition].javaClass == newDatas[newItemPosition].javaClass
	}
	
	override fun getOldListSize(): Int {
		return oldDatas.size
	}
	
	override fun getNewListSize(): Int {
		return newDatas.size
	}
	
	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		val oldData = oldDatas[oldItemPosition]
		val newData = newDatas[newItemPosition]
		return oldData == newData
	}
	
}*/
