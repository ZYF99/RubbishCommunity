package com.example.rubbishcommunity.ui.message

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MessageBinding
import com.example.rubbishcommunity.ui.MainActivity

class MessageFragment : BindingFragment<MessageBinding, MessageViewModel>(
    MessageViewModel::class.java, R.layout.frag_message
) {

    override fun initBefore() {


    }

    override fun initWidget() {
        binding.vm = viewModel
        //viewModel.refreshing.observe { binding.refreshlayout.isRefreshing = it!! }

        viewModel.init()



        binding.recMessage.run {

            layoutManager = LinearLayoutManager(context)


            adapter = MessageListAdapter(R.layout.cell_msg, viewModel.messageList.value,viewModel)

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
                    (adapter as MessageListAdapter).replaceData(it)
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