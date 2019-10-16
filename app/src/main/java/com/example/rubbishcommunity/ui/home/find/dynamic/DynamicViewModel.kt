package com.example.rubbishcommunity.ui.home.find.dynamic

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.ui.BaseViewModel
import com.example.rubbishcommunity.model.Dynamic
import io.reactivex.SingleTransformer
import org.kodein.di.generic.instance


class DynamicViewModel(application: Application) : BaseViewModel(application) {
	
	
	private val dynamicService by instance<ApiService>()
	
	val dynamicList = MutableLiveData<MutableList<Dynamic>>()
	
	val isRefreshing = MutableLiveData<Boolean>()
	
	
	fun init() {
		dynamicList.value = mutableListOf()
		getDynamicList()
	}
	
	
	fun getDynamicList() {
		val gridImgUrls1: MutableList<String> = mutableListOf()
		gridImgUrls1.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		
		val item1 = Dynamic(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls1,
			"231234",
			"2019年7月30日",
			"23123"
		)
		
		
		val gridImgUrls2: MutableList<String> = mutableListOf()
		gridImgUrls2.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		gridImgUrls2.add("http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg")
		val item2 = Dynamic(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls2,
			"231234",
			"2019年7月30日",
			"23123"
		)
		
		val gridImgUrls3: MutableList<String> = mutableListOf()
		gridImgUrls3.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		gridImgUrls3.add("http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg")
		gridImgUrls3.add("http://img0.imgtn.bdimg.com/it/u=1330684766,2510236939&fm=27&gp=0.jpg")
		val item3 = Dynamic(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls3,
			"231234",
			"2019年7月30日",
			"23123"
		)
		
		
		val gridImgUrls4: MutableList<String> = mutableListOf()
		gridImgUrls4.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		gridImgUrls4.add("http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg")
		gridImgUrls4.add("http://img0.imgtn.bdimg.com/it/u=1330684766,2510236939&fm=27&gp=0.jpg")
		gridImgUrls4.add("http://img2.imgtn.bdimg.com/it/u=1272017022,817371530&fm=27&gp=0.jpg")
		val item4 = Dynamic(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls4,
			"231234",
			"2019年7月30日",
			"23123"
		)
		
		
		val gridImgUrls5: MutableList<String> = mutableListOf()
		gridImgUrls5.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		gridImgUrls5.add("http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg")
		gridImgUrls5.add("http://img0.imgtn.bdimg.com/it/u=1330684766,2510236939&fm=27&gp=0.jpg")
		gridImgUrls5.add("http://img2.imgtn.bdimg.com/it/u=1272017022,817371530&fm=27&gp=0.jpg")
		gridImgUrls5.add("http://img1.imgtn.bdimg.com/it/u=1564534394,3601270978&fm=27&gp=0.jpg")
		val item5 = Dynamic(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls5,
			"231234",
			"2019年7月30日",
			"23123"
		)
		
		
		val gridImgUrls6: MutableList<String> = mutableListOf()
		gridImgUrls6.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		gridImgUrls6.add("http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg")
		gridImgUrls6.add("http://img0.imgtn.bdimg.com/it/u=1330684766,2510236939&fm=27&gp=0.jpg")
		gridImgUrls6.add("http://img2.imgtn.bdimg.com/it/u=1272017022,817371530&fm=27&gp=0.jpg")
		gridImgUrls6.add("http://img1.imgtn.bdimg.com/it/u=1564534394,3601270978&fm=27&gp=0.jpg")
		gridImgUrls6.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")
		val item6 = Dynamic(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls6,
			"231234",
			"2019年7月30日",
			"23123"
		)
		
		
		val gridImgUrls7: MutableList<String> = mutableListOf()
		gridImgUrls7.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		gridImgUrls7.add("http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg")
		gridImgUrls7.add("http://img0.imgtn.bdimg.com/it/u=1330684766,2510236939&fm=27&gp=0.jpg")
		gridImgUrls7.add("http://img2.imgtn.bdimg.com/it/u=1272017022,817371530&fm=27&gp=0.jpg")
		gridImgUrls7.add("http://img1.imgtn.bdimg.com/it/u=1564534394,3601270978&fm=27&gp=0.jpg")
		gridImgUrls7.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")
		gridImgUrls7.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")
		val item7 = Dynamic(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls7,
			"231234",
			"2019年7月30日",
			"23123"
		)
		
		
		val gridImgUrls8: MutableList<String> = mutableListOf()
		gridImgUrls8.add("http://b-ssl.duitang.com/uploads/blog/201508/16/20150816193236_kKUfm.jpeg")
		gridImgUrls8.add("http://img0.imgtn.bdimg.com/it/u=2426212861,900117439&fm=27&gp=0.jpg")
		gridImgUrls8.add("http://img0.imgtn.bdimg.com/it/u=1330684766,2510236939&fm=27&gp=0.jpg")
		gridImgUrls8.add("http://img2.imgtn.bdimg.com/it/u=1272017022,817371530&fm=27&gp=0.jpg")
		gridImgUrls8.add("http://img1.imgtn.bdimg.com/it/u=1564534394,3601270978&fm=27&gp=0.jpg")
		gridImgUrls8.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")
		gridImgUrls8.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")
		gridImgUrls8.add("http://img1.imgtn.bdimg.com/it/u=2909240217,602760474&fm=27&gp=0.jpg")
		val item8 = Dynamic(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls8,
			"231234",
			"2019年7月30日",
			"23123"
		)
		
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
		val item9 = Dynamic(
			"dsfsdf",
			"参与绿色行动，保护美丽家园。\n" +
					"\n" +
					"Take part in green action to protect beautiful home.",
			gridImgUrls9,
			"231234",
			"2019年7月30日",
			"23123"
		)
		
		
		val list = mutableListOf(
			item1,
			item2,
			item3,
			item4,
			item5,
			item6,
			item7,
			item8,
			item9
		)
		
		dynamicList.value = list

/*        dynamicService.getDynamicList(0)
            .compose(dealRefresh())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                dynamicList.value = it as MutableList<Dynamic>?
            }
            .compose(dealError())
            .bindLife()*/
		
		
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