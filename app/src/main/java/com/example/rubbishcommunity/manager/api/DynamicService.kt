package com.example.fenrir_stage4.manager.api

import com.example.rubbishcommunity.model.Dynamic
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DynamicService {


    @GET("hotWords?")
    fun getHotWordList(@Query("offset") offset: Int): Single<List<String>>

    @GET("dynamics?")
    fun getDynamicList(@Query("offset") offset: Int): Single<List<Dynamic>>


    @GET("dynamic?")
    fun getDynamic(@Query("dynamicId") Id: String): Single<Dynamic>


}