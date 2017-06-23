package com.zhongmeban.bean.shop;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2016/10/18.
 */

public class ProductDetailBean implements Serializable {

    /**
     * id : 2
     * name : 苹果 iPhone 6
     * caption : 至大至薄，强劲动力
     * type :
     * price : 5200
     * marketPrice : 6240
     * image : http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg
     * unit : 台
     * weight : 12.55
     * isMarketable : true
     * isList : true
     * isTop : true
     * isDelivery : true
     * introduction :
     * memo :
     * keyword :
     * seoTitle :
     * seoKeywords :
     * seoDescription :
     * score : 20
     * totalScore : 20
     * scoreCount : 20
     * generateMethod :
     * productCategory : “”
     * brand : “”
     * productImages : [{"title":"\u201c\u201d","source":"http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-source.jpg","large":"http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-large.jpg","medium":"http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-medium.jpg","thumbnail":"http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg","order":1},{"title":"\u201c\u201d","source":"http://image.demo.shopxx.net/4.0/201501/4146d7e4-1be0-4341-8718-91dd553c696c-source.jpg","large":"http://image.demo.shopxx.net/4.0/201501/4146d7e4-1be0-4341-8718-91dd553c696c-large.jpg","medium":"http://image.demo.shopxx.net/4.0/201501/4146d7e4-1be0-4341-8718-91dd553c696c-medium.jpg","thumbnail":"http://image.demo.shopxx.net/4.0/201501/4146d7e4-1be0-4341-8718-91dd553c696c-thumbnail.jpg","order":2}]
     * parameterValues : [{"group":"基本参数","entries":[{"name":"操作系统","value":"iOS"},{"name":"电池类型","value":"锂电池"}]},{"group":"显示","entries":[{"name":"屏幕尺寸","value":"4.7英寸"},{"name":"触摸屏类型","value":"电容屏、多点触控"},{"name":"分辨率","value":"1334
     * x 750"}]}]
     * specificationItems : [{"name":"颜色","entries":[{"id":0,"value":"黑色","isSelected":false},{"id":1,"value":"白色","isSelected":false},{"id":2,"value":"银色","isSelected":true},{"id":3,"value":"灰色","isSelected":true},{"id":4,"value":"红色","isSelected":false},{"id":5,"value":"金色","isSelected":true}]},{"name":"内存容量","entries":[{"id":6,"value":"16GB","isSelected":true},{"id":7,"value":"32GB","isSelected":false},{"id":8,"value":"64GB","isSelected":true},{"id":9,"value":"128GB","isSelected":true}]}]
     * tags : []
     * products : [{"id":9,"sn":"20150101102_1","price":5200,"cost":200,"marketPrice":6240,"rewardPoint":5200,"exchangePoint":4522,"stock":100,"allocatedStock":0,"isDefault":false,"specificationValues":[{"id":2,"value":"银色"},{"id":6,"value":"16GB"}]},{"id":16,"sn":"20150101102_8","price":6100,"cost":null,"marketPrice":7320,"rewardPoint":6100,"exchangePoint":1251,"stock":100,"allocatedStock":0,"isDefault":false,"specificationValues":[{"id":5,"value":"金色"},{"id":8,"value":"64GB"}]},{"id":13,"sn":"20150101102_5","price":6100,"cost":null,"marketPrice":7320,"rewardPoint":6100,"exchangePoint":4522,"stock":100,"allocatedStock":0,"isDefault":false,"specificationValues":[{"id":3,"value":"灰色"},{"id":8,"value":"64GB"}]},{"id":14,"sn":"20150101102_6","price":6800,"cost":null,"marketPrice":8160,"rewardPoint":6800,"exchangePoint":null,"stock":100,"allocatedStock":0,"isDefault":false,"specificationValues":[{"id":3,"value":"灰色"},{"id":9,"value":"128GB"}]},{"id":17,"sn":"20150101102_9","price":6800,"cost":null,"marketPrice":8160,"rewardPoint":6800,"exchangePoint":null,"stock":100,"allocatedStock":0,"isDefault":false,"specificationValues":[{"id":5,"value":"金色"},{"id":9,"value":"128GB"}]},{"id":10,"sn":"20150101102_2","price":6100,"cost":null,"marketPrice":7320,"rewardPoint":6100,"exchangePoint":null,"stock":100,"allocatedStock":0,"isDefault":false,"specificationValues":[{"id":2,"value":"银色"},{"id":8,"value":"64GB"}]},{"id":15,"sn":"20150101102_7","price":5200,"cost":null,"marketPrice":6240,"rewardPoint":5200,"exchangePoint":null,"stock":100,"allocatedStock":0,"isDefault":true,"specificationValues":[{"id":5,"value":"金色"},{"id":6,"value":"16GB"}]},{"id":12,"sn":"20150101102_4","price":5200,"cost":null,"marketPrice":6240,"rewardPoint":5200,"exchangePoint":null,"stock":100,"allocatedStock":0,"isDefault":false,"specificationValues":[{"id":3,"value":"灰色"},{"id":6,"value":"16GB"}]},{"id":11,"sn":"20150101102_3","price":6800,"cost":null,"marketPrice":8160,"rewardPoint":6800,"exchangePoint":null,"stock":100,"allocatedStock":0,"isDefault":false,"specificationValues":[{"id":2,"value":"银色"},{"id":9,"value":"128GB"}]}]
     */

