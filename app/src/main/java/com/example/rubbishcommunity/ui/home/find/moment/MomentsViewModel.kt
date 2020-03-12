package com.example.rubbishcommunity.ui.home.find.moment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.model.api.moments.*
import com.example.rubbishcommunity.model.api.release.PUBLISH_TYPE_FORWARD
import com.example.rubbishcommunity.model.api.release.ReleaseMomentRequestModel
import com.example.rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.Single
import org.kodein.di.generic.instance

class MomentsViewModel(application: Application) : BaseViewModel(application) {
	private val momentService by instance<MomentService>()
	val momentList = MutableLiveData<List<MomentContent>>()
	private val startPage = MutableLiveData(1)
	val isRefreshing = MutableLiveData(false)
	val isLoadingMore = MutableLiveData(false)
	private val isLastPage = MutableLiveData(false)
	val classify = MutableLiveData(CLASSIFY_DYNAMIC)
	
	//刷新
	fun refreshMoments() {
		momentService.fetchMomentsByClassify(
			GetMomentsByClassifyRequestModel(classify.value!!, PageParam(1, 10))
		).dealRefresh()
			.doOnApiSuccess {
				isLastPage.postValue(it.data.pageInfoResp.lastPage)
				momentList.postValue(it.data.momentContentList)
				startPage.postValue(2)
			}
	}
	
	///加载更多
	fun loadMoreMoments() {
		momentService.fetchMomentsByClassify(
			GetMomentsByClassifyRequestModel(
				classify.value!!,
				PageParam(startPage.value!!, 10)
			)
		).dealLoading()
			.doOnApiSuccess {
				isLastPage.postValue(it.data.pageInfoResp.lastPage)
				if (it.data.momentContentList.isNotEmpty())
					momentList.postValue(
						momentList.value
							?.toMutableList()
							?.apply {
								addAll(it.data.momentContentList)
							})
				startPage.postValue(startPage.value!! + 1)
			}
	}
	
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
	
	//转发
	fun forward(moment: MomentContent) {
		momentService.releaseMoment(
			ReleaseMomentRequestModel(
				0.toDouble(),
				0.toDouble(),
				moment.momentId,
				PUBLISH_TYPE_FORWARD
			)
		).doOnApiSuccess { MyApplication.showSuccess("转发成功") }
	}
	
	private fun <T> Single<T>.dealRefresh() =
		doOnSubscribe { isRefreshing.postValue(true) }
			.doFinally { isRefreshing.postValue(false) }
	
	
	private fun <T> Single<T>.dealLoading() =
		doOnSubscribe { isLoadingMore.postValue(true) }
			.doFinally { isLoadingMore.postValue(false) }
	
}