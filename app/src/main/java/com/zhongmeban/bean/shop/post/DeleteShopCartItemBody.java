package com.zhongmeban.bean.shop.post;

/**
 * Created by User on 2016/10/24.
 */

public class DeleteShopCartItemBody {

   private Long cartId  ;

    private  Long id;


    public DeleteShopCartItemBody(Long cartId, Long id) {
        this.cartId = cartId;
        this.id = id;
    }
}
