package com.example.rubbishcommunity.ui.home.find.dynamic.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.model.Comment
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.model.api.mine.UserCardResultModel
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class DynamicDetailViewModel(application: Application) : BaseViewModel(application) {
	
	
	private val dynamicService by instance<ApiService>()
	
	//标题
	val title = MutableLiveData<String>()
	
	//内容
	val content = MutableLiveData<String>()
	
	//图片列表
	val imgList = MutableLiveData<List<String>>()
	
	//评论列表
	val commentList = MutableLiveData<List<Comment>>()
	
	//位置
	val location = MutableLiveData<String>()
	
	//获赞次数
	val likeNum = MutableLiveData<Int>()
	
	//我输入的评论
	val inputComment = MutableLiveData<String>()
	
	
	val isRefreshing = MutableLiveData<Boolean>()
	
	
	fun init() {
		//模拟数据
		title.postValue("标题标题")
		
		content.postValue(
			"内容内容\n" +
					"内容内容\n" +
					"内容内容\n" +
					"内容内容\n" +
					"内容内容\n"
		)
		val myImglist = listOf(
			"http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg",
			"http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg",
			"http://img0.imgtn.bdimg.com/it/u=1330684766,2510236939&fm=27&gp=0.jpg",
			"http://img2.imgtn.bdimg.com/it/u=1272017022,817371530&fm=27&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=1564534394,3601270978&fm=27&gp=0.jpg",
			"http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg"
		)
		
		imgList.value = myImglist
		val innerComment = Comment(
			UserCardResultModel.getDefault(),
			"我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容",
			"2019年9月8日",
			listOf(
				Comment(
					UserCardResultModel.getDefault(),
					"我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容",
					"2019年9月8日",
					null
				),
				Comment(
					UserCardResultModel.getDefault(),
					"我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容",
					"2019年9月8日",
					null
				),
				Comment(
					UserCardResultModel.getDefault(),
					"我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容",
					"2019年9月8日",
					null
				)
			)
		)
		
		val comment = Comment(
			UserCardResultModel.getDefault(),
			"我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容我是内容",
			"2019年9月8日",
			listOf(
				innerComment,
				innerComment,
				innerComment,
				innerComment,
				innerComment,
				innerComment
			)
		)
		val myCommentList = listOf(
			comment,
			comment,
			comment,
			comment
		)
		commentList.value = myCommentList
		location.postValue("地址地址")
		likeNum.postValue(666)
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