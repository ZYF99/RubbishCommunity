package com.example.fenrir_stage4.mainac.ui.widget

import android.content.Context

import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.rubbishcommunity.R


class SexBox(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    companion object{
        const val MAN = 0
        const val WOMAN = 1
    }

    private var isMan = false
    private var isWoman = false
    private var manText: TextView? = null
    private var womanText: TextView? = null
    private var onBoxClickListener: OnBoxItemClickListener? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.sexbox, this)
        manText = findViewById(R.id.man_tx)
        womanText = findViewById(R.id.woman_tx)
        turnToMan()
        manText!!.setOnClickListener {
            if (isWoman) turnToMan()
        }
        womanText!!.setOnClickListener {
            if (isMan) turnToWoman()
        }
    }


    private fun turnToMan() {
        isMan = true
        isWoman = false
        manText!!.background = resources.getDrawable(R.drawable.bg_inputlin_selected)
        womanText!!.background = resources.getDrawable(R.drawable.bg_inputlin_idle)
        onBoxClickListener?.onManClick()
    }

    private fun turnToWoman() {
        isMan = false
        isWoman = true
        manText!!.background = resources.getDrawable(R.drawable.bg_inputlin_idle)
        womanText!!.background = resources.getDrawable(R.drawable.bg_inputlin_selected)
        onBoxClickListener?.onWomanClick()
    }




    //get current status
    fun getStatus(): Int {

        return when (isMan) {
            true -> MAN
            false -> WOMAN
        }

    }


    interface OnBoxItemClickListener{

        fun onManClick()

        fun onWomanClick()

    }


}