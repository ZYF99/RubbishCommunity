package com.example.rubbishcommunity.mainac.ui.guide.register

import android.annotation.SuppressLint
import android.view.View
import com.example.rubbishcommunity.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.RegisterFragBinding


class RegisterFragment : BindingFragment<RegisterFragBinding, RegisterViewModel>(
    RegisterViewModel::class.java, R.layout.frag_register
) {
    override fun initBefore() {


    }

    @SuppressLint("CheckResult")
    override fun initWidget(view: View) {
        binding.vm = viewModel
        

        viewModel.init()

/*        binding.gridHotword.run {
            val hotWordList = listOf("苹果","苹果","苹果","苹果","苹果","苹果","苹果","苹果")
            adapter = TagGridAdapter(getContext(),hotWordList)
        }*/




    }

    override fun initData() {

    }







}