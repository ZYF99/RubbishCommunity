package com.example.rubbishcommunity.ui.guide.login

import android.content.Context
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.ContractDialogBinding
import com.example.rubbishcommunity.ui.widget.BottomDialogView

class ContractDialog(
    context: Context?,
    private val onFinishClick: ()->Unit
) : BottomDialogView<ContractDialogBinding>(
    context,
    R.layout.dialog_contract) {
    
    override fun initView() {
        childBinding!!.btnFinish.setOnClickListener {
            dismiss()
            onFinishClick.invoke()
        }
    }

}