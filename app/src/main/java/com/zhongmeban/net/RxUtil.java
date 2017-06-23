package com.zhongmeban.net;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by User on 2016/11/3.
 */

public class RxUtil {

    public static <T> Observable.Transformer<T, T> normalSchedulers() {

        return new Observable.Transformer<T, T>() {

            @Override public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
