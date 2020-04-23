package com.example.rubbishcommunity.ui.userinfo

import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FragmentUserInfoBinding
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.home.mine.RecentMomentsAdapter

class UserInfoFragment : BindingFragment<FragmentUserInfoBinding, UserInfoViewModel>(
	UserInfoViewModel::class.java, R.layout.fragment_user_info
) {
	
	var openId: String? = null
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	
	}
	
	override fun onSoftKeyboardClosed() {
	
	}
	
	
	override fun initBefore() {
		binding.vm = viewModel
		openId = activity?.intent?.extras?.getString("openId")
	}
	
	override fun initWidget() {
		
		viewModel.isLoadingMore.observeNonNull {
			(binding.recRecent.adapter as RecentMomentsAdapter).onLoadMore = it
		}
		
		//返回按钮
		binding.btnBack.setOnClickListener {
			activity?.finish()
		}
		
		binding.recRecent.run {
			adapter = RecentMomentsAdapter()
		}
		
		//最近动态
		viewModel.momentList.observeNonNull {
			(binding.recRecent.adapter as RecentMomentsAdapter).replaceData(it.filter { it.pictures.isNotEmpty() })
		}
		
		//上拉加载
		binding.recRecent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				if (!recyclerView.canScrollVertically(1)
					&& viewModel.momentList.value?.size ?: 0 > 0
				)
					if (viewModel.isLoadingMore.value == false) {
						viewModel.loadMoreMoments(openId)
					}
			}
		})
	}
	
	
	//初始化数据
	override fun initData() {
		refresh()
	}
	
	private fun refresh() {
		viewModel.fetchUserInfo(openId)
		viewModel.refreshRecentMoments(openId)
	}
	
	//back键
	override fun onBackPressed(): Boolean {
		if (!isHidden) {
			activity?.finish()
			return true
		}
		return false
	}
	
}