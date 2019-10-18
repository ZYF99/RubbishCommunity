package com.example.rubbishcommunity.ui.home.message

import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MessageBinding
import com.example.rubbishcommunity.model.Person
import com.example.rubbishcommunity.ui.container.jumpToChat
import com.example.rubbishcommunity.ui.home.MainActivity
import com.example.rubbishcommunity.ui.home.message.friends.FriendListAdapter
import com.example.rubbishcommunity.ui.widget.DragBounceView

class MessageFragment : BindingFragment<MessageBinding, MessageViewModel>(
	MessageViewModel::class.java, R.layout.fragment_message
), MessageListAdapter.OnClickListener {
	override fun onCellClick(position: Int) {
		jumpToChat(context!!, (viewModel.messageList.value ?: mutableListOf())[position].uid)
	}
	
	override fun onDellClick(position: Int) {
		MyApplication.showToast("删除$position")
	}
	
	override fun initBefore() {
	
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		//viewModel.isRefreshing.observe { binding.refreshlayout.isRefreshing = it!! }
		viewModel.getMessageList().doOnSubscribe {
			binding.recMessage.adapter?.notifyDataSetChanged()
		}.bindLife()
		
		binding.recMessage.run {
			layoutManager = LinearLayoutManager(context)
			//adapter = MessageListAdapter(R.layout.cell_msg, viewModel.messageList.value,viewModel)
			adapter = viewModel.messageList.value?.let {
				MessageListAdapter(context, it, this@MessageFragment)
			}
		}
		
		binding.btnExpand.setBouncedragListener(object : DragBounceView.BounceDragListener {
			override fun onDragMaxTrigger() {
				binding.drawerlayout.openDrawer(GravityCompat.END)
			}
			
			override fun onDragBounceComplete() {
			
			}
			
		})
		
		binding.refreshLayout.setOnRefreshListener {
			when {
				!isNetworkAvailable() -> {
					(activity as MainActivity).showNetErrorSnackBar()
					viewModel.refreshing.postValue(false)
				}
				else -> {
					viewModel.getMessageList().doOnSubscribe {
						binding.recMessage.adapter?.notifyDataSetChanged()
					}.bindLife()
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
		
		binding.drawerlayout.addDrawerListener(object : DrawerLayout.DrawerListener {
			override fun onDrawerStateChanged(newState: Int) {
			
			}
			
			override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
			
			}
			
			override fun onDrawerClosed(drawerView: View) {
			}
			
			override fun onDrawerOpened(drawerView: View) {
			
			}
			
		})
		binding.rightSideLayout.findViewById<RecyclerView>(R.id.friendlsit).run {
			layoutManager = LinearLayoutManager(context)
			adapter = FriendListAdapter(context, mutableListOf(
				Person(
					"",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试A"
				),
				Person(
					"",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试B"
				),
				Person(
					"",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试C"
				),
				Person(
					"",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试D"
				),
				Person(
					"",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试E"
				),
				Person(
					"",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试F"
				),
				Person(
					"",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试G"
				),
				Person(
					"",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试H"
				)
			), object : FriendListAdapter.OnClickListener {
				override fun onCellClick(position: Int) {
				}
				
				override fun onDellClick(position: Int) {
				}
			}
			
			)
			
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