package com.example.rubbishcommunity.ui.home.homepage.search.cameraSearch

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.BaiDuIdentifyService
import com.example.rubbishcommunity.manager.dealError
import com.example.rubbishcommunity.model.api.baiduidentify.Thing
import com.example.rubbishcommunity.ui.base.BaseViewModel
import com.example.rubbishcommunity.utils.switchThread
import org.kodein.di.generic.instance


class CameraSearchViewModel(application: Application) : BaseViewModel(application) {
	
	val thingList = MutableLiveData(mutableListOf<Thing>())
	
	private val baiduIdentifyService by instance<BaiDuIdentifyService>()
	
	//识别图片物体
	fun identifyThingName(imgArrayStr: String) {
		baiduIdentifyService.getToken()
			.flatMap {
				baiduIdentifyService.getIdentifyThing(
					it.accessToken,
					imgArrayStr
				)
			}
			.switchThread()
			.compose(dealError())
			.doOnSuccess {
				it.result.sortByDescending {thing ->
					thing.score
				}
				thingList.postValue(it.result)
			}.bindLife()
		
	}
	
	
}