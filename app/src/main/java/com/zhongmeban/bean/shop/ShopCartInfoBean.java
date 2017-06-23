package com.zhongmeban.bean.shop;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2016/10/24.
 */

public class ShopCartInfoBean implements Serializable {

    /**
     * message :
     * id : 10
     * cartItems : [{"id":8,"quantity":1,"product":{"id":15,"sn":"20150101102_7","price":5200,"cost":5200,"marketPrice":6240,"rewardPoint":5200,"exchangePoint":5200,"stock":100,"allocatedStock":0,"isDefault":true,"goodsId":2,"goodsName":"苹果
     * iPhone 6","image":"http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg","specificationValues":[{"id":5,"value":"金色"},{"id":6,"value":"16GB"}]},"isChecked":false}]
     */

    private String message;
    private Long id; //购物车id
    /**
     * id : 8
     * quantity : 1
     * product : {"id":15,"sn":"20150101102_7","price":5200,"cost":5200,"marketPrice":6240,"rewardPoint":5200,"exchangePoint":5200,"stock":100,"allocatedStock":0,"isDefault":true,"goodsId":2,"goodsName":"苹果
     * iPhone 6","image":"http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg","specificationValues":[{"id":5,"value":"金色"},{"id":6,"value":"16GB"}]}
     * isChecked : false
     */

    private List<CartItemsBean> cartItems;


    public String getMessage() { return message;}


    public void setMessage(String message) { this.message = message;}


    public Long getId() { return id;}


    public void setId(Long id) { this.id = id;}


    public List<CartItemsBean> getCartItems() { return cartItems;}


    public void setCartItems(List<CartItemsBean> cartItems) { this.cartItems = cartItems;}


    public static class CartItemsBean implements Serializable, Comparable {
        private Long  id; //购物车item的id
        private int quantity;
        /**
         * id : 15
         * sn : 20150101102_7
         * price : 5200
         * cost : 5200
         * marketPrice : 6240
         * rewardPoint : 5200
         * exchangePoint : 5200
         * stock : 100
         * allocatedStock : 0
         * isDefault : true
         * goodsId : 2
         * goodsName : 苹果 iPhone 6
         * image : http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg
         * specificationValues : [{"id":5,"value":"金色"},{"id":6,"value":"16GB"}]
         */

        private ProductBean product;
        private boolean isChecked;


        public Long getId() { return id;}


        public void setId(Long id) { this.id = id;}


        public int getQuantity() { return quantity;}


        public void setQuantity(int quantity) { this.quantity = quantity;}


        public ProductBean getProduct() { return product;}


        public void setProduct(ProductBean product) { this.product = product;}


        public boolean isIsChecked() { return isChecked;}


        public void setIsChecked(boolean isChecked) { this.isChecked = isChecked;}



        @Override public int compareTo(Object another) {
            if (another instanceof CartItemsBean) {
                CartItemsBean cartItemsBean = (CartItemsBean) another;

                return (int)(cartItemsBean.id - this.id);
            }
            return 0;
        }


        public static class ProductBean implements Serializable {
            private int id;
            private String sn;
            private int price;
            private int cost;
            private int marketPrice;
            private int rewardPoint;
            private int exchangePoint;
            private int stock;
            private int allocatedStock;
            private boolean isDefault;
            private int goodsId;
            private String goodsName;
            private String image;
            /**
             * id : 5
             * value : 金色
             */

            private List<SpecificationValuesBean> specificationValues;


            public int getId() { return id;}


            public void setId(int id) { this.id = id;}


            public String getSn() { return sn;}


            public void setSn(String sn) { this.sn = sn;}


            public int getPrice() { return price;}


            public void setPrice(int price) { this.price = price;}


            public int getCost() { return cost;}


            public void setCost(int cost) { this.cost = cost;}


            public int getMarketPrice() { return marketPrice;}


            public void setMarketPrice(int marketPrice) { this.marketPrice = marketPrice;}


            public int getRewardPoint() { return rewardPoint;}


            public void setRewardPoint(int rewardPoint) { this.rewardPoint = rewardPoint;}


            public int getExchangePoint() { return exchangePoint;}


            public void setExchangePoint(int exchangePoint) { this.exchangePoint = exchangePoint;}


            public int getStock() { return stock;}


            public void setStock(int stock) { this.stock = stock;}


            public int getAllocatedStock() { return allocatedStock;}


            public void setAllocatedStock(int allocatedStock) {
                this.allocatedStock = allocatedStock;
            }


            public boolean isIsDefault() { return isDefault;}


            public void setIsDefault(boolean isDefault) { this.isDefault = isDefault;}


            public int getGoodsId() { return goodsId;}


            public void setGoodsId(int goodsId) { this.goodsId = goodsId;}


            public String getGoodsName() { return goodsName;}


            public void setGoodsName(String goodsName) { this.goodsName = goodsName;}


            public String getImage() { return image;}


            public void setImage(String image) { this.image = image;}


            public List<SpecificationValuesBean> getSpecificationValues() { return specificationValues;}


            public void setSpecificationValues(List<SpecificationValuesBean> specificationValues) {
                this.specificationValues = specificationValues;
            }


            public static class SpecificationValuesBean implements Serializable {
                private int id;
                private String value;


                public int getId() { return id;}


                public void setId(int id) { this.id = id;}


                public String getValue() { return value;}


                public void setValue(String value) { this.value = value;}
            }
        }


        public CartItemsBean(Long id, int quantity, ProductBean product, boolean isChecked) {
            this.id = id;
            this.quantity = quantity;
            this.product = product;
            this.isChecked = isChecked;
        }
    }
}
