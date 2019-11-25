package com.example.rubbishcommunity.ui.home.find.vote


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.VoteCellBinding
import com.example.rubbishcommunity.model.Vote
import com.example.rubbishcommunity.ui.adapter.BaseRecyclerAdapter

class VoteListAdapter(
	val list: MutableList<Vote>,
	onCellClick: (Int) -> Unit
) :
	BaseRecyclerAdapter<Vote, VoteCellBinding>(
		R.layout.cell_vote,
		onCellClick
	) {
	override val baseList: MutableList<Vote>
		get() = list
	
	override fun bindData(binding: VoteCellBinding, position: Int) {
		binding.vote = list[position]
	}
}