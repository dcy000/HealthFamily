package com.gzq.lib_core.utils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.gzq.lib_core.http.exception.ErrorTransformer;
import com.gzq.lib_core.http.model.BaseModel;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RxUtils {
    static {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    /**
     * 默认回调到主线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<BaseModel<T>, T> httpResponseTransformer() {
        return new ObservableTransformer<BaseModel<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseModel<T>> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .compose(ErrorTransformer.<T>getInstance())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 自己决定是否回调到主线程
     *
     * @param isObserveOnMain
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<BaseModel<T>, T> httpResponseTransformer(final boolean isObserveOnMain) {
        return new ObservableTransformer<BaseModel<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseModel<T>> upstream) {
                if (isObserveOnMain) {
                    return upstream.subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.newThread())
                            .compose(ErrorTransformer.<T>getInstance())
                            .observeOn(AndroidSchedulers.mainThread());
                }
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .compose(ErrorTransformer.<T>getInstance());
            }
        };
    }

    public static <T> AutoDisposeConverter<T> autoDisposeConverter(LifecycleOwner owner) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner));
    }

    public static <T> AutoDisposeConverter<T> autoDisposeConverter(LifecycleOwner owner, Lifecycle.Event event) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, event));
    }

    public static Observable<Integer> rxCountDown(int interval, final int times) {
        return Observable.interval(0, interval, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return times - aLong.intValue();
                    }
                })
                .take(times + 1);
    }
}
