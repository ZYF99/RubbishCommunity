package com.example.rubbishcommunity.ui.widget


import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.UserInfoDialogBinding
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import android.graphics.drawable.ColorDrawable



class UserInfoDialog(
	context: Context,
	private val userInfo: UsrProfile
	, private val onDetailClick: () -> Unit
) :
	AlertDialog(context) {
	
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
		binding.btnDetail.setOnClickListener {
			dismiss()
			onDetailClick()
		}

	}
	
}