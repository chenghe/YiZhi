package com.zhongmeban.bean.shop.post;

/**
 * Created by User on 2016/10/24.
 *
 * 更新购物车数量
 */

public class UpdateShopCartCheckBody {


    private  Long cartId ;//购物车id
    private  Long id ;//购物车条目id


    public UpdateShopCartCheckBody(Long cartId, Long id, boolean isChecked, boolean isAll) {
        this.cartId = cartId;
        this.id = id;
        this.isChecked = isChecked;
        this.isAll = isAll;
    }


    private boolean isChecked;
    private boolean isAll ;
}
