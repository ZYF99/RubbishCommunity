package com.example.rubbishcommunity.ui.home.message

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MessageBinding
import com.example.rubbishcommunity.ui.home.MainActivity

class MessageFragment : BindingFragment<MessageBinding, MessageViewModel>(
	MessageViewModel::class.java, R.layout.fragment_message
) {
	
	override fun initBefore() {
	
	
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		//viewModel.isRefreshing.observe { binding.refreshlayout.isRefreshing = it!! }
		
		viewModel.init()
		
		
		
		binding.recMessage.run {
			
			layoutManager = LinearLayoutManager(context)
			
			
			//adapter = MessageListAdapter(R.layout.cell_msg, viewModel.messageList.value,viewModel)
			adapter = viewModel.messageList.value?.let { RecyclerViewAdapter(context, it) }
			
		}
		
		
		
		
		binding.refreshLayout.setOnRefreshListener {
			when {
				!isNetworkAvailable() -> {
					(activity as MainActivity).showNetErrorSnackBar()
					viewModel.refreshing.postValue(false)
				}
				else -> {
					viewModel.getMessageList()
				}
			}
		}
		
		
		
		
		viewModel.messageList.observe {
			binding.recMessage.run {
				if (it != null) {
					// (adapter as MessageListAdapter).replaceData(it)
				}
			}
		}
		
		viewModel.refreshing.observe { isRefreshing ->
			binding.refreshLayout.run {
				if (!isRefreshing!!) finishRefresh()
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