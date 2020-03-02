package com.example.rubbishcommunity.model.api.release.draft

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 16：12
 */

/**
 * 草稿
 * */
data class Draft(
	val classify: Int,
	val content: String?,
	val latitude: Double,
	val longitude: Double,
	val pictures: List<String>,
	val title: String?,
	val topic: String?
)