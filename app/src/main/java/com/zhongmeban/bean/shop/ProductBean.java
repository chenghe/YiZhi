package com.zhongmeban.bean.shop;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2016/10/18.
 */

public class ProductBean implements Serializable {

    /**
     * content : [{"id":47,"name":"苹果 iPhone 5s Case 保护套","caption":"原装品质，精雕细琢","type":"","price":0.2,"marketPrice":288.2,"image":"http://image.demo.shopxx.net/4.0/201501/936b1088-e222-47d1-af6f-a4360a05ffde-thumbnail.jpg","unit":"个","weight":25.2,"isMarketable":false,"isList":true,"isTop":false,"isDelivery":true,"introduction":"","memo":"","keyword":"","seoTitle":"","seoKeywords":"","seoDescription":"","score":20,"totalScore":80,"scoreCount":100,"generateMethod":"","productCategory":"","brand":"","productImages":[{"title":"","source":"http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-source.jpg","large":"http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-large.jpg","medium":"http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-medium.jpg","thumbnail":"http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-thumbnail.jpg","order":1},{"title":"","source":"http://image.demo.shopxx.net/4.0/201501/4e404662-48a6-4d6c-9e6b-8cc29d2b035d-source.jpg","large":"http://image.demo.shopxx.net/4.0/201501/4e404662-48a6-4d6c-9e6b-8cc29d2b035d-large.jpg","medium":"http://image.demo.shopxx.net/4.0/201501/4e404662-48a6-4d6c-9e6b-8cc29d2b035d-medium.jpg","thumbnail":"http://image.demo.shopxx.net/4.0/201501/4e404662-48a6-4d6c-9e6b-8cc29d2b035d-thumbnail.jpg","order":2}],"parameterValues":[],"specificationItems":[],"tags":[],"products":[]},{"id":46,"name":"飞利浦 DLP10800 移动电源","caption":"精致做工，安全高效","type":null,"price":0,"marketPrice":299,"image":"http://image.demo.shopxx.net/4.0/201501/5767900a-8727-4306-b783-9132511b97ea-thumbnail.jpg","unit":"台","weight":null,"isMarketable":null,"isList":null,"isTop":false,"isDelivery":null,"introduction":null,"memo":null,"keyword":null,"seoTitle":null,"seoKeywords":null,"seoDescription":null,"score":null,"totalScore":null,"scoreCount":null,"generateMethod":null,"productCategory":null,"brand":null,"productImages":[],"parameterValues":[],"specificationItems":[],"tags":[],"products":[]},{"id":45,"name":"美的 KFR-32GWDY-IF","caption":"冷暖舒适，简约现代","type":null,"price":2399,"marketPrice":2878.8,"image":"http://image.demo.shopxx.net/4.0/201501/acef11f4-93cf-44e7-b5f8-7b156f2b1809-thumbnail.jpg","unit":"台","weight":null,"isMarketable":null,"isList":null,"isTop":false,"isDelivery":null,"introduction":null,"memo":null,"keyword":null,"seoTitle":null,"seoKeywords":null,"seoDescription":null,"score":null,"totalScore":null,"scoreCount":null,"generateMethod":null,"productCategory":null,"brand":null,"productImages":[],"parameterValues":[],"specificationItems":[],"tags":[],"products":[]}]
     * total : 33
     * pageable : {"pageNumber":1,"pageSize":3,"searchProperty":"","searchValue":"","orderProperty":"","orderDirection":"","filters":[],"orders":[]}
     * filters : []
     * orders : []
     * pageSize : 3
     * pageNumber : 1
     * orderProperty :
     * searchValue :
     * totalPages : 11
     * orderDirection :
     * searchProperty :
     */

    private int total;
    /**
     * pageNumber : 1
     * pageSize : 3
     * searchProperty :
     * searchValue :
     * orderProperty :
     * orderDirection :
     * filters : []
     * orders : []
     */

    private PageableBean pageable;
    private int pageSize;
    private int pageNumber;
    private String orderProperty;
    private String searchValue;
    private int totalPages;
    private String orderDirection;
    private String searchProperty;
    /**
     * id : 47
     * name : 苹果 iPhone 5s Case 保护套
     * caption : 原装品质，精雕细琢
     * type :
     * price : 0.2
     * marketPrice : 288.2
     * image : http://image.demo.shopxx.net/4.0/201501/936b1088-e222-47d1-af6f-a4360a05ffde-thumbnail.jpg
     * unit : 个
     * weight : 25.2
     * isMarketable : false
     * isList : true
     * isTop : false
     * isDelivery : true
     * introduction :
     * memo :
     * keyword :
     * seoTitle :
     * seoKeywords :
     * seoDescription :
     * score : 20
     * totalScore : 80
     * scoreCount : 100
     * generateMethod :
     * productCategory :
     * brand :
     * productImages : [{"title":"","source":"http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-source.jpg","large":"http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-large.jpg","medium":"http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-medium.jpg","thumbnail":"http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-thumbnail.jpg","order":1},{"title":"","source":"http://image.demo.shopxx.net/4.0/201501/4e404662-48a6-4d6c-9e6b-8cc29d2b035d-source.jpg","large":"http://image.demo.shopxx.net/4.0/201501/4e404662-48a6-4d6c-9e6b-8cc29d2b035d-large.jpg","medium":"http://image.demo.shopxx.net/4.0/201501/4e404662-48a6-4d6c-9e6b-8cc29d2b035d-medium.jpg","thumbnail":"http://image.demo.shopxx.net/4.0/201501/4e404662-48a6-4d6c-9e6b-8cc29d2b035d-thumbnail.jpg","order":2}]
     * parameterValues : []
     * specificationItems : []
     * tags : []
     * products : []
     */

