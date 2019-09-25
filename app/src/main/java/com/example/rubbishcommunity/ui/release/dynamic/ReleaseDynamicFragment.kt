package com.example.rubbishcommunity.ui.release.dynamic

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rubbishcommunity.ui.BindingFragment
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ReleaseDynamicBinding
import com.example.rubbishcommunity.ui.guide.AnimatorUtils
import com.example.rubbishcommunity.ui.container.ContainerActivity
import com.example.rubbishcommunity.utils.ErrorData
import com.example.rubbishcommunity.utils.ErrorType
import com.example.rubbishcommunity.utils.sendError
import com.jakewharton.rxbinding2.support.design.widget.RxNavigationView
import com.jakewharton.rxbinding2.view.RxMenuItem
import com.jakewharton.rxbinding2.view.RxView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.permissions.RxPermissions
import java.util.concurrent.TimeUnit


class ReleaseDynamicFragment : BindingFragment<ReleaseDynamicBinding, ReleaseDynamicViewModel>(
	ReleaseDynamicViewModel::class.java, R.layout.frag_release_dynamic
) {
	private lateinit var animationUtils: AnimatorUtils
	
	private val maxSelectNum = 9
	private val selectList = mutableListOf<LocalMedia>()
	
	
	override
	fun initBefore() {
		binding.vm = viewModel
		viewModel.init()

/*		//初始化动画工具
		animationUtils = AnimatorUtils(
			(binding.linContent.layoutParams as ViewGroup.MarginLayoutParams).leftMargin,
			(binding.linContent.layoutParams as ViewGroup.MarginLayoutParams).rightMargin,
			binding.progress,
			binding.linContent,
			binding.btnLogin
		)*/
	}
	
	
	override fun initWidget() {
		(activity as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
		//观测是否在Loading
		viewModel.isLoading.observeNonNull {
			if (it) {
				//开始登陆的动画
				animationUtils.startTransAnimation()
			} else {
				//结束登陆的动画
				animationUtils.complete()
			}
		}
		binding.imgRec.run {
			layoutManager = FullyGridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
			val recAdapter = GridImageAdapter(
				context!!,
				mutableListOf(),
				object : GridImageAdapter.OnItemClickListener {
					override fun onItemClick(position: Int, v: View) {
						if (selectList.size > 0) {
							val media = selectList[position]
							val pictureType = media.pictureType
							when (PictureMimeType.pictureToVideo(pictureType)) {
								1 ->
									// 预览图片 可自定长按保存路径
									//PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
									PictureSelector.create(activity).externalPicturePreview(
										position,
										selectList
									)
								2 ->
									// 预览视频
									PictureSelector.create(activity).externalPictureVideo(media.path)
								3 ->
									// 预览音频
									PictureSelector.create(activity).externalPictureAudio(media.path)
							}
						}
					}
					
				},
				object : GridImageAdapter.OnAddPicClickListener {
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
									sendError(ErrorData(ErrorType.UNEXPECTED))
								}
							}
							.bindLife()
					}
				})
			
			adapter = recAdapter
		}
		
		binding.toolbar.toolbar.setNavigationOnClickListener {
			showExitWarningDialog()
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
			
			}.bindLife()
		
		
	}
	
	//发布
	private fun release() {
		//网络检查
		if (!isNetworkAvailable()) {
			(activity as ContainerActivity).showNetErrorSnackBar()
			return
		}
		//真实发布
		viewModel.release()?.doOnSuccess {
			//发布成功
			(context as Activity).finish()
			
		}?.bindLife()
	}
	
	private fun showAlbum() {
		//参数很多，根据需要添加
		PictureSelector.create(this)
			.openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
			.maxSelectNum(maxSelectNum)// 最大图片选择数量
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
			.withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
			//.selectionMedia(selectList)// 是否传入已选图片
			//.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
			//.cropCompressQuality(90)// 裁剪压缩质量 默认100
			//.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
			//.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
			//.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
			.rotateEnabled(false) // 裁剪是否可旋转图片
			//.scaleEnabled()// 裁剪是否可放大缩小图片
			//.recordVideoSecond()//录制视频秒数 默认60s
			.forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
	}
	
	override fun initData() {
	
	}
	
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
				
				binding.imgRec.run {
					(adapter as GridImageAdapter).run {
						replaceDates(images)
					}
					
				}
			}
		}
	}
	
	override fun onBackPressed(): Boolean {
		showExitWarningDialog()

		return true
	}
	private fun showExitWarningDialog(){
		//退出编辑警告
		val dialog = context?.let {
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
	
	
}