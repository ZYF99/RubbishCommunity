package com.example.rubbishcommunity.ui.userinfo

import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FragmentUserInfoBinding
import com.example.rubbishcommunity.ui.base.BindingFragment

class UserInfoFragment : BindingFragment<FragmentUserInfoBinding, UserInfoViewModel>(
	UserInfoViewModel::class.java, R.layout.fragment_user_info
) {
	
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	
	}
	
	override fun onSoftKeyboardClosed() {
	
	}
	
	
	override fun initBefore() {
		binding.vm = viewModel
		
	}
	
	override fun initWidget() {
	
	}
	
	
	//初始化数据
	override fun initData() {
		val openId = activity?.intent?.extras?.getString("openId")
		viewModel.fetchUserInfo(openId)
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