package com.example.rubbishcommunity.manager


import com.example.rubbishcommunity.ui.utils.ErrorType
import com.example.rubbishcommunity.ui.utils.sendError
import io.reactivex.CompletableTransformer


/**
 * 处理UI异常并抛出错误
 *
 * */


//处理错误信息
fun dealUiError(): CompletableTransformer {
	return CompletableTransformer { obs ->
		obs.doOnError { error ->
			when (error) {
				is UiError -> {
					sendError(
						ErrorType.UI_ERROR,
						error.content
					)
				}
				else -> {
					sendError(
						ErrorType.UNEXPECTED,
						error.message ?: "未知错误"
					)
				}
			}
		}
	}
}

//UI 异常
data class UiError(val content: String) :
	Throwable()