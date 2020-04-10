package com.example.rubbishcommunity.model.api.news

import com.example.rubbishcommunity.model.api.SimpleProfileResp

data class NewsResult(
	val newsList: List<News>?
) {
	data class News(
		val newsId: Long,
		val title: String,
		val createData: Long,
		val category: String,
		val authorProfile: SimpleProfileResp,
		val newsType: Int,
		val payloadType: Int,
		val payload: String,
		val frontCoverImages: List<String>
	) {
	
		val isTEXT = (payloadType == 1)
		val isURL = (payloadType == 2)
		val isMD = (payloadType == 3)
		val isHTML = (payloadType == 4)
		val isBanner = (newsType == 1)
	}
	
}
