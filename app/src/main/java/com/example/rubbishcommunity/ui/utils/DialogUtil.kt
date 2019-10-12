package com.example.rubbishcommunity.ui.utils

import android.content.Context
import android.content.Intent
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



fun showUnexpectedDialog(context: Context): AlertDialog {
    val dialog = AlertDialog.Builder(context)
        .setTitle(R.string.homepage_dialog_title_unexpected)
        .setNegativeButton(R.string.ok, null)
        .create()
    dialog.show()
    return dialog
}



//创建 '异常' 流
private val errorSubject = PublishSubject.create<Any>()

//向下游发送 '异常' 数据
fun sendError(errorData: ErrorData) = errorSubject.onNext(errorData)

//得到一个含 '异常' 数据的流
fun getErrorObs() = errorSubject.ofType<ErrorData>()

// '异常' 信息的数据类
data class ErrorData(val errorType: ErrorType, val errorContent: String = "")

// '异常' 类型
enum class ErrorType {
    INPUT_ERROR,
    NO_WIFI,
    NO_CAMERA,
    REGISTER_OR_LOGIN_FAILED,
    UNEXPECTED,
    SERVERERROR,
    //七牛云上传失败
    QINIU_UPLOAD_FAILED,
}