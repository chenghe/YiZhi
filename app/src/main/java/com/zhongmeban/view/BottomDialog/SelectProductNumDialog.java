package com.zhongmeban.view.BottomDialog;

import android.app.Dialog;
import android.content.Context;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.baseadapterlist.CommonAdapter;
import com.zhongmeban.base.baseadapterlist.ViewHolder;
import com.zhongmeban.bean.shop.FormatBean;
import com.zhongmeban.bean.shop.ProductDetailBean;
import com.zhongmeban.view.MyGridView;
import com.zhongmeban.view.MyListView;
import com.zhongmeban.view.QuantityView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 选择商品个数的底部弹窗
 * Created by User on 2016/10/13.
 */

public class SelectProductNumDialog {

    private Context mContent;
    private TextView mTvPrice;
    private TextView mTvNum;
    private TextView mTvAddCart;
    private QuantityView mNumView;
    private onCliclNumChange mListener;
    private Dialog mDialog;
    private MyListView mListView;
    private ProductDetailBean mProductDetailBean;

    public static final int STATE_SPECIFICATIONS_SELECT = 1;
    public static final int STATE_SPECIFICATIONS_NORMAL = 0;
    public static final int STATE_SPECIFICATIONS_EMPTY = -1;


    public SelectProductNumDialog(Context mContent) {
        this.mContent = mContent;
    }


    public SelectProductNumDialog(Context mContent, ProductDetailBean deatilBean) {
        this.mContent = mContent;
        this.mProductDetailBean = deatilBean;
    }


    public void setChangeListener(onCliclNumChange listener) {
        this.mListener = listener;
    }


