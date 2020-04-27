package com.example.rubbishcommunity.ui.home.mine.machinedetail

import com.github.mikephil.charting.formatter.ValueFormatter
import java.lang.StringBuilder
import java.text.DecimalFormat

class TimeValueFormatter : ValueFormatter() {
	override fun getFormattedValue(value: Float): String {
		val decimalFormat = DecimalFormat("00.00")  //构造方法的字符格式这里如果小数不足2位,会以0补足.
		val strTemp = decimalFormat.format(value)
		val str = if (strTemp.split(".")[1].toInt() >= 60) {
			strTemp.split(".")[0] + ".60"
		} else strTemp
		val s = str.replace(".", "分")
		return StringBuilder(s).append("秒").toString()
	}
}