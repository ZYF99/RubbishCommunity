package com.example.rubbishcommunity.ui.home.find

import android.annotation.SuppressLint
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FindBinding
import com.example.rubbishcommunity.ui.home.MainActivity
import com.example.rubbishcommunity.ui.home.find.moment.CLASSIFY_DYNAMIC
import com.example.rubbishcommunity.ui.home.find.moment.CLASSIFY_RECOVERY
import com.example.rubbishcommunity.ui.home.find.moment.MomentsFragment
import com.example.rubbishcommunity.ui.home.find.questiontest.QuestionTestFragment
import com.google.android.material.appbar.AppBarLayout

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
			val dynamicFragment = MomentsFragment(CLASSIFY_DYNAMIC)
			val recoveryFragment = MomentsFragment(CLASSIFY_RECOVERY)
			val fragList = listOf(
				Pair(dynamicFragment, "动态"), Pair(recoveryFragment, "回收"),
				Pair(QuestionTestFragment(), "头脑风暴")
			)
			adapter = FindPagerAdapter(childFragmentManager, fragList)
		}
		binding.tab.run {
			setupWithViewPager(binding.findpager)
		}
		
		//监听AppBar滑动隐藏下面的BottomNavigationView
		binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
			(activity as? MainActivity)?.viewModel?.onAppBarOffsetChanged(
				verticalOffset,
				appbarHeight = binding.appbar.height.toFloat() - binding.tab.height
			)
		})
		
	}
	
	override fun initData() {
	
	}
	
	
}