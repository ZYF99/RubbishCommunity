package com.example.rubbishcommunity.mainac.ui.message

import android.view.View
import com.example.rubbishcommunity.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MessageBinding

class MessageFragment : BindingFragment<MessageBinding, MessageViewModel>(
    MessageViewModel::class.java, R.layout.frag_message
) {

    override fun initBefore() {

    }

    override fun initWidget(view: View) {

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