package com.example.rubbishcommunity.ui.widget

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.UserInfoDialogBinding
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.example.rubbishcommunity.model.api.SimpleProfileResp
import com.example.rubbishcommunity.persistence.getLocalOpenId

class UserInfoDialog(
	context: Context,
	private val userInfo: SimpleProfileResp,
	private val onFriendClick: (String) -> Unit,
	private val onHomeClick: () -> Unit
) : AlertDialog(context) {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding = DataBindingUtil.inflate<UserInfoDialogBinding>(
			LayoutInflater.from(context),
			R.layout.dialog_user_info,
			null,
			false
		)
		setContentView(binding.root)
		window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		binding.userInfo = userInfo
		binding.btnFriend.visibility = if(userInfo.openId == getLocalOpenId())View.GONE else View.VISIBLE
		binding.btnFriend.setOnClickListener {
			onFriendClick(userInfo.openId)
		}
		binding.btnHome
			.setOnClickListener {
			dismiss()
			onHomeClick()
		}
	}
	
}