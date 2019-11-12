package com.example.rubbishcommunity.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.example.rubbishcommunity.R

class ContractDialog(context: Context?, private val onFinishClick: ()->Unit) : BottomDialogView(context) {
    
    @SuppressLint("InflateParams")
    override var bView = LayoutInflater.from(context).inflate(R.layout.dialog_contract,null)!!

    override fun initView() {
        btnFinish = bView.findViewById(R.id.btn_finish)
        btnFinish.setOnClickListener {
            dismiss()
            onFinishClick.invoke()
        }
    }

}