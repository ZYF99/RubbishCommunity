package com.example.rubbishcommunity.ui.home.find.questiontest

import android.view.View
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FragmentQuestionTestBinding
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class QuestionTestFragment : BindingFragment<FragmentQuestionTestBinding, QuestionTestViewModel>(
	QuestionTestViewModel::class.java,
	R.layout.fragment_question_test
) {
	override fun initBefore() {
		binding.vm = viewModel
	}
	
	override fun initWidget() {
		binding.pagerTest.adapter = TestPagerAdapter(
			emptyList(),
			onAnswerCorrect = { position ->
				//答对后
				val pager = binding.pagerTest
				Single.timer(2, TimeUnit.SECONDS).doOnSuccess {
					if (pager.currentItem != viewModel.pagerList.value?.size ?: 0 - 1)
						pager.setCurrentItem(position + 1, true)
				}.bindLife()
			},
			onAnswerError = { position ->
				//答错后
			}
		)
		binding.flTip.setOnClickListener { it.visibility = View.GONE }
		viewModel.refreshing.observeNonNull { isRefreshing ->
			binding.refreshLayout.isRefreshing = isRefreshing
		}
		
		viewModel.pagerList.observeNonNull {
			(binding.pagerTest.adapter as TestPagerAdapter).replaceData(it)
		}
		
		binding.refreshLayout.setOnRefreshListener {
			viewModel.fetchTestList()
		}
		
		
	}
	
	override fun initData() {
		viewModel.fetchTestList()
	}
	
	
}