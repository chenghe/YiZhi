package com.zhongmeban.bean.shop.post;

import java.util.List;

/**
 * Created by User on 2016/10/24.
 */

public class DeleteShopCartListBody {

   private Long cartId  ;

    private List<Long> cartList;


    public DeleteShopCartListBody(Long cartId, List<Long> cartList) {
        this.cartId = cartId;
        this.cartList = cartList;
    }
}
