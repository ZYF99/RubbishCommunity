package com.example.rubbishcommunity.ui.home.mine

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.HeaderMineBinding
import com.example.rubbishcommunity.databinding.MineFragmentBinding
import com.example.rubbishcommunity.ui.container.jumpToEditInfo
import com.example.rubbishcommunity.ui.container.jumpToMomentDetail
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
		
		//动态列表
		viewModel.recentMomentList.observeNonNull {
			(binding.recRecent.adapter as MineMomentAdapter).replaceData(it
				.filter { it.pictures.isNotEmpty() })
		}
		
		val headerViewBinding = DataBindingUtil.inflate<HeaderMineBinding>(
			LayoutInflater.from(context),
			R.layout.header_mine,
			binding.recRecent,
			false
		)
		
		//设置按钮
		binding.btnSetting.setOnClickListener { jumpToEditInfo(context!!) }
		
		//背景
		binding.iv.setOnClickListener { showChooseBackGroundAlert() }
		
		//最近动态列表
		binding.recRecent.adapter = MineMomentAdapter {
			context?.run { jumpToMomentDetail(this, it) }
		}.apply {
			headerView = headerViewBinding.root
		}
		
		//上拉加载
		binding.recRecent.apply {
			
			addOnScrollListener(object : RecyclerView.OnScrollListener() {
				override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
					if (!recyclerView.canScrollVertically(1)
						&& viewModel.recentMomentList.value?.size ?: 0 > 0
					)
						if (viewModel.isLoadingMore.value == false) {
							viewModel.loadMoreMoments()
						}
				}
			})
		}
		
	}
	
	override fun initData() {
		viewModel.refreshUserInfo()
	}
	
	private fun showChooseBackGroundAlert() {
		AlertDialog.Builder(context!!).setTitle("更换背景")
			.setPositiveButton("从相册选择") { _, _ ->
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