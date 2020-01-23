package com.example.rubbishcommunity.ui.widget

import android.content.Context
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DatePickerBinding
import com.example.rubbishcommunity.utils.string2Date
import java.text.SimpleDateFormat
import java.util.*

class DatePopView(
	context: Context,
	private val onFinishClick: (String, String, String, Long) -> Unit
) : BottomDialogView<DatePickerBinding>(
	context,
	R.layout.datepickerview
) {
	
	private var birthLong: Long? = null //初始日期时间戳
	private var yearString = "" //选中的年字符串
	private var monthString = "" //选中的月字符串
	private var dayString = "" //选中的日字符串
	
	constructor(
		context: Context,
		birthLong: Long,
		onFinishClick: (String, String, String, Long) -> Unit
	) : this(
		context,
		onFinishClick
	) {
		this.birthLong = birthLong
	}
	
	
	override fun initView() {
		
		//得到今天的日期
		val currentDate = Calendar.getInstance()
		
		//没有传入初始的日期
		if (birthLong == null) {
			//以当前日期作为选中
			yearString = currentDate.get(Calendar.YEAR).toString()
			monthString = (currentDate.get(Calendar.MONTH) + 1).toString()
			dayString = currentDate.get(Calendar.DATE).toString()
		} else {
			//有初始日期，选中初始日期
			val originCalendar = Calendar.getInstance().apply { time = Date(birthLong!!) }
			yearString = originCalendar.get(Calendar.YEAR).toString()
			monthString = (originCalendar.get(Calendar.MONTH) + 1).toString()
			dayString = originCalendar.get(Calendar.DATE).toString()
		}
		
		//定义滚动选择器的数据项（年月日的）
		val gradeYear = mutableListOf<String>()
		val gradeMonth = mutableListOf<String>()
		val gradeDay = mutableListOf<String>()
		
		//为数据项赋值
		val thisYear = Integer.parseInt(SimpleDateFormat("yyyy").format(Date()))
		for (i in 1980..thisYear)
		//从1980到今年
			gradeYear.add(i.toString())
		for (i in 1..12)
		// 1月到12月
			gradeMonth.add(i.toString())
		for (i in 1..31)
		// 1日到31日
			gradeDay.add(i.toString())
		
		
		//set data for wheel
		childBinding.run {
			year.run {
				setData(gradeYear)
				setSelected(yearString)
				setOnSelectListener {
					yearString = it
				}
				
			}
			month.run {
				setData(gradeMonth)
				setSelected(monthString)
				setOnSelectListener {
					monthString = it
				}
			}
			day.run {
				setData(gradeDay)
				setSelected(dayString)
				setOnSelectListener {
					dayString = it
				}
			}
			
		}
		
		childBinding.btnFinish.setOnClickListener {
			dismiss()
			birthLong = Calendar.getInstance().apply {
				time = string2Date("${yearString}年${monthString}月${dayString}日")
			}.timeInMillis
			onFinishClick(yearString, monthString, dayString, birthLong!!)
		}
	}
	
	
}