    //展示一个可以从底部弹出的dialog，并且充满全屏
    public void showDialog() {
        mDialog = new Dialog(mContent, R.style.Theme_Light_Dialog);
        View dialogView = LayoutInflater.from(mContent)
            .inflate(R.layout.dialog_shop_select, null);
        initView(dialogView);
        //获得dialog的window窗口
        Window window = mDialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.dialogStyle);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        mDialog.setContentView(dialogView);
        mDialog.show();

    }


    private void initView(View dialogView) {
        mTvPrice = (TextView) dialogView.findViewById(R.id.id_dialog_product_price);
        mTvNum = (TextView) dialogView.findViewById(R.id.id_dialog_product_num);
        mTvAddCart = (TextView) dialogView.findViewById(R.id.id_dialog_product_add_cart);
        mNumView = (QuantityView) dialogView.findViewById(R.id.id_dialog_product_numview);
        mListView = (MyListView) dialogView.findViewById(R.id.id_dialog_product_mylistview);

        mNumView.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override public void onQuantityChanged(int newQuantity, boolean programmatically) {
                mTvNum.setText("已选" + newQuantity + "件");
                if (mListener != null) mListener.clickChangeListener(newQuantity);
            }


            @Override public void onLimitReached() {
            }
        });

        mTvAddCart.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                mDialog.dismiss();
                if (mListener != null) mListener.clickAddCart();
            }
        });

        fixData();

        mListView.setAdapter(new CommonAdapter<ProductDetailBean.SpecificationItemsBean>(mContent,
            R.layout.item_dialog_poduct_specifications,
            mProductDetailBean.getSpecificationItems()) {
            @Override
            protected void convert(ViewHolder viewHolder, ProductDetailBean.SpecificationItemsBean item, int positionList) {
                viewHolder.setText(R.id.id_item_dialog_product_specification_title, item.getName());
                MyGridView gridView = viewHolder.getView(
                    R.id.id_item_dialog_product_specification_grid);
                gridView.setAdapter(
                    new CommonAdapter<ProductDetailBean.SpecificationItemsBean.EntriesBean>(
                        mContext, R.layout.item_dialog_poduct_specifications_enty,
                        item.getEntries()) {
                        @Override
                        protected void convert(ViewHolder viewHolder, ProductDetailBean.SpecificationItemsBean.EntriesBean item, int positionGrid) {
                            TextView tv = viewHolder.getView(
                                R.id.id_item_dialog_product_specification_enty_content);
                            tv.setText(item.getValue());

                            changeTVState(item.getStateType(), tv);

                        }
                    });

            }
        });
    }


    List<FormatBean> formatList = new ArrayList<>();

    //所有可能的组合
    Map<Integer, List<FormatBean>> allProductsList = new HashMap<>();


    private void fixData() {

       /* Observable.from(mProductDetailBean.getProducts())
            .filter(new Func1<ProductDetailBean.ProductsBean, Boolean>() {
                @Override public Boolean call(ProductDetailBean.ProductsBean productsBean) {
                    return productsBean.isIsDefault();
                }
            }).flatMap(new Func1<ProductDetailBean.ProductsBean, Observable<ProductDetailBean.ProductsBean.SpecificationValuesBean>>() {
            @Override public Observable<ProductDetailBean.ProductsBean.SpecificationValuesBean> call(ProductDetailBean.ProductsBean productsBean) {
                return Observable.from(productsBean.getSpecificationValues());
            }
        }).subscribe(new Action1<ProductDetailBean.ProductsBean.SpecificationValuesBean>() {
            @Override
            public void call(ProductDetailBean.ProductsBean.SpecificationValuesBean specificationValuesBean) {

            }
        });*/

        for (int i = 0; i < mProductDetailBean.getProducts().size(); i++) {
            ProductDetailBean.ProductsBean product = mProductDetailBean.getProducts().get(i);
            SparseIntArray array = new SparseIntArray();
            List<FormatBean> list = new ArrayList<>();

            for (int j = 0; j < product.getSpecificationValues().size(); j++) {
                if (allProductsList.containsKey(Integer.valueOf(product.getId()))) {
                    allProductsList.get(Integer.valueOf(product.getId()))
                        .add(new FormatBean(j, product.getSpecificationValues().get(j).getId()));
                } else {
                    array.put(j, product.getSpecificationValues().get(j).getId());
                    list.add(new FormatBean(j, product.getSpecificationValues().get(j).getId()));
                }
            }

            allProductsList.put(Integer.valueOf(product.getId()), list);
        }

        Observable.from(mProductDetailBean.getProducts())
            .filter(new Func1<ProductDetailBean.ProductsBean, Boolean>() {
                @Override public Boolean call(ProductDetailBean.ProductsBean productsBean) {

                    return productsBean.isIsDefault();
                }
            }).subscribe(new Action1<ProductDetailBean.ProductsBean>() {
            @Override public void call(ProductDetailBean.ProductsBean productsBean) {

                for (int i = 0; i < productsBean.getSpecificationValues().size(); i++) {

                    formatList.add(
                        new FormatBean(i, productsBean.getSpecificationValues().get(i).getId()));
                    ProductDetailBean.SpecificationItemsBean specificationItemsBean
                        = mProductDetailBean.getSpecificationItems().get(i);
                    for (int j = 0;
                         j < specificationItemsBean.getEntries().size();
                         j++) {

                        if (specificationItemsBean
                            .getEntries()
                            .get(j)
                            .getId() == productsBean.getSpecificationValues().get(i).getId()) {
                            specificationItemsBean.getEntries()
                                .get(j)
                                .setStateType(STATE_SPECIFICATIONS_SELECT);
                        }

                    }

                }
            }
        });

        getGoodsIdBySpecifications(formatList);
        selectList.add(new FormatBean(0, 3));
        //selectList.add(new FormatBean(1, 7));
        changeStateSpecificationBySelect(selectList);
    }


    private List<FormatBean> selectList = new ArrayList<>();
    List<List<FormatBean>> resultList = new ArrayList<>();


    private void changeStateSpecificationBySelect(List<FormatBean> selectList) {

        for (Integer key : allProductsList.keySet()) {

            List<FormatBean> productArray = allProductsList.get(key);
            for (int i = 0; i < productArray.size(); i++) {
            }

        }

        for (int i = 0; i < mProductDetailBean.getProducts().size(); i++) {
            ProductDetailBean.ProductsBean product = mProductDetailBean.getProducts().get(i);
        }
        for (int i = 0; i < selectList.size(); i++) {
            for (Integer key : allProductsList.keySet()) {
                List<FormatBean> productList = allProductsList.get(key);
                for (int j = 0; j < productList.size(); j++) {
                    if (selectList.get(i).equals(productList.get(j))) {
                        Logger.v("selectList.get(i)===" + selectList.get(i) +
                            " ---aproductList.get(j)==" + productList.get(j));
                        resultList.add(Integer.valueOf(i), productList);
                    }
                }
            }

        }

        Logger.v("resultMap= " + resultList.toString());
        sss();

        for (int i = 0; i < mProductDetailBean.getSpecificationItems().size(); i++) {
            ProductDetailBean.SpecificationItemsBean specifiBean
                = mProductDetailBean.getSpecificationItems().get(i);
            for (int j = 0; j < specifiBean.getEntries().size(); j++) {
                ProductDetailBean.SpecificationItemsBean.EntriesBean entryBean
                    = specifiBean.getEntries().get(j);

                    if (map.get(Integer.valueOf(i)).contains(Integer.valueOf(entryBean.getId()))) {
                        entryBean.setStateType(STATE_SPECIFICATIONS_NORMAL);
                    } else {
                        entryBean.setStateType(STATE_SPECIFICATIONS_EMPTY);
                    }

            }
        }


    }


    Map<Integer, List<Integer>> map = new HashMap<>();


    public void sss() {
        for (int i = 0; i < resultList.size(); i++) {
            for (int j = 0; j < resultList.get(i).size(); j++) {
                FormatBean bean = resultList.get(i).get(j);
                if (map.get(Integer.valueOf(j)) != null) {
                    List<Integer> list = map.get(Integer.valueOf(j));
                    list.add(bean.id);
                } else {
                    List<Integer> list = new ArrayList<>();
                    list.add(bean.id);
                    map.put(Integer.valueOf(j), list);
                }
            }
        }

        Logger.e("id集合---"+map.toString());
    }


    private int getGoodsIdBySpecifications(final List<FormatBean> array) {
        int id = 0;
        for (Integer key : allProductsList.keySet()) {
            Logger.v("key= " + key + " and value= " + allProductsList.get(key));
            if (array.equals(allProductsList.get(key))) {
                id = key;
            }
        }
        Logger.e("LIst====" + array.toString() + "id 找到了===" + id);
        return id;
    }


    //根据设定的状态展示规格的状态，比如：正常，选中，不可选（没有这个商品）
    private void changeTVState(int type, TextView tv) {
        switch (type) {
            case STATE_SPECIFICATIONS_NORMAL:
                tv.setTextColor(mContent.getResources().getColor(R.color.text_one));
                tv.setBackgroundResource(R.drawable.select_btn_edge_color_touming_bleak);
                break;
            case STATE_SPECIFICATIONS_EMPTY:
                tv.setTextColor(mContent.getResources().getColor(R.color.gray));
                tv.setBackgroundResource(R.drawable.select_btn_edge_color_touming_broken_line);
                break;
            case STATE_SPECIFICATIONS_SELECT:
                tv.setTextColor(mContent.getResources().getColor(R.color.app_green));
                tv.setBackgroundResource(R.drawable.select_btn_edge_color_touming);
                break;

        }
    }


    public interface onCliclNumChange {
        void clickChangeListener(int num);
        void clickAddCart();
    }
}
