package com.example.rubbishcommunity

import androidx.multidex.MultiDexApplication
import com.example.fenrir_stage4.manager.base.apiModule
import com.example.fenrir_stage4.utils.SharedPreferencesUtil
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import rx_activity_result2.RxActivityResult

class MyApplication :MultiDexApplication(),KodeinAware {
    override val kodein = Kodein.lazy {
        import(apiModule)
    }

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtil.getInstance(this,"local")
        RxActivityResult.register(this)
    }

}