package com.example.rubbishcommunity.ui.home.find.moment.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.model.api.moments.*
import com.example.rubbishcommunity.model.api.release.PUBLISH_TYPE_FORWARD
import com.example.rubbishcommunity.model.api.release.ReleaseMomentRequestModel
import com.example.rubbishcommunity.ui.base.BaseViewModel
import org.kodein.di.generic.instance

class MomentDetailViewModel(application: Application) : BaseViewModel(application) {
	
	private val momentService by instance<MomentService>()
	val moment = MutableLiveData<MomentContent>()
	val isRefreshing = MutableLiveData<Boolean>()
	val inputComment = MutableLiveData<String>()
	
	//拉取单个动态
	fun fetchMomentDetail() {
		momentService.fetchMomentsByMomentId(moment.value?.momentId ?: 0)
			.dealLoading()
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
	
	//转发
	fun forward(momentId: Long) {
		momentService.releaseMoment(
			ReleaseMomentRequestModel(
				0.toDouble(),
				0.toDouble(),
				momentId,
				PUBLISH_TYPE_FORWARD
			)
		).doOnApiSuccess { MyApplication.showSuccess("转发成功") }
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
		).doOnApiSuccess { fetchMomentDetail() }
	}
	
	
}