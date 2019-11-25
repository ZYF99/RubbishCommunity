package com.example.rubbishcommunity.model

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/7/31 20:17
 */
data class Dynamic(
    val id: String?,
    val content: String,
    val images: MutableList<String>,
    val uId: String,
    val createTime: String,
    val status: String
)
