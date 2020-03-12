package com.example.rubbishcommunity.ui.home.mine

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.MineFragmentBinding
import com.example.rubbishcommunity.persistence.saveLoginState
import com.example.rubbishcommunity.ui.container.jumpToEditInfo
import com.example.rubbishcommunity.ui.container.jumpToPassword
import com.example.rubbishcommunity.ui.container.jumpToLogin
import com.example.rubbishcommunity.ui.utils.showBackgroundAlbum
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia

class MineFragment : BindingFragment<MineFragmentBinding, MineViewModel>(
	MineViewModel::class.java, R.layout.fragment_mine
) {
	
	override fun initBefore() {
	
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		//binding.collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
		
		//刷新状态监听
		viewModel.isRefreshing.observeNonNull { binding.rootLayout.isEnabled = !it }
		
		//设置按钮
		binding.btnSetting.setOnClickListener { jumpToEditInfo(context!!) }
		
		//账号安全按钮
		binding.btnSafe.setOnClickListener { jumpToPassword(context!!) }
		
		//注销按钮
		binding.btnLogout.setOnClickListener { logout() }
		
		binding.iv.setOnClickListener { showChooseBackGroundAlert() }
	}
	
	private fun logout() {
		viewModel.logout()
			.doOnSuccess {
				//注销成功
				saveLoginState(false)
				jumpToLogin(context!!)
				activity?.finish()
			}
			.bindLife()
	}
	
	override fun initData() {
	
	}
	
	override fun onResume() {
		super.onResume()
		//回到该页面时重新加载数据
		viewModel.refreshUserInfo()
	}
	
	private fun showChooseBackGroundAlert() {
		AlertDialog.Builder(context!!).setTitle("更换背景").setPositiveButton("从相册选择") { _, _ ->
			showBackgroundAlbum()
		}.show()
	}
	
	//选图后的回调
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		val images: List<LocalMedia>
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
				images = PictureSelector.obtainMultipleResult(data)
				viewModel.editBackground(images[0].cutPath)
			}
		}
	}
	
}