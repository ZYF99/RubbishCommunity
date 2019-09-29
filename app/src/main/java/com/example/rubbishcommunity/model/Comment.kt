package com.example.rubbishcommunity.model

import com.example.rubbishcommunity.model.api.mine.UserCardResultModel
import java.io.Serializable

class Comment(
	val user: UserCardResultModel,
	val content: String,
	val time: String,
	val innerCommentList: List<Comment>?
) : Serializable