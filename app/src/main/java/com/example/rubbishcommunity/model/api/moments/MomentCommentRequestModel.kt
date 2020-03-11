package com.example.rubbishcommunity.model.api.moments

const val COMMENT_COMMENT = 0 //评论
const val COMMENT_LIKE = 1 // 点赞

data class MomentCommentRequestModel(
	val commentType: Int? = COMMENT_COMMENT,
	val content: String? = "favor",
	val momentId: Long?
)