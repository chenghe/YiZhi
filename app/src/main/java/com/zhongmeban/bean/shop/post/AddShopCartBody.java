package com.zhongmeban.bean.shop.post;

/**
 * Created by User on 2016/10/24.
 */

public class AddShopCartBody {

   private Long productId ;

    private int quantity;

    private  Long memberId;

    private boolean isChecked;


    public AddShopCartBody(Long productId, int quantity, Long memberId, boolean isChecked) {
        this.productId = productId;
        this.quantity = quantity;
        this.memberId = memberId;
        this.isChecked = isChecked;
    }
}
