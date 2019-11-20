package com.example.rubbishcommunity.manager.base

import com.baidu.location.LocationClient
import com.example.rubbishcommunity.MyApplication
import com.example.rubbishcommunity.utils.initLocationOption
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * @author Zhangyf
 * @version 1.0
 * @date 2019/8/20 13：21
 */

val baiduModule = Kodein.Module {
	bind<LocationClient>() with singleton { initLocationOption(MyApplication.instance) }
}
