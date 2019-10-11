package com.example.rubbishcommunity.utils

import androidx.fragment.app.Fragment
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia

//打开选图界面
fun showAlbum(fragment: Fragment, selectMax: Int, selectedList: List<LocalMedia>?) {
	//参数很多，根据需要添加
	PictureSelector.create(fragment)
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
		.selectionMedia(selectedList)// 是否传入已选图片
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

//打开选头像界面
fun showAvatarAlbum(fragment: Fragment) {
	//参数很多，根据需要添加
	PictureSelector.create(fragment)
		.openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
		.imageSpanCount(4)// 每行显示个数
		.selectionMode(PictureConfig.SINGLE)// 多选 or 单选PictureConfig.MULTIPLE : PictureConfig.SINGLE
		.previewImage(true)// 是否可预览图片
		.isCamera(true)// 是否显示拍照按钮
		.isZoomAnim(true)// 图片列表点击 缩放效果 默认true
		//.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
		.enableCrop(true)// 是否裁剪
		.compress(false)// 是否压缩
		//.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
		.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
		.withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
		//.freeStyleCropEnabled(true)
		//.selectionMedia(selectedList)// 是否传入已选图片
		.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
		//.cropCompressQuality(90)// 裁剪压缩质量 默认100
		//.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
		//.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
		//.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
		.rotateEnabled(true) // 裁剪是否可旋转图片
		.scaleEnabled(true)// 裁剪是否可放大缩小图片
		.hideBottomControls(false)
		//.recordVideoSecond()//录制视频秒数 默认60s
		.forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
}