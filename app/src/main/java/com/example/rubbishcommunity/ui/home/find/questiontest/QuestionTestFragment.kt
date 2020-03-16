package com.example.rubbishcommunity.ui.home.find.questiontest

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FragmentQuestionTestBinding

class QuestionTestFragment : BindingFragment<FragmentQuestionTestBinding, QuestionTestViewModel>(
	QuestionTestViewModel::class.java,
	R.layout.fragment_question_test
) {
	override fun initBefore() {
	
	
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		
		//viewModel.isLoading.observe { binding.refreshlayout.isLoading = it!! }
		
		viewModel.init()
		
		binding.recVote.run {
			layoutManager = LinearLayoutManager(context)
			adapter = VoteListAdapter {
			
			}.apply { replaceData(viewModel.voteList.value!!) }
		}
		
		
		binding.refreshLayout.setOnRefreshListener {
			context!!.checkNet().doOnComplete {
				viewModel.getVoteList()
			}.doOnError {
				showNetErrorSnackBar()
				viewModel.refreshing.postValue(false)
			}.bindLife()
		}
		
		
		viewModel.voteList.observe {
			binding.recVote.run {
				(adapter as VoteListAdapter).replaceData(it!!)
			}
		}
		
		viewModel.refreshing.observe { isRefreshing ->
			binding.refreshLayout.run {
				if (!isRefreshing!!) finishRefresh()
			}
		}
		
		
	}
	
	override fun initData() {
	
	}
	
	
}