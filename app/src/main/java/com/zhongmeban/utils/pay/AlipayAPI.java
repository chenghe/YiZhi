package com.zhongmeban.utils.pay;

import android.app.Activity;
import com.alipay.sdk.app.PayTask;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.zhongmeban.utils.pay.AlipayConfig.RSA_PRIVATE;

public class AlipayAPI {

    private Activity activity;


    public AlipayAPI(Activity act) {
        this.activity = act;
    }


    /**
     *
     */
    public void pay(final IPayResult callBack) {

        Observable.create(new Observable.OnSubscribe<Map<String, String>>() {
            @Override public void call(Subscriber<? super Map<String, String>> subscriber) {

                final PayTask alipay = new PayTask(activity);

                Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap();
                String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
                String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
                final String orderInfo = orderParam + "&" + sign;

                Map<String, String> result = alipay.payV2(orderInfo, true);
                subscriber.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Map<String, String>>() {
                @Override public void call(Map<String, String> s) {
                    callBack.payResult(s);
                }
            });
    }

}
