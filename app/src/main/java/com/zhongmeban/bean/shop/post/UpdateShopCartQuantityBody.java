package com.zhongmeban.bean.shop.post;

/**
 * Created by User on 2016/10/24.
 *
 * 更新购物车数量
 */

public class UpdateShopCartQuantityBody {

   private Long id  ;

    private int quantity;

    private  Long cartId ;


    public UpdateShopCartQuantityBody(Long id, int quantity, Long cartId) {
        this.id = id;
        this.quantity = quantity;
        this.cartId = cartId;
    }

}
