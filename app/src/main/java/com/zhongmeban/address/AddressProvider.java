package com.zhongmeban.address;

import java.util.List;

public interface AddressProvider {
    void provideProvinces(AddressReceiver<AddressBean> addressReceiver);
    void provideCitiesWith(int provinceId, AddressReceiver<AddressBean> addressReceiver);
    void provideCountiesWith(int cityId, AddressReceiver<AddressBean> addressReceiver);
    void provideStreetsWith(int countyId, AddressReceiver<AddressBean> addressReceiver);

    interface AddressReceiver<T> {
        void send(List<T> data);
    }
}