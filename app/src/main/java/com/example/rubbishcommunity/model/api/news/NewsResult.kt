package com.example.rubbishcommunity.model.api.news

import com.example.rubbishcommunity.model.api.SimpleProfileResp

data class NewsResult(
	val newsDetailList: List<News>?
) {
	data class News(
		val newsId: Long,
		val title: String,
		val createDate: Long,
		val category: String,
		val authorProfile: SimpleProfileResp,
		val newsType: Int,
		val payloadType: Int,
		val payload: String,
		val frontCoverImages: List<String>
	) {
	
		fun isTEXT() = (payloadType == 1)
		fun isURL() = (payloadType == 2)
		fun isMD() = (payloadType == 3)
		fun isHTML() = (payloadType == 4)
		fun isBanner() = (newsType == 1)
	}
	
}