    private int id;
    private String name;
    private String caption;
    private String type;
    private int price;
    private int marketPrice;
    private String image;
    private String unit;
    private double weight;
    private boolean isMarketable;
    private boolean isList;
    private boolean isTop;
    private boolean isDelivery;
    private String introduction;
    private String memo;
    private String keyword;
    private String seoTitle;
    private String seoKeywords;
    private String seoDescription;
    private int score;
    private int totalScore;
    private int scoreCount;
    private String generateMethod;
    private String productCategory;
    private String brand;
    /**
     * title : “”
     * source : http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-source.jpg
     * large : http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-large.jpg
     * medium : http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-medium.jpg
     * thumbnail : http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg
     * order : 1
     */

    private List<ProductImagesBean> productImages;
    /**
     * group : 基本参数
     * entries : [{"name":"操作系统","value":"iOS"},{"name":"电池类型","value":"锂电池"}]
     */

    private List<ParameterValuesBean> parameterValues;
    /**
     * name : 颜色
     * entries : [{"id":0,"value":"黑色","isSelected":false},{"id":1,"value":"白色","isSelected":false},{"id":2,"value":"银色","isSelected":true},{"id":3,"value":"灰色","isSelected":true},{"id":4,"value":"红色","isSelected":false},{"id":5,"value":"金色","isSelected":true}]
     */

    private List<SpecificationItemsBean> specificationItems;
    private List<?> tags;
    /**
     * id : 9
     * sn : 20150101102_1
     * price : 5200
     * cost : 200
     * marketPrice : 6240
     * rewardPoint : 5200
     * exchangePoint : 4522
     * stock : 100
     * allocatedStock : 0
     * isDefault : false
     * specificationValues : [{"id":2,"value":"银色"},{"id":6,"value":"16GB"}]
     */

    private List<ProductsBean> products;


    public int getId() { return id;}


    public void setId(int id) { this.id = id;}


    public String getName() { return name;}


    public void setName(String name) { this.name = name;}


    public String getCaption() { return caption;}


    public void setCaption(String caption) { this.caption = caption;}


    public String getType() { return type;}


    public void setType(String type) { this.type = type;}


    public int getPrice() { return price;}


    public void setPrice(int price) { this.price = price;}


    public int getMarketPrice() { return marketPrice;}


    public void setMarketPrice(int marketPrice) { this.marketPrice = marketPrice;}


    public String getImage() { return image;}


    public void setImage(String image) { this.image = image;}


    public String getUnit() { return unit;}


    public void setUnit(String unit) { this.unit = unit;}


    public double getWeight() { return weight;}


    public void setWeight(double weight) { this.weight = weight;}


    public boolean isIsMarketable() { return isMarketable;}


    public void setIsMarketable(boolean isMarketable) { this.isMarketable = isMarketable;}


    public boolean isIsList() { return isList;}


    public void setIsList(boolean isList) { this.isList = isList;}


    public boolean isIsTop() { return isTop;}


    public void setIsTop(boolean isTop) { this.isTop = isTop;}


    public boolean isIsDelivery() { return isDelivery;}


    public void setIsDelivery(boolean isDelivery) { this.isDelivery = isDelivery;}


    public String getIntroduction() { return introduction;}


    public void setIntroduction(String introduction) { this.introduction = introduction;}


    public String getMemo() { return memo;}


    public void setMemo(String memo) { this.memo = memo;}


    public String getKeyword() { return keyword;}


    public void setKeyword(String keyword) { this.keyword = keyword;}


