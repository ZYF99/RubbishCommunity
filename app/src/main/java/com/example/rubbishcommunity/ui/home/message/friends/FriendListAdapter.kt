package com.example.rubbishcommunity.ui.home.message.friends

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FriendListCellBinding
import com.example.rubbishcommunity.model.Person
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter


class FriendListAdapter(
	onCellClick: (Person) -> Unit
	) : BaseRecyclerAdapter<Person, FriendListCellBinding>(
	R.layout.cell_friend,
	onCellClick
) {
	
	override fun bindData(binding: FriendListCellBinding, position: Int) {
		binding.person = baseList[position]
	}
}
