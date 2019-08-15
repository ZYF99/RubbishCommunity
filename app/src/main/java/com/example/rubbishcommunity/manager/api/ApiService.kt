package com.example.rubbishcommunity.manager.api

import com.example.rubbishcommunity.model.Dynamic
import com.example.rubbishcommunity.model.Vote
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("hotWords?")
    fun getHotWordList(@Query("offset") offset: Int): Single<List<String>>


    @GET("dynamics?")
    fun getDynamicList(@Query("offset") offset: Int): Single<List<Dynamic>>


    @GET("dynamic?")
    fun getDynamic(@Query("dynamicId") Id: String): Single<Dynamic>


    @GET("votes?")
    fun getVoteList(@Query("offset") offset: Int): Single<List<Vote>>

    @GET("vote?")
    fun getVote(@Query("voteId") Id: String): Single<Vote>

}