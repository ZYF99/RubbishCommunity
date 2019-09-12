package com.example.rubbishcommunity.ui.find.vote

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.rubbishcommunity.manager.api.ApiService
import com.example.rubbishcommunity.base.BaseViewModel
import com.example.rubbishcommunity.model.Vote
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.generic.instance


class VoteViewModel(application: Application) : BaseViewModel(application) {


    private val apiService by instance<ApiService>()

    val voteList = MutableLiveData<MutableList<Vote>>()

    val refreshing = MutableLiveData<Boolean>()


    fun init() {
        voteList.value = mutableListOf()
        getVoteList()

    }

    fun getVoteList() {

        val item = Vote("Take part in green action to protect beautiful home.")

        val list = mutableListOf(item, item, item, item, item, item)

        voteList.value = list

        apiService.getVoteList(0)
            .compose(dealRefresh())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                voteList.value = it as MutableList<Vote>?
            }
            .compose(dealError())
            .bindLife()
    }


    private fun <T> dealRefresh(): SingleTransformer<T, T> {
        return SingleTransformer { obs ->
            obs
                .doOnSubscribe { refreshing.postValue(true) }
                .doOnSuccess { refreshing.postValue(false) }
                .doOnError { refreshing.postValue(false) }
        }
    }


}