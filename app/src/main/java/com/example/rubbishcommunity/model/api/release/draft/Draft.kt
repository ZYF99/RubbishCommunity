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
	val msg: String,
	val classify: Int,
	val dynamic: String,
	val latitude: Double,
	val longitude: Double,
	val pictures: List<String>,
	val topic: String
)