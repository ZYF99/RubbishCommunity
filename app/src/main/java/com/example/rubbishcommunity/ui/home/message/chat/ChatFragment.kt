package com.example.rubbishcommunity.ui.home.message.chat

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.NotifyMessageOutClass
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ChatBinding
import com.example.rubbishcommunity.model.api.SimpleProfileResp
import com.example.rubbishcommunity.service.MQNotifyData
import com.example.rubbishcommunity.ui.utils.hideSoftKeyBoard
import com.example.rubbishcommunity.ui.utils.openSoftKeyBoard
import com.example.rubbishcommunity.utils.globalGson
import com.jakewharton.rxbinding2.view.RxView
import me.everything.android.ui.overscroll.IOverScrollState.*
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter
import java.util.concurrent.TimeUnit

class ChatFragment : BindingFragment<ChatBinding, ChatViewModel>(
	ChatViewModel::class.java, R.layout.fragment_chat
) {
	
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
		binding.consContent.scrollTo(0, keyboardHeightInPx)
	}
	
	override fun onSoftKeyboardClosed() {
		hideInputLin()
	}
	
	override fun initBefore() {
		viewModel.receiver.value = globalGson.getAdapter(SimpleProfileResp::class.java).fromJson((activity!!.intent.getSerializableExtra("userProfile")) as String)
		viewModel.init()
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		//viewModel.isLoading.observe { binding.refreshlayout.isLoading = it!! }
		binding.recChat.run {
			layoutManager = LinearLayoutManager(context)
			adapter = ChatListAdapter(viewModel.chatList.value!!) {
			
			}
			
			setOnScrollListener(object : RecyclerView.OnScrollListener() {
				override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
					super.onScrolled(recyclerView, dx, dy)
					if (dy < 0) {
						//手指向下滑动，显示上方内容，隐藏输入
						hideInputLin()
					}
				}
			})
			
			VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(this)).setOverScrollStateListener { decor, oldState, newState ->
				//recyclerView弹性滑动（过度滑动）的监听
				when (newState) {
					STATE_IDLE -> {
						//没有过度滚动
					}
					STATE_DRAG_START_SIDE -> {
						//从上部（左部）开始拖动
						hideInputLin()
					}
					STATE_DRAG_END_SIDE -> {
						//从下部（右部）开始拖动
						//showInputLin()
					}
					STATE_BOUNCE_BACK -> {
						if (oldState == STATE_DRAG_START_SIDE) {
							//拖动停止-视图开始从*左端*弹回自然位置。
						} else { // i.e. (oldState == STATE_DRAG_END_SIDE)
							//视图开始从“右端”反弹。
							showInputLin()
						}
					}
				}
			}
		}
		
		RxView.clicks(binding.btnSend)
			.throttleFirst(1, TimeUnit.SECONDS)
			.doOnNext {
				viewModel.sendStringMsg()
			}.bindLife()
		
		viewModel.chatList.observeNonNull { list ->
			binding.recChat.run {
				(adapter as ChatListAdapter).replaceData(list)
				scrollToPosition(list.size - 1)
			}
		}
		
		
	}
	
	override fun initData() {
	
	}
	
	
	private fun showInputLin() {
		if (!mManager.isSoftKeyboardOpened) {
			activity!!.openSoftKeyBoard(binding.editMsg)
		}
	}
	
	private fun hideInputLin() {
		if (mManager.isSoftKeyboardOpened) {
			activity!!.hideSoftKeyBoard()
		}
		binding.consContent.scrollTo(0, 0)
	}
	
	override fun onMQMessageArrived(mqNotifyData: MQNotifyData) {
		when(mqNotifyData.mqNotifyType){
			NotifyMessageOutClass.NotifyType.SYNC_NEW_MESSAGE -> {
			
			}
		}
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