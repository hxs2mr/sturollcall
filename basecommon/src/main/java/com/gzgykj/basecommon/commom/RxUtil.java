package com.gzgykj.basecommon.commom;

import android.text.TextUtils;
import android.util.Log;


import com.gzgykj.basecommon.exception.ApiException;
import com.gzgykj.basecommon.response.AppRespose;
import com.gzgykj.basecommon.utils.GsonUtil;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * **********************
 * 功 能:RX 转换类
 * *********************
 */

public class RxUtil {
    /**
     * compose简化线程 统一线程处理
     * FlowableTransformer
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerObservableHelper() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

//            @Override
//            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
//                return upstream.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
        };
    }

    public static <T> FlowableTransformer<T, T> rxSchedulerFlowableHelper() {
        return new FlowableTransformer<T, T>() {

            @Override
            public Publisher<T> apply(@NonNull Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 处理Gank 接口返回的结果
     * FlowableTransformer
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<AppRespose<T>, T> handlerLYLResult() {
        return new ObservableTransformer<AppRespose<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<AppRespose<T>> upstream) {

                return upstream.flatMap(new Function<AppRespose<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull AppRespose<T> lylResponse) throws
                            Exception {
                        // TODO: 2018-01-16  删除输出
                        Log.e("response" ,""+ GsonUtil.obj2JSON(lylResponse));
                        int status = lylResponse.getCode();
                         if (status== 0) {
                            if (lylResponse.getData() == null){
                                return Observable.error(new ApiException(TextUtils.isEmpty
                                        (lylResponse.getMsg()) ? "服务端异常" : lylResponse.getMsg(),
                                        lylResponse.getCode()));
                            }else {
                                return createObservable(lylResponse.getData());
                            }
                        }else if(status==1||status==-1)
                        {
                            return Observable.error(new ApiException(TextUtils.isEmpty
                                    (lylResponse.getMsg()) ? "服务端异常" : lylResponse.getMsg(),
                                    lylResponse.getCode()));
                        }else {
                            return createObservable(lylResponse.getData());
                        }
//                        else {
//                            return Observable.error(new ApiException(TextUtils.isEmpty
//                                    (lylResponse.getMsg()) ? "网络连接错误2" : lylResponse.getMsg(),
//                                    lylResponse.getStatus()));
//                        }
                    }
                });
            }
        };
    }


    /**
     * 生成Flowable
     *
     * @param results
     * @param <T>
     * @return
     */
    private static <T> Publisher<T> createFlowable(final T results) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<T> e) throws Exception {
                try {
                    e.onNext(results);
                    e.onComplete();
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    private static <T> ObservableSource<T> createObservable_two(final T results) {
        return Observable.create(new ObservableOnSubscribe<T>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                try {
                    e.onNext(results);
                    e.onComplete();
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        });
    }

    private static <T> ObservableSource<T> createObservable(final T results) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                try {
                    e.onNext(results);
                    e.onComplete();
                } catch (Exception ex) {
                    e.onError(ex);

                }
            }
        });
    }


}
