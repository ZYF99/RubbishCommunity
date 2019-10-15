package com.example.rubbishcommunity.manager.base

import com.baidu.location.LocationClient
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.utils.initLocationOption
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


val baiduModule = Kodein.Module {
	bind<LocationClient>() with singleton { initLocationOption(MyApplication.instance) }
}
