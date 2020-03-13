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
	
	//点赞或取消点赞
	fun like(momentId: Long, onLikedAction: (Long) -> Unit) {
		momentService.pushCommentOrLike(
			MomentCommentRequestModel(
				commentType = COMMENT_LIKE,
				momentId = momentId
			)
		)
			.doOnApiSuccess { onLikedAction.invoke(it.data.commentId) }
	}
	
	//评论
	fun pushComment(momentId: Long, onLikedAction: (Long) -> Unit) {
		momentService.pushCommentOrLike(
			MomentCommentRequestModel(
				commentType = COMMENT_COMMENT,
				content = inputComment.value,
				momentId = momentId
			)
		)
			.doOnApiSuccess { onLikedAction.invoke(it.data.commentId) }
	}
	
	//回复评论
	fun replyComment(commentId: Long?, onLikedAction: () -> Unit) {
		momentService.replyComment(
			ReplyCommentRequestModel(
				commentId = commentId,
				content = inputComment.value?:""
			)
		)
			.doOnApiSuccess { onLikedAction.invoke() }
	}
	
	private fun <T> dealRefresh(): SingleTransformer<T, T> {
		return SingleTransformer { obs ->
			obs
				.doOnSubscribe { isRefreshing.postValue(true) }
				.doOnSuccess { isRefreshing.postValue(false) }
				.doOnError { isRefreshing.postValue(false) }
		}
	}
	
	
}