package com.example.rubbishcommunity.manager.api


import com.example.rubbishcommunity.LocationOutClass
import com.example.rubbishcommunity.model.TestCard
import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.search.Category
import com.example.rubbishcommunity.model.api.search.SearchKeyConclusion
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/11/20 13：12
 */
interface RubbishService {
	
	//protobufTest
	@GET("api/test/protobuf/base64")
	@Headers("Content-Type: application/x-protobuf;charset=UTF-8", "Accept: application/x-protobuf")
	fun protobufGetTest(): Single<LocationOutClass.Location>
	
	//根据物品名称搜索分类
	@GET("api/common/tools/{searchKey}/search")
	fun searchClassification(@Path("searchKey") searchKey: String): Single<ResultModel<MutableList<SearchKeyConclusion>>>
	
	//根据sortId搜索分类信息
	@GET("api/common/tools/{classNum}/categories")
	fun searchCategoryByName(@Path("classNum") classNum: Int): Single<ResultModel<Category>>
	
	//随机拉取垃圾分类问题
	@GET("api/common/tools/game/question")
	fun fetchQuestions(): Single<ResultModel<List<TestCard>>>
	
	
}