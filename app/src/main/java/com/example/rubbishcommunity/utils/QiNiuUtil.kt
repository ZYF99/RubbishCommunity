package com.example.rubbishcommunity.utils

import com.example.rubbishcommunity.manager.api.ImageService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.GetQiNiuTokenRequestModel
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.persistence.getLocalEmail
import com.luck.picture.lib.entity.LocalMedia
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UpProgressHandler
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//七牛云工具
class QiNiuUtil

//上传单张图片
fun upLoadImage(
	imageService: ImageService,
	imagePath: String,
	onUpdateSuccess: (String) -> Unit,
	onUpdateProgress: (Int) -> Unit
): Single<ResultModel<Map<String, String>>> {
	val upKey = "${getLocalEmail()}/${System.currentTimeMillis()}"
	return imageService.getQiNiuToken(GetQiNiuTokenRequestModel("dew", listOf(upKey)))
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.doOnSuccess { tokenRsp ->
			tokenRsp.data.map {
				UploadManager().put(
					imagePath, upKey, it.value,
					{ key, responseInfo, response ->
						//上传后，返回结果
						val s = "$key, $responseInfo, $response"
						//返回码处理
						dealQiNiuErrorCode(responseInfo)
						//上传成功
						onUpdateSuccess(s)
					}, UploadOptions(
						null, "test-type", true,
						UpProgressHandler { key, percent ->
							//上传中，返回进度
							onUpdateProgress(percent.toInt() * 100)
						}, null
					)
				)
			}
		}.compose(dealError())
}

//上传图片列表
fun upLoadImageList(
	imageService: ImageService,
	imagePathList: List<LocalMedia>,
	onUpdateSuccess: (String) -> Unit,
	onUpdateProgress: (Int) -> Unit
): Single<ResultModel<Map<String, String>>> {
	val pathList = mutableListOf<String>()
	val upKeyList = mutableListOf<String>()
	var mill = System.currentTimeMillis()
	imagePathList.map { media ->
		pathList.add(media.path)
		mill += 100
		upKeyList.add("${getLocalEmail()}/$mill")
	}
	return imageService.getQiNiuToken(GetQiNiuTokenRequestModel("dew", upKeyList))
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.compose(dealErrorCode())
		.doOnSuccess { tokenRsp ->
			tokenRsp.data.map { entry ->
				//key是图片在七牛云的上的相对path，value是上传所需token
				val index = upKeyList.indexOf(entry.key)
				UploadManager().put(
					pathList[index], upKeyList[index], entry.value,
					{ key, responseInfo, response ->
						//上传后，返回结果
						val s = "$key, $responseInfo, $response"
						//返回码处理
						dealQiNiuErrorCode(responseInfo)
						if (index == imagePathList.size - 1) {
							//已经全部上传成功
							onUpdateSuccess(s)
							
						}
					}, UploadOptions(
						null, "test-type", true,
						UpProgressHandler { key, percent ->
							//上传中，返回进度
							onUpdateProgress(((100 / (pathList.size)) * percent).toInt())
						}, null
					)
				)
			}
		}.compose(dealError())
}

data class QiNiuUpLoadException(val responseInfo: ResponseInfo) : Throwable()

fun dealQiNiuErrorCode(responseInfo: ResponseInfo) {
	when {
		responseInfo.isOK -> {
			//上传成功
		}
		!responseInfo.isOK -> {
			//上传失败，抛出异常
			throw QiNiuUpLoadException(responseInfo)
		}
		else -> {
			//其他
		}
	}
}

