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
			val cpuCoreCount: Int, //cpu核心数
			val cpuTemperature: Double, //cpu温度
			val cpuUsageRate: Double, //cpu使用率
			val diskUseRate: String, //硬盘使用率
			val freeDiskSize: String, //硬盘剩余空间
			val freeMemorySize: Int, //内存剩余空间
			val hardDiskSize: String, //硬盘空间
			val memorySize: Int, //内存大小
			val systemName: String, //系统名
			val usedHardDiskSize: String,//硬盘已使用空间
			val usedMemorySize: Int, //已使用内存大小
			val macAddress: String, //mac地址
			val ipAddr: String //硬件在内网中的ip地址
		)
	}
}
