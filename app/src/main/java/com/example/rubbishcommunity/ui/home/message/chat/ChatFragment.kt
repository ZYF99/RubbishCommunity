package com.example.rubbishcommunity.ui.home.message.chat

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ChatBinding
import com.example.rubbishcommunity.databinding.MessageBinding
import com.example.rubbishcommunity.ui.home.MainActivity
import com.example.rubbishcommunity.ui.home.message.MessageListAdapter
import com.example.rubbishcommunity.ui.home.message.MessageViewModel
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

class ChatFragment : BindingFragment<ChatBinding, ChatViewModel>(
	ChatViewModel::class.java, R.layout.fragment_chat
) {
	
	
	override fun initBefore() {
		viewModel.run { init((activity!!.intent.getSerializableExtra("openId")) as String) }
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		//viewModel.isRefreshing.observe { binding.refreshlayout.isRefreshing = it!! }
		binding.recChat.layoutManager = LinearLayoutManager(context)
		
		RxView.clicks(binding.btnSend)
			.throttleFirst(1, TimeUnit.SECONDS)
			.doOnNext {
				viewModel.sendStringMsg()
			}.bindLife()
		
		
		viewModel.chatList.observeNonNull {
			binding.recChat.run {
				adapter = viewModel.chatList.value?.let { ChatListAdapter(context, it) }
			}
		}
		
		
	}
	
	override fun initData() {
	
	}


/*    //create pop of jobPicker
    private fun createJobPop() {
        hideKeyboard()
        binding.btnjobdown.setImageResource(R.drawable.icn_chevron_down_black)
        val pop = context?.let { ContractDialog(it) }
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