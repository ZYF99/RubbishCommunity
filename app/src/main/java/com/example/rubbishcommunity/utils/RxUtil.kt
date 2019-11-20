package com.example.rubbishcommunity.utils

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

class SwitchTransform<T> internal constructor(
    private val subscribeOn: Scheduler,
    private val observeOn: Scheduler
) : ObservableTransformer<T, T>,
    FlowableTransformer<T, T>,
    MaybeTransformer<T, T>,
    SingleTransformer<T, T>,
    CompletableTransformer {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.subscribeOn(subscribeOn)
            .observeOn(observeOn)
    }

    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(subscribeOn)
            .observeOn(observeOn)
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream.subscribeOn(subscribeOn)
            .observeOn(observeOn)
    }

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.subscribeOn(subscribeOn)
            .observeOn(observeOn)
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.subscribeOn(subscribeOn)
            .observeOn(observeOn)
    }
}

fun <T> switchThread(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
) = SwitchTransform<T>(subscribeOn, observeOn)

/**
 *
 */

fun <T> Single<T>.switchThread(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Single<T> = compose(SwitchTransform<T>(subscribeOn, observeOn))

fun <T> Flowable<T>.switchThread(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Flowable<T> = compose(SwitchTransform<T>(subscribeOn, observeOn))

fun <T> Maybe<T>.switchThread(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Maybe<T> = compose(SwitchTransform<T>(subscribeOn, observeOn))

fun <T> Observable<T>.switchThread(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Observable<T> = compose(SwitchTransform<T>(subscribeOn, observeOn))

fun <T> Completable.switchThread(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Completable = compose(SwitchTransform<T>(subscribeOn, observeOn))

/**
 *
 */

//fun <T> Single<T>.handleError(context: Context): Single<T> =
//    compose(autoHandleError<T>(context))
//
//fun <T> Completable.handleError(context: Context): Completable =
//    compose(autoHandleError<T>(context))
//
//fun <T> Flowable<T>.handleError(context: Context): Flowable<T> =
//    compose(autoHandleError<T>(context))
//
//fun <T> Maybe<T>.handleError(context: Context): Maybe<T> =
//    compose(autoHandleError<T>(context))
//
//fun <T> Observable<T>.handleError(context: Context): Observable<T> =
//    compose(autoHandleError<T>(context))

/**
 *
 */

//fun <T> Single<T>.showProgressDialog(context: Context): Single<T> =
//    compose(DialogUtil.autoProgressDialog(context))
//
//fun <T> Maybe<T>.showProgressDialog(context: Context): Maybe<T> =
//    compose(DialogUtil.autoProgressDialogMaybe(context))
//
//fun <T> Observable<T>.showProgressDialog(context: Context): Observable<T> =
//    compose(DialogUtil.autoProgressDialogObs<T>(context))
//
//fun Completable.showProgressDialog(context: Context): Completable =
//    compose(DialogUtil.autoProgressDialogCompletable(context))
//
//fun <T> Flowable<T>.showProgressDialog(context: Context): Flowable<T> =
//    compose(DialogUtil.autoProgressDialogFlow(context))