package com.example.rubbishcommunity.model.api.moments


import com.example.rubbishcommunity.model.api.SimpleProfileResp
import com.example.rubbishcommunity.model.api.mine.UsrProfile
import com.example.rubbishcommunity.persistence.getLocalOpenId
import com.example.rubbishcommunity.ui.utils.equalsList
import java.io.Serializable


data class GetMomentsResultModel(
	val momentContentList: List<MomentContent>,
	val pageInfoResp: PageInfoResp
)

data class MomentContent(
	val momentId: Long,
	val publisher: SimpleProfileResp,
	val originPublisher: SimpleProfileResp,
	val topic: String,
	val title: String,
	val classify: Int,
	val content: String,
	val pictures: List<String>,
	val publishedDate: Long,
	val latitude: Double,
	val longitude: Double,
	val publishType: String,
	val momentCommentList: List<MomentComment>
) {
	val realCommentList: List<MomentComment>?
		get() {
			return momentCommentList.filter { it.commentType == COMMENT_COMMENT }
		}
	
	val likeList: List<MomentComment>?
		get() {
			return momentCommentList.filter { it.commentType == COMMENT_LIKE }
		}
	
	val isLikedByMe: Boolean
		get() {
			return momentCommentList.find { it.commentator.openId == getLocalOpenId() && it.commentType == COMMENT_LIKE } != null
		}
	
	override fun equals(other: Any?): Boolean {
		return hashCode() == other.hashCode()
	}
	override fun hashCode(): Int {
		var result = momentId.hashCode()
		result = 31 * result + publisher.hashCode()
		result = 31 * result + originPublisher.hashCode()
		result = 31 * result + topic.hashCode()
		result = 31 * result + title.hashCode()
		result = 31 * result + classify
		result = 31 * result + content.hashCode()
		result = 31 * result + pictures.hashCode()
		result = 31 * result + publishedDate.hashCode()
		result = 31 * result + latitude.hashCode()
		result = 31 * result + longitude.hashCode()
		result = 31 * result + publishType.hashCode()
		result = 31 * result + momentCommentList.hashCode()
		return result
	}
}

data class MomentComment(
	val commentId: Long,
	val commentType: Int,
	val content: String = "favor",
	val commentator: SimpleProfileResp,
	val commentDate: Long,
	val commentReplyList: List<CommentReply>
)

data class CommentReply(
	val replyId: Long,
	val content: String,
	val replyBy: UsrProfile,
	val replyDate: Long
):Serializable

data class PageInfoResp(
	val pageNum: Int,
	val pageSize: Int,
	val size: Int,
	val startRow: Int,
	val endRow: Int,
	val pages: Int,
	val prePage: Int,
	val nextPage: Int,
	val hasPreviousPage: Boolean,
	val hasNextPage: Boolean,
	val firstPage: Boolean,
	val lastPage: Boolean
)