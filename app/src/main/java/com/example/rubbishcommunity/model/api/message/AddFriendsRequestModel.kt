package com.example.rubbishcommunity.model.api.message

import com.example.rubbishcommunity.persistence.getLocalUin

data class AddFriendsRequestModel(
	val recipientUin: Long, //接收方的UIN
	val relationSource: Int = 1, //渠道： 默认 0   moments 1   搜索 2
	val relationType: Int = 20, //关系类型： 无 0   拉黑 10   发起加好友  20  临时  30   好友  40
	val sponsorUin: Long = getLocalUin() //我的UIN
)