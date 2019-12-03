package com.example.rubbishcommunity.manager


import com.example.rubbishcommunity.manager.base.ServerError
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.ui.utils.ErrorData
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.example.rubbishcommunity.utils.*
import io.reactivex.SingleTransformer


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
							throw ApiException(result as ResultModel<*>)
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
				is ApiException -> {
					sendError(
						ErrorType.REGISTER_OR_LOGIN_FAILED,
						error.result.meta.msg
					)
				}
				is ServerError -> {
					sendError(
						ErrorType.SERVERERROR,
						error.msg
					)
				}
				is QiNiuUpLoadException -> {
					sendError(
						ErrorType.REGISTER_OR_LOGIN_FAILED,
						error.responseInfo.error
					)
				}
				is HanLPInputError -> {
					sendError(
						ErrorType.INPUT_ERROR,
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
data class ApiException(val result: ResultModel<*>) :
	Throwable()