    private List<ContentBean> content;
    private List<?> filters;
    private List<?> orders;


    public int getTotal() { return total;}


    public void setTotal(int total) { this.total = total;}


    public PageableBean getPageable() { return pageable;}


    public void setPageable(PageableBean pageable) { this.pageable = pageable;}


    public int getPageSize() { return pageSize;}


    public void setPageSize(int pageSize) { this.pageSize = pageSize;}


    public int getPageNumber() { return pageNumber;}


    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber;}


    public String getOrderProperty() { return orderProperty;}


    public void setOrderProperty(String orderProperty) { this.orderProperty = orderProperty;}


    public String getSearchValue() { return searchValue;}


    public void setSearchValue(String searchValue) { this.searchValue = searchValue;}


    public int getTotalPages() { return totalPages;}


    public void setTotalPages(int totalPages) { this.totalPages = totalPages;}


    public String getOrderDirection() { return orderDirection;}


    public void setOrderDirection(String orderDirection) { this.orderDirection = orderDirection;}


    public String getSearchProperty() { return searchProperty;}


    public void setSearchProperty(String searchProperty) { this.searchProperty = searchProperty;}


    public List<ContentBean> getContent() { return content;}


    public void setContent(List<ContentBean> content) { this.content = content;}


    public List<?> getFilters() { return filters;}


    public void setFilters(List<?> filters) { this.filters = filters;}


    public List<?> getOrders() { return orders;}


    public void setOrders(List<?> orders) { this.orders = orders;}


    public static class PageableBean implements Serializable {
        private int pageNumber;
        private int pageSize;
        private String searchProperty;
        private String searchValue;
        private String orderProperty;
        private String orderDirection;
        private List<?> filters;
        private List<?> orders;


        public int getPageNumber() { return pageNumber;}


        public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber;}


        public int getPageSize() { return pageSize;}


        public void setPageSize(int pageSize) { this.pageSize = pageSize;}


        public String getSearchProperty() { return searchProperty;}


        public void setSearchProperty(String searchProperty) {
            this.searchProperty = searchProperty;
        }


        public String getSearchValue() { return searchValue;}


        public void setSearchValue(String searchValue) { this.searchValue = searchValue;}


        public String getOrderProperty() { return orderProperty;}


        public void setOrderProperty(String orderProperty) { this.orderProperty = orderProperty;}


        public String getOrderDirection() { return orderDirection;}


        public void setOrderDirection(String orderDirection) {
            this.orderDirection = orderDirection;
        }


        public List<?> getFilters() { return filters;}


        public void setFilters(List<?> filters) { this.filters = filters;}


        public List<?> getOrders() { return orders;}


        public void setOrders(List<?> orders) { this.orders = orders;}
    }


    public static class ContentBean implements Serializable{
        private Long id;
        private String name;
        private String caption;
        private String type;
        private double price;
        private double marketPrice;
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
         * title :
         * source : http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-source.jpg
         * large : http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-large.jpg
         * medium : http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-medium.jpg
         * thumbnail : http://image.demo.shopxx.net/4.0/201501/eee0bfc5-d5f6-48c4-aa75-73fb709d824b-thumbnail.jpg
         * order : 1
         */

        private List<ProductImagesBean> productImages;
        private List<?> parameterValues;
        private List<?> specificationItems;
        private List<?> tags;
        private List<?> products;


        public Long getId() { return id;}


        public void setId(Long id) { this.id = id;}


        public String getName() { return name;}


        public void setName(String name) { this.name = name;}


        public String getCaption() { return caption;}


        public void setCaption(String caption) { this.caption = caption;}


        public String getType() { return type;}


        public void setType(String type) { this.type = type;}


        public double getPrice() { return price;}


        public void setPrice(double price) { this.price = price;}


        public double getMarketPrice() { return marketPrice;}


        public void setMarketPrice(double marketPrice) { this.marketPrice = marketPrice;}


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


        public void setSeoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
        }


        public int getScore() { return score;}


        public void setScore(int score) { this.score = score;}


        public int getTotalScore() { return totalScore;}


        public void setTotalScore(int totalScore) { this.totalScore = totalScore;}


        public int getScoreCount() { return scoreCount;}


        public void setScoreCount(int scoreCount) { this.scoreCount = scoreCount;}


        public String getGenerateMethod() { return generateMethod;}


        public void setGenerateMethod(String generateMethod) {
            this.generateMethod = generateMethod;
        }


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


        public List<?> getParameterValues() { return parameterValues;}


        public void setParameterValues(List<?> parameterValues) {
            this.parameterValues = parameterValues;
        }


        public List<?> getSpecificationItems() { return specificationItems;}


        public void setSpecificationItems(List<?> specificationItems) {
            this.specificationItems = specificationItems;
        }


        public List<?> getTags() { return tags;}


        public void setTags(List<?> tags) { this.tags = tags;}


        public List<?> getProducts() { return products;}


        public void setProducts(List<?> products) { this.products = products;}


        public static class ProductImagesBean implements Serializable{
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
    }
}
