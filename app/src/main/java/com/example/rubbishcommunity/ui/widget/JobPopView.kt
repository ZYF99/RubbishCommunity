package com.example.fenrir_stage4.mainac.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.ui.widget.BottomDialogView
import com.example.rubbishcommunity.ui.widget.WheelView

class JobPopView(context: Context) : BottomDialogView(context) {


    var job = ""
    private lateinit var wheelView1: WheelView

    @SuppressLint("InflateParams")
    override var bView = LayoutInflater.from(context).inflate(R.layout.singlepickerview,null)!!

    override fun initView() {
        wheelView1 = bView.findViewById(R.id.city)
        btnFinish = bView.findViewById(R.id.btn_finish)

        val cityList = mutableListOf("记者","教育家","程序员","考古学家","会计","剧作家","经理")
        job = cityList[cityList.size/2]
        wheelView1.setData(cityList)

        //wheel select event
        wheelView1.setOnSelectListener(object : WheelView.OnSelectListener {
            override fun onSelect(text: String) {
                job = text
            }
        })
    }










}