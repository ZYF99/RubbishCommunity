package com.example.rubbishcommunity.utils

import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 *  for better control the Rx disposable lifecycle
 */

interface BindLife {
    val compositeDisposable: CompositeDisposable

    fun Disposable.bindLife() = addDisposable(this)

    fun Single<*>.bindLife() = subscribe({ }, { Timber.e(it, "Single error") }).bindLife()

    fun Maybe<*>.bindLife() = subscribe({ }, { Timber.e(it, "Maybe error") }).bindLife()

    fun Observable<*>.bindLife() = subscribe({ }, { Timber.e(it, "Observable error") }).bindLife()

    fun Completable.bindLife() = subscribe({ }, { Timber.e(it, "Completable error") }).bindLife()

    fun Flowable<*>.bindLife() = subscribe({ }, { Timber.e(it, "Flowable  error") }).bindLife()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun removeDisposable(disposable: Disposable?) {
        if (disposable != null)
            compositeDisposable.remove(disposable)
    }
    
    fun destroyDisposable() = compositeDisposable.clear()
}