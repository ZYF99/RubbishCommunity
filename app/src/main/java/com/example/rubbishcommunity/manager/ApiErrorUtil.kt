package com.example.rubbishcommunity.manager


import com.example.rubbishcommunity.manager.base.ServerError
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.ui.utils.ErrorData
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import com.example.rubbishcommunity.utils.*
import io.reactivex.Observable
import io.reactivex.Single
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
			if (judgeCodeIfIsSuccess(result)) return@doOnSuccess
			else throw ApiError(result as ResultModel<*>)
		}
	}
}

private fun <T> judgeCodeIfIsSuccess(result: T): Boolean {
	return when (result) {
		is ResultModel<*> -> {//LeoWong的API错误
			when (result.meta.code) {
				in 1000..1999 -> true
				else -> false
			}
		}
		else -> true
	}
}


private fun dealRetryError(reTryCount: Int, error: Throwable): Boolean {
	return (error as? ApiError)?.result?.meta?.code == -1000 && reTryCount < 3
}

private fun catchApiError(error: Throwable){
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


fun <T> Single<T>.catchApiError(): Single<T> =
	compose(dealErrorCode())
		.compose(com.example.rubbishcommunity.manager.catchApiError())

fun <T>Observable<T>.catchApiError(): Observable<T> {
	return retry { reTryCount, error ->
		//服务器返回meta的code为-1000时需要进行至多3次重试
		dealRetryError(reTryCount, error)
	}.doOnError { error ->
		catchApiError(error)
	}
}

//处理错误信息
fun <T> catchApiError(): SingleTransformer<T, T> {
	return SingleTransformer { obs ->
		obs.retry { reTryCount, error ->
			(error as? ApiError)?.result?.meta?.code == -1000 && reTryCount < 3
		}.doOnError { error ->
			catchApiError(error)
		}
	}
}

//业务异常，非服务异常
data class ApiError(val result: ResultModel<*>) : Throwable()