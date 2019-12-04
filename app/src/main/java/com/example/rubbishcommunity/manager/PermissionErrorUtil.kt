package com.example.rubbishcommunity.manager



import com.example.rubbishcommunity.ui.utils.ErrorData
import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import io.reactivex.ObservableTransformer


/**
 * 处理异常返回码并抛出
 *
 * */

//resolve errorCode in Observable
fun <T> dealPermissionErrorMsg(): ObservableTransformer<T, T> {
	return ObservableTransformer { obs ->
		obs.doOnNext { result ->
			when (result) {
				is Boolean -> {
					if(result == true){
						return@doOnNext
					}else{
						throw NoPermissionError()
					}
				}
			}
		}
	}
}



//处理错误信息
fun <T> dealPermissionError(): ObservableTransformer<T, T> {
	return ObservableTransformer { obs ->
		obs.doOnError { error ->
			when (error) {
				is NoPermissionError -> {
					sendError(
						ErrorType.NO_PERMISSION,
						"权限获取失败"
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

//权限获取异常
class NoPermissionError: Throwable()