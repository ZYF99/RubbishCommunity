package com.example.rubbishcommunity.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.ui.widget.BasePopWindow
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit


class AddDialog(context: Context, private val onAddDialogClickListener: OnAddDialogClickListener) :
	BasePopWindow(context){
	
	lateinit var view: View
	private lateinit var btnDynamic: LinearLayout
	private lateinit var btnRecovery: LinearLayout

	override fun getViewId(inflater: LayoutInflater): View {
		view = inflater.inflate(R.layout.dialog_add, null)
		
		btnDynamic = view.findViewById(R.id.btn_dynamic)
		btnRecovery = view.findViewById(R.id.btn_recovery)

		return view
	}

	override fun initWindow() {
		super.initWindow()
		
		RxView.clicks(btnDynamic).throttleFirst(2,TimeUnit.SECONDS).doOnNext {
			onAddDialogClickListener.onDynamicClick()
			dismiss()
		}.subscribe()
		
		RxView.clicks(btnRecovery).throttleFirst(2,TimeUnit.SECONDS).doOnNext {
			onAddDialogClickListener.onRecoveryClick()
			dismiss()
		}.subscribe()
		
	}
	

	override fun dismiss() {
		super.dismiss()
		onAddDialogClickListener.onDismiss()
	}

	//抛给view层的监听接口
	interface OnAddDialogClickListener {
		
		//点击添加弹窗中的添加动态
		fun onDynamicClick()
		
		//点击添加弹窗中的发起投票
		fun onRecoveryClick()

		//功能弹窗的dismiss
		fun onDismiss()
	}
}
