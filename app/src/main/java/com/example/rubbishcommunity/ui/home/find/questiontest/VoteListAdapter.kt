package com.example.rubbishcommunity.ui.home.find.questiontest


import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.VoteCellBinding
import com.example.rubbishcommunity.model.Vote
import com.example.rubbishcommunity.ui.adapter.EmptyRecyclerAdapter

class VoteListAdapter(
	onCellClick: (Vote) -> Unit
) : EmptyRecyclerAdapter<Vote, VoteCellBinding>(
	R.layout.cell_vote,
	onCellClick
) {

	
	override fun bindData(binding: VoteCellBinding, position: Int) {
		binding.vote = baseList[position]
	}
}