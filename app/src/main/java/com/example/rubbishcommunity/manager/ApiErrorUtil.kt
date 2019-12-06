package com.example.rubbishcommunity.manager


import com.example.rubbishcommunity.manager.base.ServerError
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.ui.utils.ErrorData
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.example.rubbishcommunity.utils.*
import io.reactivex.SingleTransformer
import java.net.SocketTimeoutException


/**
 * 处理异常返回码并抛出
 *
 * */

//resolve errorCode in Observable
fun <T> dealErrorCode(): SingleTransformer<T, T> {
	return SingleTransformer { obs ->
		obs.doOnSuccess { result ->
			when (result) {
				is ResultModel<*> -> {//LeoWong的API错误
					when (result.meta.code) {
						in 1000..1999 -> {
							return@doOnSuccess
						}
						else -> {
							throw ApiError(result as ResultModel<*>)
						}
					}
				}
				
				else -> {
					return@doOnSuccess
				}
			}
		}
	}
}


//处理错误信息
fun <T> dealError(): SingleTransformer<T, T> {
	return SingleTransformer { obs ->
		obs.doOnError { error ->
			when (error) {
				is ApiError -> {
					sendError(
						ErrorType.API_ERROR,
						error.result.meta.msg
					)
				}
				is ServerError -> {
					sendError(
						ErrorType.SERVERERROR,
						error.msg
					)
				}
				is SocketTimeoutException -> {
					sendError(
						ErrorType.API_ERROR,
						"请求超时～"
					)
				}
				
				is QiNiuUpLoadError -> {
					sendError(
						ErrorType.SERVERERROR,
						error.responseInfo.error
					)
				}
				is HanLPInputError -> {
					sendError(
						ErrorType.UI_ERROR,
						error.str
					)
				}
				else -> sendError(
					ErrorData(
						ErrorType.UNEXPECTED
					)
				)
			}
		}
	}
}

//业务异常，非服务异常
data class ApiError(val result: ResultModel<*>) :
	Throwable()