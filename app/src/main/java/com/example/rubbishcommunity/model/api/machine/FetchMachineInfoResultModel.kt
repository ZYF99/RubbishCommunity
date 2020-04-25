package com.example.rubbishcommunity.model.api.machine

data class FetchMachineInfoResultModel(
	val machineHeathInfoList: List<MachineHeathInfo>
) {
	data class MachineHeathInfo(
		val machineMacAddress: String,
		val machineName: String,
		val machineType: Int,
		val machineVersion: String,
		val machineMaker: String,
		val nikeName: String,
		val healthInfoResult: HealthInfoResult
	) {
		data class HealthInfoResult(
			val cpuCoreCount: Int,
			val cpuTemperature: Double,
			val cpuUsageRate: Double,
			val diskUseRate: String,
			val freeDiskSize: String,
			val freeMemorySize: Int,
			val hardDiskSize: String,
			val memorySize: Int,
			val systemName: String,
			val usedHardDiskSize: String,
			val usedMemorySize: Int,
			val macAddress: String,
			val ipAddr: String
		)
	}
}
