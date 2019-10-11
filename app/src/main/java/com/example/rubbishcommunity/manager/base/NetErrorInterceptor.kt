package com.example.rubbishcommunity.manager.base

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetErrorInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain?): Response? {
        if (chain == null)
            return null

        val response = chain.proceed(chain.request())
        
        
        if (response.code() in 400..503)
            throw ServerError(response.code(),response.message())

        return response
    }
}

data class ServerError(val code: Int,val msg:String) : RuntimeException()