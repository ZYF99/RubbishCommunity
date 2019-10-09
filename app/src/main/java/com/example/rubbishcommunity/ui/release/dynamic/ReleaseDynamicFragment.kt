package com.example.rubbishcommunity.ui.release.dynamic

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ReleaseDynamicBinding
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.ui.showGallery
import com.example.rubbishcommunity.utils.ErrorData
import com.example.rubbishcommunity.utils.ErrorType
import com.example.rubbishcommunity.utils.sendError
import com.jakewharton.rxbinding2.view.RxView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.permissions.RxPermissions
import java.util.concurrent.TimeUnit


class ReleaseDynamicFragment : BindingFragment<ReleaseDynamicBinding, ReleaseDynamicViewModel>(
	ReleaseDynamicViewModel::class.java, R.layout.fragment_release_dynamic
), ReleaseDynamicGridImageAdapter.OnGridItemClickListener {
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
	
	}
	
	override fun onSoftKeyboardClosed() {
	
	}
	
	//最多展示张数
	private val selectMax = 9
	
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
					sendError(ErrorData(ErrorType.NO_CAMERA, "没有权限获取相册"))
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
				MyApplication.showToast("已保存到草稿箱～")
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
	
	//打开选图界面
	private fun showAlbum() {
		//参数很多，根据需要添加
		PictureSelector.create(this)
			.openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
			.maxSelectNum(selectMax)// 最大图片选择数量
			.minSelectNum(1)// 最小选择数量
			.imageSpanCount(4)// 每行显示个数
			.selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
			.previewImage(true)// 是否可预览图片
			.isCamera(true)// 是否显示拍照按钮
			.isZoomAnim(true)// 图片列表点击 缩放效果 默认true
			//.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
			.enableCrop(false)// 是否裁剪
			.compress(false)// 是否压缩
			//.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
			.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
			//.withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
			//.freeStyleCropEnabled(true)
			.selectionMedia(viewModel.selectedList.value)// 是否传入已选图片
			.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
			//.cropCompressQuality(90)// 裁剪压缩质量 默认100
			//.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
			//.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
			//.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
			.rotateEnabled(false) // 裁剪是否可旋转图片
			.rotateEnabled(false) // 裁剪是否可旋转图片
			.scaleEnabled(true)// 裁剪是否可放大缩小图片
			.hideBottomControls(false)
			//.recordVideoSecond()//录制视频秒数 默认60s
			.forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
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
	
	//获取定位
	private fun getLocation() {
		//定位权限检查
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			//定位权限检查
			if (
				(activity?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
				(activity?.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
			) {
				RxPermissions(activity as Activity).request(
					Manifest.permission.READ_PHONE_STATE,
					Manifest.permission.ACCESS_COARSE_LOCATION
				).doOnNext {
					if (!it) {
						//申请权限未通过
						viewModel.location.postValue("未能成功获取到位置信息")
					} else {
						viewModel.getLocation()
					}
				}.bindLife()
			} else {
				viewModel.getLocation()
			}
		} else {
			viewModel.getLocation()
		}
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
				val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				val uri = Uri.fromParts("package", context?.packageName, null);
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