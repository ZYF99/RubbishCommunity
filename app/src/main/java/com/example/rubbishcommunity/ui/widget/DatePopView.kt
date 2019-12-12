package com.example.rubbishcommunity.ui.widget

import android.content.Context
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.DatePickerBinding
import com.example.rubbishcommunity.utils.stringToDate
import java.text.SimpleDateFormat
import java.util.*

class DatePopView(
	context: Context,
	private val onFinishClick: (String, String, String, Long) -> Unit
) : BottomDialogView<DatePickerBinding>(
	context,
	R.layout.datepickerview
) {
	
	private var birthString: String? = null
	private var birthLong: Long? = null
	private var years = ""
	var months = ""
	var days = ""
	
	constructor(
		context: Context,
		birthString: String,
		onFinishClick: (String, String, String, Long) -> Unit
	) : this(
		context,
		onFinishClick
	) {
		this.birthString = birthString
	}
	
	
	override fun initView() {
		
		var date = getCurrentDate()
		if (birthString == null) {
			years = date.get(Calendar.YEAR).toString() + "年"
			months = (date.get(Calendar.MONTH) + 1).toString() + "月"
			days = date.get(Calendar.DATE).toString() + "日"
		} else {
			val dateS = stringToDate(birthString!!)
			date = Calendar.getInstance().run {
				time = dateS
				this
			}
			years = date.get(Calendar.YEAR).toString() + "年"
			months = (date.get(Calendar.MONTH) + 1).toString() + "月"
			days = date.get(Calendar.DATE).toString() + "日"
		}
		
		
		//定义滚动选择器的数据项（年月日的）
		val gradeYear = mutableListOf<String>()
		val gradeMonth = mutableListOf<String>()
		val gradeDay = mutableListOf<String>()
		
		//为数据项赋值
		val thisYear = Integer.parseInt(SimpleDateFormat("yyyy").format(Date()))
		for (i in 1980..thisYear)
		//从1980到今年
			gradeYear.add(i.toString() + "年")
		for (i in 1..12)
		// 1月到12月
			gradeMonth.add(i.toString() + "月")
		for (i in 1..31)
		// 1日到31日
			gradeDay.add(i.toString() + "日")
		
		
		//set data for wheel
		childBinding.run {
			year.run {
				setData(gradeYear)
				setSelected(years)
				setOnSelectListener {
					years = it
				}
				
			}
			month.run {
				setData(gradeMonth)
				setSelected(months)
				setOnSelectListener {
					months = it
				}
			}
			day.run {
				setData(gradeDay)
				setSelected(days)
				setOnSelectListener {
					days = it
				}
			}
			
		}
		
		childBinding.btnFinish.setOnClickListener {
			dismiss()
			birthLong = Calendar.getInstance().apply {
				time = stringToDate("$years$months$days")
			}.timeInMillis
			onFinishClick(years, months, days, birthLong!!)
		}
		
	}
	
	
	private fun getCurrentDate(): Calendar {
		return Calendar.getInstance()
	}
	
	
}