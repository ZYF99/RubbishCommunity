package com.example.rubbishcommunity

import androidx.multidex.MultiDexApplication
import com.example.fenrir_stage4.manager.base.apiModule
import com.example.rubbishcommunity.persistence.SharedPreferencesUtils
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import rx_activity_result2.RxActivityResult
var instance: MyApplication? = null


class MyApplication :MultiDexApplication(),KodeinAware {
    override val kodein = Kodein.lazy {
        import(apiModule)
    }

companion object{
    lateinit var instance: MyApplication
}
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtils.getInstance(this,"local")
        RxActivityResult.register(this)
        instance = this
    }

}