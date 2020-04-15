package com.example.rubbishcommunity.utils

import com.example.rubbishcommunity.manager.api.IpService
import com.example.rubbishcommunity.model.api.ip.IpAddressResultModel
import com.example.rubbishcommunity.ui.base.BaseViewModel


fun BaseViewModel.getAddressFromIp(ipService: IpService,action:(String)->Unit) {
	ipService.getAddress()
		.switchThread()
		.doOnSuccess{
			val temp = it.string()
			val s = temp.replace("var returnCitySN = ","")
			val json = s.replace(";","")
			val cityTemp = globalGson.fromJson<IpAddressResultModel>(json,IpAddressResultModel::class.java)
				.cname
			action(cityTemp)
	}.bindLife()

}