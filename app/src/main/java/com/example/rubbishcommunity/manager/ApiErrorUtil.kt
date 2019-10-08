package com.example.rubbishcommunity.manager


import com.example.rubbishcommunity.manager.base.ServerError
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.utils.ErrorData
import com.example.rubbishcommunity.utils.ErrorType
import com.example.rubbishcommunity.utils.sendError
import io.reactivex.SingleTransformer


/**
 * 处理异常返回码并抛出
 *
 * */

//resolve errorCode in Observable
fun <T> dealErrorCode(): SingleTransformer<T, T> {
	return SingleTransformer { obs ->
		obs.doOnSuccess { result ->
			when ((result as ResultModel<*>).meta.code) {
				in 999..2000 -> {
					return@doOnSuccess
				}
				else -> {
					throw ServerBackException(result as ResultModel<*>)
				}
			}
		}
	}
}


//resolve error in Observable
fun <T> dealError(): SingleTransformer<T, T> {
	return SingleTransformer { obs ->
		obs.doOnError { error ->
			when (error) {
				is ServerBackException -> {
					sendError(
						ErrorData(
							ErrorType.REGISTER_OR_LOGIN_FAILED,
							error.result.meta.msg
						)
					)
				}
				is ServerError -> {
					sendError(
						ErrorData(
							ErrorType.SERVERERROR
						)
					)
				}
				else -> sendError(ErrorData(ErrorType.UNEXPECTED))
			}
		}
	}
}

//业务异常，非服务异常
data class ServerBackException(val result: ResultModel<*>) :
	Throwable()