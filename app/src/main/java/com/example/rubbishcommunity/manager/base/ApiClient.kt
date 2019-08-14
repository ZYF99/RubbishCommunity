package com.example.fenrir_stage4.manager.base


import com.example.rubbishcommunity.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient private constructor(private val retrofit: Retrofit, val okHttpClient: OkHttpClient) {

    fun <S> createService(serviceClass: Class<S>): S = retrofit.create(serviceClass)

    class Builder(
        //val apiAuthorizations: MutableMap<String, Interceptor> = LinkedHashMap(),
        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder(),
        private val adapterBuilder: Retrofit.Builder = Retrofit.Builder()
    ) {

        fun build(url: String? = null): ApiClient {
            val baseUrl = url ?: BuildConfig.BASE_URL
/*                .let { url ->
                    if (!url.endsWith("/"))
                        "$url/"
                    else
                        url
                }
                .let { url ->
                    if (!url.startsWith("http"))
                        "https://$url"
                    else
                        url
                }*/

            adapterBuilder
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())

            val client = okBuilder.build()
            val retrofit = adapterBuilder.client(client).build()
            return ApiClient(retrofit, client)
        }
    }

}