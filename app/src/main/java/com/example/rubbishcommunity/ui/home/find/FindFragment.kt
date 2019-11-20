package com.example.rubbishcommunity.ui.home.find

import android.annotation.SuppressLint
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FindBinding
import com.example.rubbishcommunity.ui.home.find.dynamic.DynamicFragment
import com.example.rubbishcommunity.ui.home.find.vote.VoteFragment


class FindFragment :
	BindingFragment<FindBinding, FindViewModel>(
		FindViewModel::class.java, R.layout.fragment_find
	) {
	
	
	override fun initBefore() {
	}
	
	@SuppressLint("CheckResult")
	override fun initWidget() {
		binding.vm = viewModel
		binding.findpager.run {
			val dynamicFragment = DynamicFragment()
			val voteFragment = VoteFragment()
			val fragList = listOf(Pair(dynamicFragment, "动态"), Pair(voteFragment, "投票"))
			adapter = FindPagerAdapter(childFragmentManager, fragList)
		}
		binding.tab.run {
			setupWithViewPager(binding.findpager)
		}
		
		
	}
	
	override fun initData() {
	
	}
	
	
}