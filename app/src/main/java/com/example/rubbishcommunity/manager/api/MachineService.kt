package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.machine.*
import io.reactivex.Single
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 14：12
 */
interface MachineService {
	
	//绑定硬件设备
	@POST("api/IoTDA/bind")
	fun bindMachine(
		@Body BindMachineRequestModel: BindMachineRequestModel
	): Single<ResultModel<BindMachineResultModel?>>
	
	//获取所有硬件设备信息
	@GET("api/IoTDA/health")
	fun fetchMachineInfo(): Single<ResultModel<FetchMachineInfoResultModel>>
	
	//获取设备历史
	@POST("api/IoTDA/search/history")
	fun fetchSearchHistoryInfo(@Body fetchMachineSearchHistoryResultModel: FetchMachineSearchHistoryRequestModel): Single<ResultModel<FetchMachineSearchHistoryResultModel>>
	
}