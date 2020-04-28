package com.example.rubbishcommunity.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.example.rubbishcommunity.R
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject

/**
 *  DialogUtil
 */

/*
* 退出App警告
* */
fun showExitWarningDialog(context: Context, okAction: () -> Unit): AlertDialog {
	val dialog = AlertDialog.Builder(context)
		.setTitle(R.string.homepage_dialog_title_no_wife)
		.setMessage(R.string.homepage_dialog_msg_no_wife)
		.setPositiveButton(R.string.homepage_dialog_button_setting) { _, _ ->
			context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
		}
		.setNegativeButton(R.string.ok) { _, _ -> okAction.invoke() }
		.create()
	dialog.show()
	return dialog
}


fun showNoWifiDialog(context: Context, okAction: () -> Unit): AlertDialog {
	val dialog = AlertDialog.Builder(context)
		.setTitle(R.string.homepage_dialog_title_no_wife)
		.setMessage(R.string.homepage_dialog_msg_no_wife)
		.setPositiveButton(R.string.homepage_dialog_button_setting) { _, _ ->
			context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
		}
		.setNegativeButton(R.string.ok) { _, _ -> okAction.invoke() }
		.create()
	dialog.show()
	return dialog
}

//引导用户去设置界面的弹窗
fun Context.showLeadToSettingDialog(permissionStr: String) {
	//解释原因，并且引导用户至设置页手动授权
	AlertDialog.Builder(this)
		.setMessage(
			"获取${permissionStr}权限失败:\n" +
					"读取、写入或删除存储空间\n" +
					"点击'去授权'到设置页面手动授权"
		)
		.setPositiveButton("去授权") { _, _ ->
			//引导用户至设置页手动授权
			val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
			val uri = Uri.fromParts("package", packageName, null)
			intent.data = uri
			startActivity(intent)
		}
		.setNegativeButton("取消") { _, _ ->
		}.show()
}


fun Context.showUnexpectedDialog(): AlertDialog {
	val dialog = AlertDialog.Builder(this)
		.setTitle(R.string.homepage_dialog_title_unexpected)
		.setNegativeButton(R.string.ok, null)
		.create()
	dialog.show()
	return dialog
}


//创建 '异常' 流
private val errorSubject = PublishSubject.create<Any>()

//向下游发送 '异常' 数据
fun sendError(
	errorType: ErrorType,
	errorContent: String
) = errorSubject.onNext(
	ErrorData(
		errorType,
		errorContent
	)
)

//向下游发送 '异常' 数据
fun sendError(
	errorData: ErrorData
) = errorSubject.onNext(
	errorData
)


//得到一个含 '异常' 数据的流
fun getErrorObs() = errorSubject.ofType<ErrorData>()

// '异常' 信息的数据类
data class ErrorData(val errorType: ErrorType, val errorContent: String = "")

// '异常' 类型
enum class ErrorType {
	UI_ERROR,
	API_ERROR,
	TOKEN_INVALID,
	SERVERERROR,
	UNEXPECTED,
}