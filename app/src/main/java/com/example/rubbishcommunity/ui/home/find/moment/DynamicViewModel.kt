package com.example.rubbishcommunity.ui.home.find.moment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.MomentService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.manager.dealErrorCode
import com.example.rubbishcommunity.model.api.LocationReq
import com.example.rubbishcommunity.model.api.moments.GetMomentsRequestModel
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.model.api.moments.Moment
import com.example.rubbishcommunity.model.api.moments.PageParam
import com.example.rubbishcommunity.utils.switchThread
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class DynamicViewModel(application: Application) : BaseViewModel(application) {
	
	private val dynamicService by instance<MomentService>()
	
	val dynamicList = MutableLiveData<MutableList<Moment>>()
	
	val isRefreshing = MutableLiveData(false)
	
	
	fun getDynamicList() {
		mockData()
		
		return dynamicService.fetchMoments(
			GetMomentsRequestModel(LocationReq(), PageParam(2, 10, null))
		)
			.compose(dealRefresh())
			.switchThread()
			.doOnSuccess {
				//dynamicList.value = it
			}
			.compose(dealErrorCode())
			.compose(dealError())
			.bindLife()
	}
	
	
	private fun mockData() {
		val gridImgUrls9: MutableList<String> = mutableListOf()
		gridImgUrls9.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		gridImgUrls9.add("http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg")
		gridImgUrls9.add("http://img0.imgtn.bdimg.com/it/u=1330684766,2510236939&fm=27&gp=0.jpg")
		gridImgUrls9.add("http://img2.imgtn.bdimg.com/it/u=1272017022,817371530&fm=27&gp=0.jpg")
		gridImgUrls9.add("http://img1.imgtn.bdimg.com/it/u=1564534394,3601270978&fm=27&gp=0.jpg")
		gridImgUrls9.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")
		gridImgUrls9.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")
		gridImgUrls9.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")
		gridImgUrls9.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")
		val item9 = Moment(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls9,
			"231234",
			"2019年7月30日",
			"23123"
		)
		
		val gridImgUrls2: MutableList<String> = mutableListOf()
		gridImgUrls2.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		gridImgUrls2.add("http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg")
		
		val item2 = Moment(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls2,
			"231234",
			"2019年7月30日",
			"dsafaerwg"
		)
		
		val gridImgUrls3: MutableList<String> = mutableListOf()
		gridImgUrls3.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		
		val item3 = Moment(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls3,
			"231234",
			"2019年7月30日",
			"dsafaerwg"
		)
		
		dynamicList.value = mutableListOf(
			item9,
			item2,
			item3
		)
		
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