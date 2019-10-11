package com.example.rubbishcommunity.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.ui.widget.BottomDialogView
import com.example.rubbishcommunity.ui.widget.WheelView

class ContractDialog(context: Context?, private val clickListener: OnMyClickListener) : BottomDialogView(context) {
    
    @SuppressLint("InflateParams")
    override var bView = LayoutInflater.from(context).inflate(R.layout.dialog_contract,null)!!

    override fun initView() {
        btnFinish = bView.findViewById(R.id.btn_finish)
        btnFinish.setOnClickListener {
            dismiss()
            clickListener.onFinishClick()
        }
    }
    
    interface OnMyClickListener {
        fun onFinishClick()
    }

}