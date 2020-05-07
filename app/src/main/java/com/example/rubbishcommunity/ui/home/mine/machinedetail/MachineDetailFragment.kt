package com.example.rubbishcommunity.ui.home.mine.machinedetail

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rubbishcommunity.NotifyMessageOutClass
import com.example.rubbishcommunity.R
import com.example.rubbishcommunity.databinding.FragmentMachineDetailBinding
import com.example.rubbishcommunity.model.api.machine.FetchMachineInfoResultModel
import com.example.rubbishcommunity.persistence.saveMachineSearchHistoryEndTime
import com.example.rubbishcommunity.persistence.saveMachineSearchHistoryStartTime
import com.example.rubbishcommunity.service.MQNotifyData
import com.example.rubbishcommunity.service.parseDataToMachineNotifyMessage
import com.example.rubbishcommunity.ui.base.BindingFragment
import com.example.rubbishcommunity.ui.home.MainActivity
import com.example.rubbishcommunity.ui.widget.DatePopView
import com.example.rubbishcommunity.utils.getTimeShortFloat
import com.example.rubbishcommunity.utils.globalGson
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.reactivex.Observable
import java.lang.StringBuilder
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MachineDetailFragment : BindingFragment<FragmentMachineDetailBinding, MachineDetailViewModel>(
	MachineDetailViewModel::class.java, R.layout.fragment_machine_detail
) {
	override fun initBefore() {
		binding.vm = viewModel
		viewModel.machineInfo.value =
			globalGson.getAdapter(FetchMachineInfoResultModel.MachineHeathInfo::class.java)
				.fromJson(activity?.intent?.getStringExtra("machineInfo"))
	}
	
	override fun initWidget() {
		
		//搜索历史观察
		viewModel.searchList.observeNonNull {
			(binding.recSearchHistory.adapter as SearchHistoryAdapter).replaceData(it)
		}
		
		//配置搜索历史列表
		binding.recSearchHistory.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = SearchHistoryAdapter {}
		}
		
		//开始时间
		binding.btnStartTime.setOnClickListener {
			DatePopView(
				context!!,
				viewModel.endTime.value
			) { _, _, _, timeLong ->
				viewModel.startTime.value = timeLong
				saveMachineSearchHistoryStartTime(timeLong)
				viewModel.fetchSearchHistory()
			}.show()
		}
		
		//结束时间
		binding.btnEndTime.setOnClickListener {
			DatePopView(
				context!!,
				viewModel.endTime.value
			) { _, _, _, timeLong ->
				viewModel.endTime.value = timeLong
				saveMachineSearchHistoryEndTime(timeLong)
				viewModel.fetchSearchHistory()
			}.show()
		}
		
		//配置使用率折线图
		setUpRateLineChart()
		
		//配置温度折线图
		setUpTempLineChart()
		
	}
	
	override fun initData() {
		
		//拉历史
		viewModel.fetchSearchHistory()
		
		//拉健康状态
		viewModel.fetchMachineHealthInfo {
			//开始无限画折线图
			Observable.interval(0, 1, TimeUnit.SECONDS)
				.doOnNext {
					val rateData = viewModel.lineRateData.value
					val tempData = viewModel.lineTempData.value
					val timeX = getTimeShortFloat(Date())
					updateRateLineChartData(
						Entry(timeX, rateData?.first ?: 0f),
						Entry(timeX, rateData?.second ?: 0f),
						Entry(timeX, rateData?.third ?: 0f)
					)
					updateTempLineChartData(Entry(timeX,tempData?:0f))
				}.bindLife()
		}
		
	}
	
	//接受到MQ消息
	override fun onMQMessageArrived(mqNotifyData: MQNotifyData) {
		when (mqNotifyData.mqNotifyType) {
			NotifyMessageOutClass.NotifyType.NOTIFY_MACHINE_SEARCH -> { //搜索有更新
				parseDataToMachineNotifyMessage(mqNotifyData)
				viewModel.fetchSearchHistory()
			}
			NotifyMessageOutClass.NotifyType.SYNC_MACHINE_HEALTH -> { //健康状态有更新
				//parseDataToMachineNotifyMessage(mqNotifyData)
				viewModel.fetchMachineHealthInfo {}
			}
			else -> {
			
			}
		}
	}
	
	/**
	 * 配置更新内存、硬盘、cpu使用率折线图
	 */
	private fun setUpRateLineChart() {
		binding.linRateChart.apply {
			description = Description().apply { text = "内存、硬盘、CPU使用情况图" }
			setScaleEnabled(true)
			xAxis.labelCount = 5
			xAxis.spaceMin = 1f
			xAxis.position = XAxis.XAxisPosition.BOTTOM
			xAxis.valueFormatter = object : ValueFormatter() {
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
			data = LineData(
				mutableListOf<ILineDataSet>(
					createLineDataSet("内存使用率", resources.getColor(R.color.blue)),
					createLineDataSet("硬盘使用率", resources.getColor(R.color.colorRed)),
					createLineDataSet("CPU使用率", resources.getColor(R.color.colorAccent))
				)
			)
			invalidate()
		}
	}
	
	/**
	 * 配置cpu温度折线图
	 */
	private fun setUpTempLineChart() {
		binding.linTempChart.apply {
			description = Description().apply { text = "CPU温度折线图" }
			axisRight.isEnabled = false
			setScaleEnabled(true)
			xAxis.labelCount = 5
			xAxis.spaceMin = 1f
			xAxis.position = XAxis.XAxisPosition.BOTTOM
			xAxis.valueFormatter = TimeValueFormatter()
			data = LineData(
				mutableListOf<ILineDataSet>(
					createLineDataSet("CPU温度", resources.getColor(R.color.blue))
				)
			)
			invalidate()
		}
	}
	
	/**
	 * 更新内存、硬盘、cpu使用率折线图的数据
	 */
	private fun updateRateLineChartData(
		memoryData: Entry?,
		diskData: Entry?,
		cpuData: Entry?
	) {
		val lineDataTemp = binding.linRateChart.data
		lineDataTemp.addEntry(memoryData, 0)
		lineDataTemp.addEntry(diskData, 1)
		lineDataTemp.addEntry(cpuData, 2)
		// 像ListView那样的通知数据更新
		binding.linRateChart.notifyDataSetChanged()
		binding.linRateChart.xAxis.setLabelCount(5, true)
		binding.linRateChart.moveViewToX(lineDataTemp.xMax) // 将坐标移动到最新
	}
	
	/**
	 * cpu温度折线图的数据
	 */
	private fun updateTempLineChartData(
		tempData: Entry?
	) {
		val lineDataTemp = binding.linTempChart.data
		lineDataTemp.addEntry(tempData, 0)
		// 像ListView那样的通知数据更新
		binding.linTempChart.notifyDataSetChanged()
		binding.linTempChart.xAxis.setLabelCount(5, true)
		binding.linTempChart.moveViewToX(lineDataTemp.xMax) // 将坐标移动到最新
	}
	
	/**
	 * 创建一条折线
	 */
	private fun createLineDataSet(
		name: String,
		lineColor: Int
	) = LineDataSet(mutableListOf(), name).apply {
		axisDependency = YAxis.AxisDependency.LEFT
		color = lineColor
		setDrawCircles(false)
		setDrawValues(false)
	}
	
}
