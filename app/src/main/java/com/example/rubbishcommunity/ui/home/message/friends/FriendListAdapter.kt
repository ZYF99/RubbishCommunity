package com.example.rubbishcommunity.ui.home.message.friends

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FriendListCellBinding
import com.example.rubbishcommunity.model.Person
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter


class FriendListAdapter(
	val list: MutableList<Person>,
	onCellClick: (Int) -> Unit
	) : BaseRecyclerAdapter<Person, FriendListCellBinding>(
	R.layout.cell_friend,
	onCellClick
) {
	override val baseList: MutableList<Person> = list
	
	override fun bindData(binding: FriendListCellBinding, position: Int) {
		binding.person = list[position]
	}
}
