package com.example.rubbishcommunity.ui.home.mine

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DialogBindMachineBinding
import com.example.rubbishcommunity.databinding.HeaderMineBinding
import com.example.rubbishcommunity.databinding.MineFragmentBinding
import com.example.rubbishcommunity.ui.container.jumpToEditInfo
import com.example.rubbishcommunity.ui.container.jumpToMachineDetail
import com.example.rubbishcommunity.ui.container.jumpToMomentDetail
import com.example.rubbishcommunity.ui.home.MainActivity
import com.example.rubbishcommunity.ui.utils.showBackgroundAlbum
import com.google.android.material.appbar.AppBarLayout
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia

class MineFragment : BindingFragment<MineFragmentBinding, MineViewModel>(
	MineViewModel::class.java, R.layout.fragment_mine
) {
	
	var bindAlertDialog: AlertDialog? = null
	var headerViewBinding: HeaderMineBinding? = null
	
	override fun initBefore() {
	
	}
	
	override fun initWidget() {
		binding.vm = viewModel
		//binding.collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
		
		//刷新状态监听
		viewModel.isRefreshing.observeNonNull {
			if (it) alertDialog?.show() else alertDialog?.dismiss()
			binding.rootLayout.isEnabled = !it
		}
		
		//加载更多状态监听
		viewModel.isLoadingMore.observeNonNull {
			if (binding.recRecent.adapter != null)
				(binding.recRecent.adapter as MineMomentAdapter).onLoadMore = it
		}
		
		//加载状态监听
		viewModel.isLoadingMore.observeNonNull {
			if (binding.recRecent.adapter != null)
				(binding.recRecent.adapter as MineMomentAdapter).onLoadMore = it
		}
		
		//动态列表
		viewModel.recentMomentList.observeNonNull {
			(binding.recRecent.adapter as MineMomentAdapter).replaceData(it)
		}
		
		//机器列表
		viewModel.machineBannerList.observeNonNull {
			(headerViewBinding?.recBannerMachine?.adapter as MachineBannerAdapter).replaceData(it)
		}
		
		//监听AppBar滑动隐藏下面的BottomNavigationView
		binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
			(activity as? MainActivity)?.viewModel?.onAppBarOffsetChanged(
				verticalOffset,
				appbarHeight = binding.appbar.height.toFloat() - binding.toolbarHide.height
			)
		})
		
		headerViewBinding = DataBindingUtil.inflate(
			LayoutInflater.from(context),
			R.layout.header_mine,
			binding.recRecent,
			false
		)
		
		headerViewBinding?.recBannerMachine?.apply {
			adapter = MachineBannerAdapter {
				jumpToMachineDetail(context, it)
			}
		}
		
		val dialogBinding = DataBindingUtil.inflate<DialogBindMachineBinding>(
			LayoutInflater.from(context),
			R.layout.dialog_bind_machine,
			null,
			false
		)
		
		//绑定设备按钮
		headerViewBinding?.btnBindMachine?.setOnClickListener {
			if (bindAlertDialog == null)
				bindAlertDialog = AlertDialog.Builder(context!!)
					.setTitle("绑定智能设备")
					.setView(dialogBinding.root)
					.setCancelable(false)
					.setPositiveButton("绑定") { _, _ ->
						viewModel.bindMachine(
							dialogBinding.etBindKey.text.toString(),
							dialogBinding.etMac.text.toString(),
							dialogBinding.etNickName.text.toString()
						)
					}
					.setNegativeButton("取消") { _, _ -> }
					.create()
			bindAlertDialog?.show()
		}
		
		//设置按钮
		binding.btnSetting.setOnClickListener { jumpToEditInfo(context!!) }
		
		//背景
		binding.iv.setOnClickListener { showChooseBackGroundAlert() }
		
		//最近动态列表
		binding.recRecent.adapter = MineMomentAdapter {
			context?.run { jumpToMomentDetail(this, it) }
		}.apply {
			headerView = headerViewBinding?.root
		}
		
		//上拉加载
		binding.recRecent.apply {
			(itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
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