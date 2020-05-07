package com.example.rubbishcommunity.ui.adapter

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.CellAddFriendBinding
import com.example.rubbishcommunity.model.api.SimpleProfileResp
import com.example.rubbishcommunity.model.api.message.UserMatchRelationRespListModel

class LikeFriendListAdapter(
	onCellClick: (UserMatchRelationRespListModel.UserMatchRelationModel) -> Unit,
	val onReceiveClick:(SimpleProfileResp)->Unit,
	val onRefuseClick:(SimpleProfileResp)->Unit
) : BaseRecyclerAdapter<UserMatchRelationRespListModel.UserMatchRelationModel, CellAddFriendBinding>(
	R.layout.cell_add_friend,
	onCellClick
) {
	
	override fun bindData(binding: CellAddFriendBinding, position: Int) {
		val person = baseList[position]
		binding.person = person
		binding.btnReceive.setOnClickListener {
			onReceiveClick(person.recipientProfile)
		}
		binding.btnRefuse.setOnClickListener {
			onRefuseClick(person.recipientProfile)
		}
	}
}