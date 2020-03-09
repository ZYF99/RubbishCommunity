package com.example.rubbishcommunity.ui.home.message

import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MessageBinding
import com.example.rubbishcommunity.model.Person
import com.example.rubbishcommunity.ui.adapter.ITEM_SWIPE_VERTICAL
import com.example.rubbishcommunity.ui.adapter.attachItemSwipe
import com.example.rubbishcommunity.ui.container.jumpToChat
import com.example.rubbishcommunity.ui.home.message.friends.FriendListAdapter
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
			binding.refreshLayout.run {
				if (!isRefreshing) finishRefresh()
			}
		}
		
		//下拉刷新控件
		binding.refreshLayout.setOnRefreshListener {
			viewModel.fetchMessageList()
		}
		
		//消息列表
		binding.recMessage.run {
			layoutManager = LinearLayoutManager(context)
			adapter = MessageListAdapter(
				{ message ->
					//if (!isDrawerOpen)//侧边栏未开启
					jumpToChat(
						context!!,
						message.uid
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
			val friendList = mutableListOf(
				Person(
					"aaaaa",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试A"
				),
				Person(
					"aaaaa",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试A"
				),
				Person(
					"aaaaa",
					"https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=a9e671b9a551f3dedcb2bf64a4eff0ec/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
					"测试A"
				)
			)
			layoutManager = LinearLayoutManager(context)
			adapter = FriendListAdapter { message ->
				jumpToChat(context, message.uid)
			}.apply { replaceData(friendList) }
		}
		
	}
	
	//初始化数据
	override fun initData() {
		viewModel.fetchMessageList()
	}
	
}