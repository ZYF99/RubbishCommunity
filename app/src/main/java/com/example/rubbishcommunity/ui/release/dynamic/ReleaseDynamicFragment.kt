package com.example.rubbishcommunity.ui.release.dynamic

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ReleaseDynamicBinding
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.ui.showGallery
import com.example.rubbishcommunity.ui.utils.ErrorData
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.utils.getLocationWithCheckPermission
import com.example.rubbishcommunity.ui.utils.sendError
import com.jakewharton.rxbinding2.view.RxView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.permissions.RxPermissions
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit


class ReleaseDynamicFragment : BindingFragment<ReleaseDynamicBinding, ReleaseDynamicViewModel>(
	ReleaseDynamicViewModel::class.java, R.layout.fragment_release_dynamic
), ReleaseDynamicGridImageAdapter.OnGridItemClickListener {
	
	private val locationClient by instance<LocationClient>()
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	
	}
	
	override fun onSoftKeyboardClosed() {
	
	}
	
	//添加图片按钮
	override fun onAddPicClick() {
		//获取写的权限
		val rxPermission = RxPermissions(activity as Activity)
		rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
			.doOnNext { permission ->
				if (permission.granted) {// 用户已经同意该权限
					//第二种方式，直接进入相册，但是 是有拍照得按钮的
					showAlbum()
					//第一种方式，弹出选择和拍照的dialog
					//showPop
				} else {
					sendError(
						ErrorData(
							ErrorType.NO_CAMERA,
							"没有权限获取相册"
						)
					)
					if (!permission.shouldShowRequestPermissionRationale) {
						showLeadToSettingDialog()
					}
				}
			}
			.bindLife()
	}
	
	//单项图片点击
	override fun onGridItemClick(position: Int, v: View) {
		//单项点击
		val media = viewModel.selectedList.value!![position]
		val picType = media.pictureType
		when (PictureMimeType.pictureToVideo(picType)) {
			1 -> // 预览图片 可自定长按保存路径
				//PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
				/*		PictureSelector.create(this@ReleaseDynamicFragment).externalPicturePreview(
							position,
							viewModel.selectedList.value
						)*/ {
				
				val list = mutableListOf<String>()
				viewModel.selectedList.value!!.forEach {
					list.add(it.path)
				}
				showGallery(context!!, list, position)
			}
			
			2 -> // 预览视频
				PictureSelector.create(this@ReleaseDynamicFragment).externalPictureVideo(
					media.path
				)
			3 -> // 预览音频
				PictureSelector.create(this@ReleaseDynamicFragment).externalPictureAudio(
					media.path
				)
		}
	}
	
	//单项图片删除
	override fun onGridItemDel(position: Int) {
		viewModel.selectedList.value?.removeAt(position)
	}
	
	
	override
	fun initBefore() {
		binding.vm = viewModel
	}
	
	override fun initWidget() {
		//已选图片列表
		binding.imgRec.run {
			layoutManager = FullyGridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
			val recAdapter = ReleaseDynamicGridImageAdapter(
				context,
				mutableListOf(),
				this@ReleaseDynamicFragment
			)
			adapter = recAdapter
		}
		
		//发布按钮
		RxView.clicks(binding.btnRelease)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				release()
			}.bindLife()
		
		//存草稿按钮
		RxView.clicks(binding.btnDraft)
			.throttleFirst(2, TimeUnit.SECONDS)
			.doOnNext {
				viewModel.saveDraft()
			}.bindLife()
		
		//退出点击事件
		binding.toolbar.toolbar.setNavigationOnClickListener {
			showExitWarningDialog()
		}
		
		//监听选中列表的变化
		viewModel.selectedList.observeNonNull {
			(binding.imgRec.adapter as ReleaseDynamicGridImageAdapter).replaceDates(it)
		}
		
	}
	
	override fun initData() {
		viewModel.init()
		//获取位置
		getLocation()
	}
	
	//获取定位
	private fun getLocation() {
		getLocationWithCheckPermission(activity!!,locationClient,object :BDAbstractLocationListener(){
			override fun onReceiveLocation(bdLocation: BDLocation?) {
				viewModel.location.postValue(bdLocation?.addrStr)
			}
		})?.bindLife()
	}
	
	
	//打开选图界面
	private fun showAlbum() {
		com.example.rubbishcommunity.ui.utils.showAlbum(this, 9, viewModel.selectedList.value!!)
	}
	
	//发布
	private fun release() {
		//网络检查
		if (!isNetworkAvailable()) {
			(activity as ContainerActivity).showNetErrorSnackBar()
			return
		}
		//真实发布
		viewModel.release(object : ReleaseListener {
			override fun releaseSuccess(s: String) {
				//发布成功
				viewModel.clearDraft()
				(context as Activity).finish()
				MyApplication.showSuccess("发布成功")
			}
		})?.doOnSuccess {
			//获取Token列表成功
		}?.bindLife()
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
	private fun showExitWarningDialog() {
		//退出编辑警告
		context?.let {
			AlertDialog.Builder(it)
				.setTitle(R.string.release_exit_dialog_title)
				.setMessage(R.string.release_exit_dialog_msg)
				.setPositiveButton(R.string.correct) { _, _ ->
					activity?.finish()
				}
				.setNegativeButton(R.string.cancel) { _, _ ->
				
				}
				.create()
				.show()
		}
	}
	
	//引导用户去设置界面的弹窗
	private fun showLeadToSettingDialog() {
		//解释原因，并且引导用户至设置页手动授权
		AlertDialog.Builder(context!!)
			.setMessage(
				"获取相关权限失败:\n\n" +
						"使用摄像头，\n" +
						"读取、写入或删除存储空间\n\n" +
						"这将导致图片无法添加，点击'去授权'到设置页面手动授权"
			)
			.setPositiveButton("去授权") { _, _ ->
				//引导用户至设置页手动授权
				val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
				val uri = Uri.fromParts("package", context?.packageName, null)
				intent.data = uri
				startActivity(intent)
			}
			.setNegativeButton("取消") { _, _ ->
			}.show()
	}
	
	//返回按钮
	override fun onBackPressed(): Boolean {
		showExitWarningDialog()
		return true
	}
	
	
}