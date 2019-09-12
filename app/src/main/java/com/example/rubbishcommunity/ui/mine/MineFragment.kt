package com.example.rubbishcommunity.ui.mine

import android.graphics.Color
import android.view.View
import com.bumptech.glide.Glide
import com.example.rubbishcommunity.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MineBinding

class MineFragment : BindingFragment<MineBinding, MineViewModel>(
    MineViewModel::class.java, R.layout.frag_mine
) {

    override fun initBefore() {

    }

    override fun initWidget(view: View) {

        binding.collapsingToolbarLayout.title = "欢迎您，zhangyf"
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK)
        binding.collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
        Glide.with(context).load(R.drawable.bg).crossFade().into(binding.iv)
        Glide.with(context).load(R.drawable.bg).crossFade().into(binding.potrait)
        
        
/*        RxView.clicks(binding.tvDate)
            .doOnNext {
                createDatePop()
            }.bindLife()*/


    }

    override fun initData() {

    }


/*    //create pop of jobPicker
    private fun createJobPop() {
        hideKeyboard()
        binding.btnjobdown.setImageResource(R.drawable.icn_chevron_down_black)
        val pop = context?.let { JobPopView(it) }
        pop?.show()
        //pop click listener
        pop?.setOnClickListener(object : BottomDialogView.OnMyClickListener {
            @SuppressLint("SetTextI18n")
            override fun onFinishClick() {
                binding.tvWork.text = pop.job
            }
        })

    }*/





}