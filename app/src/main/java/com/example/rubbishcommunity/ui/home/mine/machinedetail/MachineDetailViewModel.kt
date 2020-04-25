package com.example.rubbishcommunity.ui.home.mine.machinedetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.MachineService
import com.example.rubbishcommunity.manager.catchApiError
import com.example.rubbishcommunity.model.api.machine.FetchMachineInfoResultModel
import com.example.rubbishcommunity.model.api.machine.FetchMachineSearchHistoryRequestModel
import com.example.rubbishcommunity.model.api.machine.FetchMachineSearchHistoryResultModel
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.Flowable
import io.reactivex.Single
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class MachineDetailViewModel(application: Application) : BaseViewModel(application) {
	private val machineService by instance<MachineService>()
	val machineInfo = MutableLiveData<FetchMachineInfoResultModel.MachineHeathInfo>()
	val searchList = MutableLiveData<List<FetchMachineSearchHistoryResultModel.MachineSearchHistory.SearchCount>>()
	
	fun fetchMachineHealthInfo() {
		Flowable.interval(2, TimeUnit.SECONDS)
			.flatMapSingle {
				machineService.fetchMachineInfo()
			}
			.switchThread()
			.catchApiError()
			.doOnNext {resultModel ->
				machineInfo.value = resultModel.data.machineHeathInfoList.find {
					it.machineMacAddress == machineInfo.value?.machineMacAddress
				}
			}.bindLife()
	}
	
	fun fetchSearchHistory() {
		machineService.fetchSearchHistoryInfo(
			FetchMachineSearchHistoryRequestModel(
				99999999999999,
				machineInfo.value?.machineMacAddress,
				1
			)
		).dealLoading()
			.doOnApiSuccess {
				searchList.postValue(it.data.machineSearchHistoryList.find {
					it.machineMacAddress == machineInfo.value?.machineMacAddress
				}?.searchCountList)
			}
	}
	
}
