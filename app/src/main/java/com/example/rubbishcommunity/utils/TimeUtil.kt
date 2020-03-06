package com.example.rubbishcommunity.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.R
import java.text.ParseException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

fun long2DateString(timeStamp: Long): String {
	val format = SimpleDateFormat("yyyy年MM月dd日")
	val date = Date(timeStamp)
	return format.format(date)
}

@BindingAdapter("time")
fun long2Date(textView: TextView, birthLong: Long) {
	textView.text = long2DateString(birthLong)
}


fun string2Date(string: String): Date {
	val format = SimpleDateFormat("yyyy年MM月dd日")
	var date = Date()
	try {
		date = format.parse(string)!!
	} catch (e: ParseException) {
		MyApplication.showError("转换日期失败：${e.message}")
	}
	return date
}

fun getNowString(): String {
	val formatter = SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss")
	val curDate = Date(System.currentTimeMillis())
	//获取当前时间
	return formatter.format(curDate)
}

fun getAgeByBirth(birthDay: Date): Int {
	var age: Int
	val cal = Calendar.getInstance()
	if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
		throw  IllegalArgumentException(
			"The birthDay is before Now.It's unbelievable!"
		)
	}
	val yearNow = cal.get(Calendar.YEAR)  //当前年份
	val monthNow = cal.get(Calendar.MONTH)  //当前月份
	val dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH) //当前日期
	cal.time = birthDay
	val yearBirth = cal.get(Calendar.YEAR)
	val monthBirth = cal.get(Calendar.MONTH)
	val dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH)
	age = yearNow - yearBirth   //计算整岁数
	if (monthNow <= monthBirth) {
		if (monthNow == monthBirth) {
			if (dayOfMonthNow < dayOfMonthBirth) age--//当前日期在生日之前，年龄减一
		} else {
			age-- //当前月份在生日之前，年龄减一
		}
	}
	return age
}

/**
 * 获取时间 小时:分 HH:mm
 *
 * @return
 */
fun getTimeShort(date: Date): String {
	val formatter = SimpleDateFormat("HH:mm")
	return formatter.format(date)
}


@BindingAdapter("recentTime")
fun getRecentTime(textView: TextView, timeStamp: Long) {
	val time = Date(timeStamp)
	val now = Date()
	textView.text = when (judgeRecentRime(time, now)) {
		YESTERDAY -> "昨天 ${time.hours}:${time.minutes}"
		TODAY -> getTimeShort(time)
		IN_SEVEN_DAY -> getWeekOfDate(Date(timeStamp))
		OUT_SEVEN_DAY -> {
			if (now.year == time.year) {
				//同年
				"${time.month}月${time.day}日 ${getTimeShort(time)}"
			} else {
				//非同年
				"${time.year}年${time.month}月${time.day}日 ${getTimeShort(time)}"
			}
		}
		else -> {
			//其他（防止错误）
			"${time.year}年${time.month}月${time.day}日 ${getTimeShort(time)}"
		}
	}
}

const val TODAY = -1 //今天
const val YESTERDAY = 0 //昨天
const val IN_SEVEN_DAY = 1 //七天以内
const val OUT_SEVEN_DAY = 2 //七天外


/**
 * @author Zhangyf.
 * @param oldTime 较小的时间
 * @param newTime 较大的时间 (如果为空   默认当前时间 ,表示和当前时间相比)
 * @return -1 ：同一天.    0：昨天 .   1 ：至少是前天.
 */
@SuppressLint("SimpleDateFormat")
private fun judgeRecentRime(oldTime: Date, newTime: Date): Int {
	//将下面的 理解成  yyyy-MM-dd 00：00：00 更好理解点
	val format = SimpleDateFormat("yyyy-MM-dd")
	val todayStr = format.format(newTime)
	val today = format.parse(todayStr)
	//昨天 86400000=24*60*60*1000 一天
	return if (today!!.time - oldTime.time > 0 && today.time - oldTime.time <= 86400000) {
		//昨天
		YESTERDAY
	} else if (today.time - oldTime.time <= 0) { //至少是今天
		//今天
		TODAY
	} else { //至少是前天
		if ((today.time - oldTime.time) < 604800000) {
			//七天以内
			IN_SEVEN_DAY
		} else {
			//至少七天前
			OUT_SEVEN_DAY
		}
	}
	
}

fun getWeekOfDate(dt: Date): String {
	val weekDays = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
	val cal = Calendar.getInstance()
	cal.time = dt
	var w = cal.get(Calendar.DAY_OF_WEEK) - 1
	if (w < 0)
		w = 0
	return weekDays[w]
}
