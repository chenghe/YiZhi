package com.zhongmeban.shopmodle.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.zhongmeban.R;
import com.zhongmeban.shopmodle.activity.ActProductBrowser;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.base.baseadapterlist.CommonAdapter;
import com.zhongmeban.base.baseadapterlist.ViewHolder;
import com.zhongmeban.bean.shop.ProductDetailBean;
import com.zhongmeban.utils.AlignedTextUtils;
import com.zhongmeban.utils.DisplayUtil;
import com.zhongmeban.utils.SpannableUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.BottomDialog.SelectProductNumDialog;
import com.zhongmeban.view.ScrollLinearLayout;
import com.zhongmeban.view.ViewPagerNest;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 商品详情 --->商品简介
 *
 * 邮箱：cchen@ideabinder.com
 */
public class FragProductInfo extends BaseFragment implements View.OnClickListener {

    public static final String KEY = "shoushu";
    private ViewPagerNest mViewPager;
    private TextView mTvIndicator;
    private RelativeLayout mLayoutShopSelect;
    private ScrollLinearLayout mListPeoductInfo;
    private int mCurrentPos;
    private List<ProductInfo> mDatasInfo = new ArrayList<>();
    private List<String> imgUrlNormal = new ArrayList<>();
    private ArrayList<String> imgUrlLarge= new ArrayList<>();
    private ProductDetailBean productDetailBean;

    private TextView mTvName;
    private TextView mTvPrice;
    private TextView mTvPriceMarket;
    private TextView mTvIntrduce;

    public static FragProductInfo newInstance(ProductDetailBean productDetail) {
        FragProductInfo fragment = new FragProductInfo();
        Bundle args = new Bundle();
        args.putSerializable(KEY, productDetail);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productDetailBean = (ProductDetailBean) getArguments().getSerializable(KEY);
        }
        mDatasInfo.add(new ProductInfo("品　　牌", "肿么办"));
        mDatasInfo.add(new ProductInfo("规　　格", "150mg"));
        mDatasInfo.add(new ProductInfo("原　产　国", "日本"));
        mDatasInfo.add(new ProductInfo("保　质　期", "一辈子"));
        mDatasInfo.add(new ProductInfo("功　　效", "没啥效果"));
        mDatasInfo.add(new ProductInfo("服用方法",
            "一天二十四个小时随便吃，反正吃了也没啥用，不信你试试，有用也别怪我,一天二十四个小时随便吃，反正吃了也没啥用，不信你试试，有用也别怪我"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_product_info, container, false);

        initWidget(view);

        initData(productDetailBean);
        return view;
    }


    public void initWidget(View view) {
        mViewPager = (ViewPagerNest) view.findViewById(R.id.id_frag_product_viewpager);
        mViewPager.setOnClickListener(this);
        mTvIndicator = (TextView) view.findViewById(R.id.id_frag_product_tv_indicator);
        mLayoutShopSelect = (RelativeLayout) view.findViewById(
            R.id.id_frag_product_select_shopping);
        mLayoutShopSelect.setOnClickListener(this);
        mListPeoductInfo = (ScrollLinearLayout) view.findViewById(R.id.id_frag_product_list_info);


        mTvName = (TextView) view.findViewById(R.id.id_frag_product_tv_name);
        mTvPrice = (TextView) view.findViewById(R.id.id_frag_product_tv_price_our);
        mTvPriceMarket = (TextView) view.findViewById(R.id.id_frag_product_tv_price_market);
        mTvIntrduce = (TextView) view.findViewById(R.id.id_frag_product_tv_intrduce);
        mTvPriceMarket.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );


        if (!TextUtils.isEmpty(productDetailBean.getCaption())){
            mTvIntrduce.setText(productDetailBean.getCaption());
        }
        mTvName.setText(productDetailBean.getName());
        mTvPrice.setText("￥ "+productDetailBean.getPrice());
        mTvPriceMarket.setText("￥ "+productDetailBean.getMarketPrice());
    }


    /**
     * 初始化数据和UI。数据从activity传过来
     * @param productDetailBean
     */
    private void initData(ProductDetailBean productDetailBean) {
        for (ProductDetailBean.ProductImagesBean bean:productDetailBean.getProductImages()) {
            imgUrlNormal.add(bean.getSource());
            imgUrlLarge.add(bean.getLarge());
        }

        mTvIndicator.setText(
            SpannableUtils.setTextSize(( 1) + "/" + imgUrlNormal.size(), 0, 1,
                DisplayUtil.sp2px(getActivity(), 20)));
        mViewPager.setAdapter(new ImgPagerAdapter(imgUrlNormal));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                mCurrentPos = position;
                mTvIndicator.setText(
                    SpannableUtils.setTextSize((position + 1) + "/" + imgUrlNormal.size(), 0, 1,
                        DisplayUtil.sp2px(getActivity(), 20)));
            }


            @Override public void onPageScrollStateChanged(int state) {

            }
        });

        mListPeoductInfo.setAdapter(
            new CommonAdapter<ProductInfo>(getActivity(), R.layout.item_poduct_infos, mDatasInfo) {
                @Override
                protected void convert(ViewHolder viewHolder, ProductInfo item, int position) {
                        TextView title = viewHolder.getView(R.id.id_item_product_info_title);
                        TextView content = viewHolder.getView(R.id.id_item_product_info_content);
                        title.setText(AlignedTextUtils.formatText(mDatas.get(position).title));
                        content.setText(" "+item.content);
                }
            });

    }



    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_frag_product_select_shopping:
                showSelectNUmDialog(productDetailBean);
                break;
        }
    }


    private void showSelectNUmDialog(ProductDetailBean deatilBean) {
        SelectProductNumDialog dialog = new SelectProductNumDialog(getActivity(),deatilBean);
        dialog.showDialog();
        dialog.setChangeListener(new SelectProductNumDialog.onCliclNumChange() {
            @Override public void clickChangeListener(int num) {
                ToastUtil.showText(getActivity(),num+"个");
            }


            @Override public void clickAddCart() {

            }
        });
    }


    /**
     * 商品多图展示-------------并不是轮播图
     */
    class ImgPagerAdapter extends PagerAdapter {

        private List<String> mDatas;
        public ImgPagerAdapter(List<String> list) {
            this.mDatas = list;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView img = new ImageView(getActivity());

            Picasso.with(getActivity()).load(mDatas.get(position)).into(img);
            img.setTag(position);
            container.addView(img);

            img.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), ActProductBrowser.class);
                    intent.putStringArrayListExtra(ActProductBrowser.EXTRA_IMG_LIST,imgUrlLarge);
                    Logger.v(imgUrlLarge.toString());
                    startActivity(intent);
                }
            });
            return img;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView piv = (ImageView) object;
            container.removeView(piv);
        }


        @Override
        public int getItemPosition(Object object) {
            ImageView img = (ImageView) object;
            Object imgTag = img.getTag();

            return POSITION_NONE;
        }


        @Override
        public int getCount() {
            return mDatas.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


    public static class ProductInfo {
        String title;
        String content;


        public ProductInfo(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
