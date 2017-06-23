package com.zhongmeban.address;

import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.net.HttpShopMethods;
import java.util.List;
import rx.Subscriber;

public class DefaultAddressProvider implements AddressProvider {
    @Override
    public void provideProvinces(final AddressReceiver<AddressBean> addressReceiver) {

        HttpShopMethods.getInstance().getAddressInfo(new Subscriber<HttpResult<List<AddressBean>>>() {
            @Override public void onCompleted() {

            }


            @Override public void onError(Throwable e) {

            }


            @Override public void onNext(HttpResult<List<AddressBean>> addressBeanHttpResult) {
                addressReceiver.send(addressBeanHttpResult.data);
            }
        }, 0);

    }


    @Override
    public void provideCitiesWith(int provinceId, final AddressReceiver<AddressBean> addressReceiver) {
        HttpShopMethods.getInstance().getAddressInfo(new Subscriber<HttpResult<List<AddressBean>>>() {
            @Override public void onCompleted() {

            }


            @Override public void onError(Throwable e) {

            }


            @Override public void onNext(HttpResult<List<AddressBean>> addressBeanHttpResult) {
                addressReceiver.send(addressBeanHttpResult.data);
            }
        }, provinceId);

    }


    @Override
    public void provideCountiesWith(int cityId, final AddressReceiver<AddressBean> addressReceiver) {
        HttpShopMethods.getInstance().getAddressInfo(new Subscriber<HttpResult<List<AddressBean>>>() {
            @Override public void onCompleted() {

            }


            @Override public void onError(Throwable e) {

            }


            @Override public void onNext(HttpResult<List<AddressBean>> addressBeanHttpResult) {
                addressReceiver.send(addressBeanHttpResult.data);
            }
        }, cityId);

    }


    @Override
    public void provideStreetsWith(int countyId, final AddressReceiver<AddressBean> addressReceiver) {
        HttpShopMethods.getInstance().getAddressInfo(new Subscriber<HttpResult<List<AddressBean>>>() {
            @Override public void onCompleted() {

            }


            @Override public void onError(Throwable e) {

            }


            @Override public void onNext(HttpResult<List<AddressBean>> addressBeanHttpResult) {
                addressReceiver.send(addressBeanHttpResult.data);
            }
        }, countyId);

    }
}
