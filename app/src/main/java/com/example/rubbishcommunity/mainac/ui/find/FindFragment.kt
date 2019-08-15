package com.example.rubbishcommunity.mainac.ui.find

import android.annotation.SuppressLint
import android.view.View
import com.example.rubbishcommunity.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.mainac.ui.find.dynamic.DynamicFragment
import com.example.rubbishcommunity.mainac.ui.find.dynamic.FindViewModel
import com.example.rubbishcommunity.mainac.ui.find.vote.VoteFragment


class FindFragment : BindingFragment<com.example.rubbishcommunity.databinding.FindBinding, FindViewModel>(
    FindViewModel::class.java, R.layout.frag_find
) {


    override fun initBefore() {
    }

    @SuppressLint("CheckResult")
    override fun initWidget(view: View) {

        binding.vm = viewModel

        // viewModel.refreshing.observe { binding.refreshlayout. = it!! }

        viewModel.init()

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