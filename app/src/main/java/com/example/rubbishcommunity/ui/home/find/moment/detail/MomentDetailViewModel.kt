package com.example.rubbishcommunity.ui.home.find.moment.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.manager.api.UserService
import com.example.rubbishcommunity.model.api.moments.*
import com.example.rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class MomentDetailViewModel(application: Application) : BaseViewModel(application) {
	
	private val momentService by instance<MomentService>()
	val moment = MutableLiveData<MomentContent>()
	val isRefreshing = MutableLiveData<Boolean>()
	val inputComment = MutableLiveData<String>()
	
	//拉取单个动态
	private fun fetchMomentDetail() {
		momentService.fetchMomentsByMomentId(moment.value?.momentId?.toInt() ?: 0)
			.doOnApiSuccess {
				moment.postValue(it.data)
			}
	}
	
	//点赞或取消点赞
	fun like() {
		momentService.pushCommentOrLike(
			MomentCommentRequestModel(
				commentType = COMMENT_LIKE,
				momentId = moment.value?.momentId
			)
		).doOnApiSuccess { fetchMomentDetail() }
	}
	
	//评论
	fun pushComment(momentId: Long) {
		momentService.pushCommentOrLike(
			MomentCommentRequestModel(
				commentType = COMMENT_COMMENT,
				content = inputComment.value,
				momentId = momentId
			)
		).doOnApiSuccess { fetchMomentDetail() }
	}
	
	//回复评论
	fun replyComment(commentId: Long?) {
		momentService.replyComment(
			ReplyCommentRequestModel(
				commentId = commentId,
				content = inputComment.value ?: ""
			)
		)
			.doOnApiSuccess { fetchMomentDetail() }
	}
	
	
}