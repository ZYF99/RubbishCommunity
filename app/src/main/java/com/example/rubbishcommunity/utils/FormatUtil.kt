package com.example.rubbishcommunity.utils

import com.example.rubbishcommunity.MyApplication
import java.text.ParseException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

fun stringToDate(string: String): Date {
	val format = SimpleDateFormat("yyyy年MM月dd日")
	val pos = ParsePosition(0)
	var date:Date = Date()
	try {
		date = format.parse(string,pos)!!
	}catch (e: ParseException) {
		MyApplication.showError("转换日期失败：${e.message}")
	}
	return date
}