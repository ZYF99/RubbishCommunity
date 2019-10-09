package com.example.rubbishcommunity

import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.persistence.getLocalUserName
import com.example.rubbishcommunity.ui.release.dynamic.GetQiNiuTokenRequestModel
import com.luck.picture.lib.entity.LocalMedia
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UpProgressHandler
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//七牛云工具
class QiNiuUtil {
	interface QiNiuUpLoadListener {
		fun onSuccess(s: String)
		fun onProgress(percent: Int)
	}
	
}

//上传单张图片
fun upLoadImage(
	apiService: ApiService,
	imagePath: String,
	upLoadListener: QiNiuUtil.QiNiuUpLoadListener
): Single<ResultModel<Map<String, String>>> {
	val upKey = "${getLocalUserName()}/${System.currentTimeMillis()}"
	return apiService.getQiNiuToken(GetQiNiuTokenRequestModel("dew", listOf(upKey)))
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.doOnSuccess { tokenRsp ->
			tokenRsp.data.map {
				UploadManager().put(
					imagePath, upKey, tokenRsp.meta.msg,
					{ key, responseInfo, response ->
						//上传后，返回结果
						val s = "$key, $responseInfo, $response"
						//返回码处理
						dealQiNiuErrorCode(responseInfo)
						//上传成功
						upLoadListener.onSuccess(s)
					}, UploadOptions(
						null, "test-type", true,
						UpProgressHandler { key, percent ->
							//上传中，返回进度
							upLoadListener.onProgress(percent.toInt() * 100)
							
						}, null
					)
				)
			}
		}.compose(dealError())
}

//上传图片列表
fun upLoadImageList(
	apiService: ApiService,
	imagePathList: List<LocalMedia>,
	upLoadListener: QiNiuUtil.QiNiuUpLoadListener
): Single<ResultModel<Map<String, String>>> {
	val pathList = mutableListOf<String>()
	val upKeyList = mutableListOf<String>()
	var mill = System.currentTimeMillis()
	imagePathList.map { media ->
		pathList.add(media.path)
		mill += 1000
		upKeyList.add("${getLocalUserName()}/$mill")
	}
	return apiService.getQiNiuToken(GetQiNiuTokenRequestModel("dew", upKeyList))
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.compose(dealErrorCode())
		.doOnSuccess { tokenRsp ->
			tokenRsp.data.map { entry ->
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
							upLoadListener.onSuccess(s)
						}
					}, UploadOptions(
						null, "test-type", true,
						UpProgressHandler { key, percent ->
							//上传中，返回进度
							upLoadListener.onProgress(((100 / (pathList.size)) * percent).toInt())
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

