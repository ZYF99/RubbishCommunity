package com.example.rubbishcommunity.service

import android.util.Base64
import com.example.rubbishcommunity.NotifyMessageOutClass
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject

//创建 'MQ消息' 流
private val mqNotifySubject = PublishSubject.create<Any>()

//向下游发送 'MQ消息' 数据
fun sendMQData(
	notifyMessageType: NotifyMessageOutClass.NotifyType,
	mqNotifyData: MQNotifyData
) = mqNotifySubject.onNext(
	MQNotifyData(
		notifyMessageType,
		mqNotifyData.payload
	)
)

//向下游发送 'MQ消息' 数据
fun sendMQData(
	mqNotifyData: MQNotifyData
) = mqNotifySubject.onNext(
	mqNotifyData
)


//得到一个含 'MQ消息' 数据的流
fun getMQNotifyObs() = mqNotifySubject.ofType<MQNotifyData>()

// 'MQ消息' 信息的数据类
data class MQNotifyData(
	val mqNotifyType: NotifyMessageOutClass.NotifyType,
	val payload: String
)

private fun parseMQData(
	mqNotifyData: MQNotifyData
): Any {
	val resultDataBytes = Base64.decode(
		mqNotifyData.payload,
		Base64.NO_WRAP
	)
	
	//信息类型处理
	return when (mqNotifyData.mqNotifyType) {
		NotifyMessageOutClass.NotifyType.NOTIFY_MACHINE_SEARCH -> {//硬件搜索垃圾
			NotifyMessageOutClass.MachineNotifyMessage.parseFrom(resultDataBytes)
		}
		NotifyMessageOutClass.NotifyType.SYNC_MACHINE_HEALTH -> {//硬件更新健康信息
		
		}
		
		NotifyMessageOutClass.NotifyType.SYNC_MOMENTS_COMMENT -> {//Moments被评论
		
		}
		NotifyMessageOutClass.NotifyType.SYNC_MOMENTS_FAVORITE -> {//Moments被点赞
		
		}
		NotifyMessageOutClass.NotifyType.SYNC_MOMENTS_REPLY -> {//Moments的评论被回复
		
		}
		NotifyMessageOutClass.NotifyType.ERROR_DEFAULT_TYPE -> {//
		
		}
		else -> {
		}
	}
}

//将NotifyMessageData解析成NotifyMessage的Model
fun parseDataToMachineNotifyMessage(mqNotifyData: MQNotifyData) =
	parseMQData(mqNotifyData) as NotifyMessageOutClass.MachineNotifyMessage
	

