package com.zhongmeban.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.bean.shop.ProductBean;
import com.zhongmeban.utils.ImageLoaderZMB;
import it.sephiroth.android.library.widget.HListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/9/26.
 * 商城首页横向的展示列表
 */

public class ShopCarLandList extends FrameLayout {

    private FrameLayout mRootView;
    private HListView mHList;
    private Context mContext;


    private List<ProductBean.ContentBean> mDatasHot = new ArrayList<>();

    public ShopCarLandList(Context context) {
        this(context, null);
    }


    public ShopCarLandList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ShopCarLandList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext= context;

        mRootView = (FrameLayout) LayoutInflater.from(context)
            .inflate(R.layout.view_shop_home_land, this);

        mHList = (HListView) mRootView.findViewById(R.id.id_view_shop_home_land_list);

    }

    public void setmDatasHot(List<ProductBean.ContentBean> mDatasHot) {
        this.mDatasHot = mDatasHot;

        mHList.setAdapter(new com.zhongmeban.base.baseadapterlist.CommonAdapter<ProductBean.ContentBean>(mContext, R.layout.item_shop_land, mDatasHot) {
            @Override
            protected void convert(com.zhongmeban.base.baseadapterlist.ViewHolder viewHolder, ProductBean.ContentBean bean, int position) {
                TextView tvName = viewHolder.getView(R.id.id_item_shop_home_tv_land);
                TextView tvPrice = viewHolder.getView(R.id.id_item_shop_home_tv__price);
                TextView tvPriceOld = viewHolder.getView(R.id.id_item_shop_home_tv_price_old);
                tvPriceOld.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                tvName.setText(bean.getName());
                tvPrice.setText("￥ "+bean.getPrice());
                tvPriceOld.setText("￥ "+bean.getMarketPrice());
                ImageView img = viewHolder.getView(R.id.id_item_shop_home_iv_land);

                //Picasso.with(mContext).load(bean.getImage()).into(img);
                ImageLoaderZMB.getInstance().loadImage(mContext,bean.getImage(),img);
            }
        });
    }


    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }


    @Override public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
