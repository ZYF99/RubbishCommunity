package com.example.fenrir_stage4

import android.app.Application
import com.example.fenrir_stage4.manager.base.apiModule
import com.example.fenrir_stage4.utils.SharedPreferencesUtil
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import rx_activity_result2.RxActivityResult

class MyApplication :Application(),KodeinAware{
    override val kodein = Kodein.lazy {
        import(apiModule)
    }

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtil.getInstance(this,"local")
        RxActivityResult.register(this)
    }

}