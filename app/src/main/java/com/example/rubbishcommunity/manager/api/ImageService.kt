package com.example.rubbishcommunity.manager.api



import com.example.rubbishcommunity.model.api.GetQiNiuTokenRequestModel
import com.example.rubbishcommunity.model.api.ResultModel
import io.reactivex.Single
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 14：12
 */
interface ImageService {

	//获取七牛云上传图片凭证
	@POST("api/common/image/token")
	fun getQiNiuToken(@Body requestModel: GetQiNiuTokenRequestModel): Single<ResultModel<Map<String, String>>>
	
	
}