package com.example.rubbishcommunity.ui.home.mine.editinfo


import android.content.Context
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.EditGenderDialogBinding
import com.example.rubbishcommunity.databinding.EditNameDialogBinding
import com.example.rubbishcommunity.ui.widget.BottomDialogView

class EditGenderDialog(
	context: Context?,
	private val oldGender: String,
	val onConfirmClick: (String) -> Unit
) : BottomDialogView<EditGenderDialogBinding>(
	context,
	R.layout.dialog_edit_gender
) {
	
	var gender: String = oldGender
	
	override fun initView() {
		setCancelable(true)
		childBinding.gender = gender
		val genderList = mutableListOf("男","女")
		childBinding.wheelGender.run {
			setData(genderList)
			setSelected(gender)
			setOnSelectListener {
				onConfirmClick(it)
				dismiss()
			}
		}
		
	}
	
}