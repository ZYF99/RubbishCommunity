package com.example.rubbishcommunity.ui.home.mine.machinedetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.MachineService
import com.example.rubbishcommunity.model.api.machine.FetchMachineInfoResultModel
import com.example.rubbishcommunity.model.api.machine.FetchMachineSearchHistoryRequestModel
import com.example.rubbishcommunity.model.api.machine.FetchMachineSearchHistoryResultModel
import com.example.rubbishcommunity.persistence.getMachineSearchHistoryEndTime
import com.example.rubbishcommunity.persistence.getMachineSearchHistoryStartTime
import com.example.rubbishcommunity.ui.base.BaseViewModel
import org.kodein.di.generic.instance

class MachineDetailViewModel(application: Application) : BaseViewModel(application) {
	private val machineService by instance<MachineService>()
	val machineInfo = MutableLiveData<FetchMachineInfoResultModel.MachineHeathInfo>()
	val searchList =
		MutableLiveData<List<FetchMachineSearchHistoryResultModel.MachineSearchHistory.SearchCount>>()
	val startTime = MutableLiveData(getMachineSearchHistoryStartTime()) //检索开始时间
	val endTime = MutableLiveData(getMachineSearchHistoryEndTime()) //检索结束时间
	val lineRateData =
		MutableLiveData<Triple<Float, Float, Float>>() //内存折线Entry,硬盘折线Entry,Cpu折线Entry
	val lineTempData = MutableLiveData<Float>() //Cpu温度Entry
	
	//拉取设备健康状态
	fun fetchMachineHealthInfo(action: () -> Unit) {
		machineService.fetchMachineInfo().doOnApiSuccess { resultModel ->
			val machineInfoTemp = resultModel.data.machineHeathInfoList.find {
				it.machineMacAddress == machineInfo.value?.machineMacAddress
			}
			machineInfo.value = machineInfoTemp
			
			lineRateData.value = Triple(
				(machineInfoTemp?.healthInfoResult?.usedMemorySize?.toFloat()
					?: 0f) / (machineInfoTemp?.healthInfoResult?.memorySize?.toFloat()
					?: 0f),
				(machineInfoTemp?.healthInfoResult?.diskUseRate?.replace(
					"%",
					""
				)?.toFloat() ?: 0f) / 100,
				(machineInfoTemp?.healthInfoResult?.cpuUsageRate?.toFloat() ?: 0f) / 100
			)
			lineTempData.value = machineInfoTemp?.healthInfoResult?.cpuTemperature?.toFloat() ?: 0f
			action()
		}
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
				searchList.postValue(it.data.machineSearchHistoryList.find { machineSearchHistory ->
					machineSearchHistory.machineMacAddress == machineInfo.value?.machineMacAddress
				}?.searchCountList)
			}
	}
	
}
