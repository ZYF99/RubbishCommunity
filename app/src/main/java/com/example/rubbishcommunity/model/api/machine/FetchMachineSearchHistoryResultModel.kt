package com.example.rubbishcommunity.model.api.machine

data class FetchMachineSearchHistoryResultModel(
	val machineSearchHistoryList:List<MachineSearchHistory>
	

) {
	data class MachineSearchHistory(
		val machineMacAddress: String,
		val machineName: String,
		val machineType: Int,
		val machineVersion: String,
		val machineMaker: String,
		val nikeName: String,
		val searchCountList: List<SearchCount>
		
	){
		data class SearchCount(
			val searchName: String,
			val searchCount: Int
		)
	}
	

}



