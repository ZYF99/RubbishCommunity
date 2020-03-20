package com.example.rubbishcommunity.ui.home.find.questiontest

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FragmentQuestionTestBinding
import io.reactivex.Completable
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
		//设置题卡适配器
		binding.pagerTest.adapter = TestPagerAdapter(
			emptyList(),
			onAnswerCorrect = { position ->
				//答对后
				val pager = binding.pagerTest
				Completable.timer(2, TimeUnit.SECONDS).doOnComplete {
					if (pager.currentItem != viewModel.pagerList.value?.size ?: 0 - 1) //不是最后一张
						pager.setCurrentItem(position + 1, true)
					else //是最后一张
						AlertDialog.Builder(context).setMessage("本轮答题结束，是否开启新的一轮答题").setPositiveButton(
							"开启"
						) { _, _ ->
							viewModel.fetchTestList()
						}
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