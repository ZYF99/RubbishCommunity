package com.example.rubbishcommunity.ui.release.moments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FragmentReleaseMomentBinding
import com.example.rubbishcommunity.ui.adapter.ITEM_SWIPE_FREE
import com.example.rubbishcommunity.ui.adapter.attachItemSwipe
import com.example.rubbishcommunity.ui.utils.showGallery
import com.example.rubbishcommunity.ui.utils.showAlbum
import com.example.rubbishcommunity.utils.checkStoragePermission
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia

annotation class MomentsType {
	companion object {
		const val TYPE_DYNAMIC = "dynamic"
		const val TYPE_RECOVERY = "recovery"
	}
}

class ReleaseMomentFragment : BindingFragment<FragmentReleaseMomentBinding, ReleaseMomentViewModel>(
	ReleaseMomentViewModel::class.java, R.layout.fragment_release_moment
) {
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	}
	
	override fun onSoftKeyboardClosed() {
	}
	
	//添加图片按钮点击事件
	private val onAddPicClick: () -> Unit = {
		//获取写的权限
		checkStoragePermission().doOnNext {
			// 打开选图界面
			showAlbum(9, viewModel.selectedList.value!!)
		}.bindLife()
	}
	
	//单项图片点击
	private val onGridItemClick: (Int, View) -> Unit = { position, _ ->
		//单项点击
		val media = viewModel.selectedList.value!![position]
		//val picType = media.mimeType
		showGallery(
			context!!,
			viewModel.selectedList.value!!.map {
				it.path
			},
			position
		)
	}
	
	//单项图片删除
	private val onGridItemDelClick: (Int) -> Unit = { position ->
		viewModel.selectedList.value?.removeAt(position)
	}
	
	override
	fun initBefore() {
		binding.vm = viewModel
		viewModel.momentsType = activity?.intent?.getStringExtra("moments_type")!!
		viewModel.toolbarTitle.value = when (viewModel.momentsType) {
			MomentsType.TYPE_DYNAMIC -> "发布动态"
			MomentsType.TYPE_RECOVERY -> "发布回收"
			else -> ""
		}
	}
	
	override fun initWidget() {
		//已选图片列表
		binding.imgRec.run {
			layoutManager = GridLayoutManager(context, 3)
			attachItemSwipe(ITEM_SWIPE_FREE, {}, {})
			adapter = ReleaseDynamicGridImageAdapter(
				context,
				viewModel.selectedList.value ?: mutableListOf(),
				onAddPicClick,
				onGridItemClick,
				onGridItemDelClick
			)
		}
		
		//发布按钮
		binding.btnRelease.setOnClickListener {
			//真实发布
			viewModel.release {
				//发布成功
				MyApplication.showSuccess("发布成功")
				activity!!.finish()
			}
		}
		
		//清除草稿按钮
		binding.btnClearDraft.setOnClickListener {
			//清理草稿箱
			viewModel.clearDraft {
				MyApplication.showSuccess("草稿已清理~")
			}
		}
		
		//监听选中列表的变化
		viewModel.selectedList.observeNonNull {
			(binding.imgRec.adapter as ReleaseDynamicGridImageAdapter).replaceData(it)
		}
		
	}
	
	@RequiresApi(Build.VERSION_CODES.Q)
	override fun initData() {
		//获取草稿
		viewModel.fetchDraft()
		//获取位置
		getLocation()
	}
	
	//获取定位
	@RequiresApi(Build.VERSION_CODES.Q)
	private fun getLocation() {
		viewModel.getCity()
/*		checkLocationPermissionAndGetLocation(
			initLocationClient(MyApplication.instance)
		).doOnNext {
			viewModel.location.postValue(it)
		}.doOnError {
			//当没有定位权限时
			showLocationServiceDialog {
				getLocation()
			}
		}.bindLife()*/
	}
	
	//选图后的回调
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		val images: List<LocalMedia>
		if (resultCode == RESULT_OK) {
			if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
				// 例如 LocalMedia 里面返回三种path
				// 1.media.getPath(); 为原图path
				// 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
				// 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
				// 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
				images = PictureSelector.obtainMultipleResult(data)
				viewModel.selectedList.postValue(images)
			}
		}
	}
	
	//退出警告
	private fun exit() {
		if (viewModel.selectedList.value!!.isNotEmpty()
			|| viewModel.title.value!!.isNotEmpty()
			|| viewModel.content.value!!.isNotEmpty()
		) {
			//退出编辑警告
			AlertDialog.Builder(context!!)
				.setTitle(R.string.release_exit_dialog_title)
				.setMessage(R.string.release_exit_dialog_msg)
				.setPositiveButton(R.string.release_exit_dialog_save) { _, _ ->
					viewModel.saveDraft {
						MyApplication.showSuccess("已存入草稿箱～")
						activity?.finish()
					}
				}
				.setNegativeButton(R.string.exit) { _, _ ->
					activity?.finish()
				}
				.create()
				.show()
		} else {
			activity!!.finish()
		}
	}
	
	
	//返回按钮
	override fun onBackPressed(): Boolean {
		exit()
		return true
	}
	
	
}