    public String getSeoTitle() { return seoTitle;}


    public void setSeoTitle(String seoTitle) { this.seoTitle = seoTitle;}


    public String getSeoKeywords() { return seoKeywords;}


    public void setSeoKeywords(String seoKeywords) { this.seoKeywords = seoKeywords;}


    public String getSeoDescription() { return seoDescription;}


    public void setSeoDescription(String seoDescription) { this.seoDescription = seoDescription;}


    public int getScore() { return score;}


    public void setScore(int score) { this.score = score;}


    public int getTotalScore() { return totalScore;}


    public void setTotalScore(int totalScore) { this.totalScore = totalScore;}


    public int getScoreCount() { return scoreCount;}


    public void setScoreCount(int scoreCount) { this.scoreCount = scoreCount;}


    public String getGenerateMethod() { return generateMethod;}


    public void setGenerateMethod(String generateMethod) { this.generateMethod = generateMethod;}


    public String getProductCategory() { return productCategory;}


    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }


    public String getBrand() { return brand;}


    public void setBrand(String brand) { this.brand = brand;}


    public List<ProductImagesBean> getProductImages() { return productImages;}


    public void setProductImages(List<ProductImagesBean> productImages) {
        this.productImages = productImages;
    }


    public List<ParameterValuesBean> getParameterValues() { return parameterValues;}


    public void setParameterValues(List<ParameterValuesBean> parameterValues) {
        this.parameterValues = parameterValues;
    }


    public List<SpecificationItemsBean> getSpecificationItems() { return specificationItems;}


    public void setSpecificationItems(List<SpecificationItemsBean> specificationItems) {
        this.specificationItems = specificationItems;
    }


    public List<?> getTags() { return tags;}


    public void setTags(List<?> tags) { this.tags = tags;}


    public List<ProductsBean> getProducts() { return products;}


    public void setProducts(List<ProductsBean> products) { this.products = products;}


    public static class ProductImagesBean implements Serializable {
        private String title;
        private String source;
        private String large;
        private String medium;
        private String thumbnail;
        private int order;


        public String getTitle() { return title;}


        public void setTitle(String title) { this.title = title;}


        public String getSource() { return source;}


        public void setSource(String source) { this.source = source;}


        public String getLarge() { return large;}


        public void setLarge(String large) { this.large = large;}


        public String getMedium() { return medium;}


        public void setMedium(String medium) { this.medium = medium;}


        public String getThumbnail() { return thumbnail;}


        public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail;}


        public int getOrder() { return order;}


        public void setOrder(int order) { this.order = order;}
    }


    public static class ParameterValuesBean implements Serializable {
        private String group;
        /**
         * name : 操作系统
         * value : iOS
         */

        private List<EntriesBean> entries;


        public String getGroup() { return group;}


        public void setGroup(String group) { this.group = group;}


        public List<EntriesBean> getEntries() { return entries;}


        public void setEntries(List<EntriesBean> entries) { this.entries = entries;}


        public static class EntriesBean implements Serializable {
            private String name;
            private String value;


            public String getName() { return name;}


            public void setName(String name) { this.name = name;}


            public String getValue() { return value;}


            public void setValue(String value) { this.value = value;}
        }
    }


    public static class SpecificationItemsBean implements Serializable {
        private String name;
        /**
         * id : 0
         * value : 黑色
         * isSelected : false
         */

        private List<EntriesBean> entries;


        public String getName() { return name;}


        public void setName(String name) { this.name = name;}


        public List<EntriesBean> getEntries() { return entries;}


        public void setEntries(List<EntriesBean> entries) { this.entries = entries;}


        public static class EntriesBean implements Serializable {
            private int id;
            private String value;
            private boolean isSelected;
            private int stateType;


            public int getStateType() { return stateType;}


            public void setStateType(int stateType) { this.stateType = stateType;}


            public int getId() { return id;}


            public void setId(int id) { this.id = id;}


            public String getValue() { return value;}


            public void setValue(String value) { this.value = value;}


            public boolean isIsSelected() { return isSelected;}


            public void setIsSelected(boolean isSelected) { this.isSelected = isSelected;}
        }
    }


    public static class ProductsBean implements Serializable {
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
        /**
         * id : 2
         * value : 银色
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


        public void setAllocatedStock(int allocatedStock) { this.allocatedStock = allocatedStock;}


        public boolean isIsDefault() { return isDefault;}


        public void setIsDefault(boolean isDefault) { this.isDefault = isDefault;}


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
}
