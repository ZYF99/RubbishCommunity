package com.example.rubbishcommunity.ui.widget


import android.content.Context
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.EditNameDialogBinding

class EditNameDialog(
	context: Context?,
	private val oldName: String,
	val onConfirmClick: (String) -> Unit
) : BottomDialogView<EditNameDialogBinding>(
	context,
	R.layout.dialog_edit_name
) {
	
	override fun initView() {
		setCancelable(true)
		childBinding.name = oldName
		childBinding.btnConfirm.setOnClickListener {
			dismiss()
			onConfirmClick(childBinding.name ?: oldName)
		}
	}
	
}