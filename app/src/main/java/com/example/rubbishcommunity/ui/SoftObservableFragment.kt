package com.example.rubbishcommunity.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.rubbishcommunity.utils.SoftKeyBroadManager

open class SoftObservableFragment : Fragment(), SoftKeyBroadManager.SoftKeyboardStateListener {
	protected lateinit var mManager: SoftKeyBroadManager
	
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		//添加软键盘的监听
		mManager = SoftKeyBroadManager(view)
		mManager.addSoftKeyboardStateListener(this)
	}
	
	override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
		view?.scrollTo(0, keyboardHeightInPx)
	}
	
	override fun onSoftKeyboardClosed() {
		view?.scrollTo(0, 0)
	}
	
	override fun onDestroy() {
		super.onDestroy()
		//销毁时移除软键盘监听
		mManager.removeSoftKeyboardStateListener(this)
	}
	
	
}