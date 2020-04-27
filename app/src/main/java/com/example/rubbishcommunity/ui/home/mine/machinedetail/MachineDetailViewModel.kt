package com.example.rubbishcommunity.ui.home.mine.machinedetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.MachineService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.model.api.machine.FetchMachineInfoResultModel
import com.example.rubbishcommunity.model.api.machine.FetchMachineSearchHistoryRequestModel
import com.example.rubbishcommunity.model.api.machine.FetchMachineSearchHistoryResultModel
import com.example.rubbishcommunity.persistence.getMachineSearchHistoryEndTime
import com.example.rubbishcommunity.persistence.getMachineSearchHistoryStartTime
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.getTimeShortFloat
import com.example.rubbishcommunity.utils.switchThread
import com.github.mikephil.charting.data.Entry
import io.reactivex.Flowable
import org.kodein.di.generic.instance
import java.util.*
import java.util.concurrent.TimeUnit

class MachineDetailViewModel(application: Application) : BaseViewModel(application) {
	private val machineService by instance<MachineService>()
	val machineInfo = MutableLiveData<FetchMachineInfoResultModel.MachineHeathInfo>()
	val searchList =
		MutableLiveData<List<FetchMachineSearchHistoryResultModel.MachineSearchHistory.SearchCount>>()
	val startTime = MutableLiveData(getMachineSearchHistoryStartTime()) //检索开始时间
	val endTime = MutableLiveData(getMachineSearchHistoryEndTime()) //检索结束时间
	val lineRateData =
		MutableLiveData<Triple<Entry, Entry, Entry>>() //内存折线Entry,硬盘折线Entry,Cpu折线Entry
	val lineTempData = MutableLiveData<Entry>() //Cpu温度Entry
	
	//拉取设备健康状态
	fun fetchMachineHealthInfo() {
		Flowable.interval(0, 2, TimeUnit.SECONDS)
			.flatMapSingle { machineService.fetchMachineInfo() }
			.switchThread()
			.catchApiError()
			.doOnNext { resultModel ->
				val machineInfoTemp = resultModel.data.machineHeathInfoList.find {
					it.machineMacAddress == machineInfo.value?.machineMacAddress
				}
				machineInfo.value = machineInfoTemp
				val timeX = getTimeShortFloat(Date())
				lineRateData.value = Triple(
					Entry(
						timeX,
						(machineInfoTemp?.healthInfoResult?.usedMemorySize?.toFloat()
							?: 0f) / (machineInfoTemp?.healthInfoResult?.memorySize?.toFloat()
							?: 0f)
					), Entry(
						timeX,
						(machineInfoTemp?.healthInfoResult?.diskUseRate?.replace(
							"%",
							""
						)?.toFloat() ?: 0f) / 100
					), Entry(
						timeX,
						machineInfoTemp?.healthInfoResult?.cpuUsageRate?.toFloat() ?: 0f / 100
					)
				)
				lineTempData.value =
					Entry(timeX, machineInfoTemp?.healthInfoResult?.cpuTemperature?.toFloat() ?: 0f)
			}.bindLife()
	}
	
	//拉取设备垃圾查询历史记录
	fun fetchSearchHistory() {
		machineService.fetchSearchHistoryInfo(
			FetchMachineSearchHistoryRequestModel(
				endTime.value,
				machineInfo.value?.machineMacAddress,
				startTime.value
			)
		).dealLoading()
			.doOnApiSuccess {
				searchList.postValue(it.data.machineSearchHistoryList.find {
					it.machineMacAddress == machineInfo.value?.machineMacAddress
				}?.searchCountList)
			}
	}
	
}
