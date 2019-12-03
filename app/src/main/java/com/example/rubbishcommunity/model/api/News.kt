package com.example.rubbishcommunity.model.api



data class NewsResult(
	val reason:String,
	val result:Result
){
	data class Result(
		val stat:String,
		val data:MutableList<News>
	)
	
}
data class News(
val uniquekey:String,
val title:String,
val date:String,
val category:String,
val author_name:String,
val url:String,
val thumbnail_pic_s:String,
val thumbnail_pic_s02:String
)