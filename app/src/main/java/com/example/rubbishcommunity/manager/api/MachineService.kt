package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.api.ResultModel
import com.example.rubbishcommunity.model.api.guide.CompleteInfoRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterRequestModel
import com.example.rubbishcommunity.model.api.guide.LoginOrRegisterResultModel
import com.example.rubbishcommunity.model.api.machine.BindMachineRequestModel
import com.example.rubbishcommunity.model.api.mine.UserCardResultModel
import com.example.rubbishcommunity.model.api.mine.UsrProfileResp
import com.example.rubbishcommunity.model.api.password.ResetPasswordRequestModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/10/20 14：12
 */
interface MachineService {
	
	//绑定硬件设备
	@POST("api/IoTDA/bind")
	fun bindMachine(
		@Body BindMachineRequestModel: BindMachineRequestModel
	): Single<ResponseBody>
	
	
}