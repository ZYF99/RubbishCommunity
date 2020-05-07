package com.example.rubbishcommunity.ui.home.message

import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MessageBinding
import com.example.rubbishcommunity.ui.adapter.ITEM_SWIPE_VERTICAL
import com.example.rubbishcommunity.ui.adapter.attachItemSwipe
import com.example.rubbishcommunity.ui.container.jumpToChat
import com.example.rubbishcommunity.ui.home.MainActivity
import com.example.rubbishcommunity.ui.home.message.friends.FriendListAdapter
import com.google.android.material.appbar.AppBarLayout
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MessageFragment : BindingFragment<MessageBinding, MessageViewModel>(
	MessageViewModel::class.java, R.layout.fragment_message
) {
	
	var isDrawerOpen = false
	
	override fun initBefore() {
	
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		
		viewModel.messageList.observeNonNull {
			(binding.recMessage.adapter as MessageListAdapter).replaceData(it)
		}
		
		viewModel.isRefreshing.observeNonNull { isRefreshing ->
			binding.refreshLayout.isRefreshing = isRefreshing
		}
		

		//监听AppBar滑动隐藏下面的BottomNavigationView
		binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
			(activity as? MainActivity)?.viewModel?.onAppBarOffsetChanged(
				verticalOffset,
				appbarHeight = binding.appbar.height.toFloat() - binding.toolbarHide.height
			)
		})
		
		//下拉刷新控件
		binding.refreshLayout.setOnRefreshListener {
			viewModel.fetchMessageList()
			viewModel.fetchFriendsList()
		}
		
		//消息列表
		binding.recMessage.run {
			adapter = MessageListAdapter(
				{ message ->
					//if (!isDrawerOpen)//侧边栏未开启
					jumpToChat(
						context!!,
						message.user
					)
				},
				{ position ->
					MyApplication.showToast("删除$position")
				}
			)
			
			//添加拖拽互换位置功能
			attachItemSwipe(ITEM_SWIPE_VERTICAL, {
				//onSwipeStart 交换开始
				binding.refreshLayout.isEnabled = false
			}, {
				//onSwiped 交换完成
				binding.refreshLayout.isEnabled = true
			})
		}
		
		//侧拉条点击
		RxView.clicks(binding.btnExpand)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				binding.drawerlayout.openDrawer(GravityCompat.END)
			}.bindLife()
		
		//侧拉条拉动
		binding.btnExpand.setBouncedragListener(object : DragBounceView.BounceDragListener {
			override fun onDragMaxTrigger() {
				//拉到最大值时
				//binding.drawerlayout.openDrawer(GravityCompat.END)
			}
			
			override fun onDragBounceComplete() {
				//反弹回原始位置时
				Observable.just("")
					.delay(200, TimeUnit.MILLISECONDS)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.doOnNext {
						binding.drawerlayout.openDrawer(GravityCompat.END)
					}
					.bindLife()
			}
		})
		
		//侧拉栏
		binding.drawerlayout.addDrawerListener(object : DrawerLayout.DrawerListener {
			override fun onDrawerStateChanged(newState: Int) {
			}
			
			override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
			}
			
			override fun onDrawerClosed(drawerView: View) {
				isDrawerOpen = false
			}
			
			override fun onDrawerOpened(drawerView: View) {
				isDrawerOpen = true
			}
		})
		
		//好友列表
		binding.rightSideLayout.findViewById<RecyclerView>(R.id.friendlsit).run {
			adapter = FriendListAdapter { user ->
				jumpToChat(context, user.recipientProfile)
			}
		}
		
		viewModel.friendsList.observeNonNull {
			(binding.rightSideLayout.findViewById<RecyclerView>(R.id.friendlsit).adapter as FriendListAdapter)
				.replaceData(it)
		}
		
		
	}
	
	//初始化数据
	override fun initData() {
		viewModel.fetchMessageList()
		viewModel.fetchFriendsList()
	}
	
}