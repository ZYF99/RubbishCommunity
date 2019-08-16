package com.example.rubbishcommunity.mainac.ui.homepage

import android.annotation.SuppressLint
import android.view.View
import com.example.rubbishcommunity.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.HomepageBinding


class HomepageFragment : BindingFragment<HomepageBinding, HomepageViewModel>(
    HomepageViewModel::class.java, R.layout.frag_homepage
) {
    override fun initBefore() {


    }

    @SuppressLint("CheckResult")
    override fun initWidget(view: View) {
        binding.vm = viewModel

        //viewModel.refreshing.observe { binding.refreshlayout.isRefreshing = it!! }

        viewModel.init()

        binding.gridHotword.run {
            val hotWordList = listOf("苹果","苹果","苹果","苹果","苹果","苹果","苹果","苹果")

            adapter = TagGridAdapter(getContext(),hotWordList)


        }




    }

    override fun initData() {

    }







}