package com.zhongmeban.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.flexbox.FlexboxLayout;
import com.zhongmeban.R;
import com.zhongmeban.base.baseadapterlist.CommonAdapter;
import com.zhongmeban.base.baseadapterlist.ViewHolder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhy on 16/5/16.
 */
public class TagFlowBoxLayout extends FlexboxLayout {
    private CommonAdapter mAdapter;
    /**
     * 如果为false，则checkable无效
     */

    private Set<Integer> mSelectedItem = new HashSet<Integer>();
    private List<String> mDatas;
    private Context mContext;

    private int normalColor;
    private int selectColor;
    private int normalBg;
    private int selectBg;
    private boolean isSingle = true;


    public TagFlowBoxLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public TagFlowBoxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public TagFlowBoxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setFlexDirection(FlexboxLayout.FLEX_DIRECTION_ROW);
        setFlexWrap(FLEX_WRAP_WRAP);
        //space-around
        setJustifyContent(FlexboxLayout.JUSTIFY_CONTENT_FLEX_START);
        //setJustifyContent(FlexboxLayout.JUSTIFY_CONTENT_SPACE_AROUND);
        setBuilder(new Builder());
    }


    public void setAdapter() {

        mAdapter = new CommonAdapter<String>(this.getContext(), R.layout.item_text_shape_flex,
            mDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, final int position) {
                TextView tv = viewHolder.getView(R.id.tv_item_shape);
                tv.setText(item);
                tv.setTextColor(ContextCompat.getColor(mContext,
                    mSelectedItem.contains(position)
                    ? selectColor
                    : normalColor));
                tv.setBackgroundResource(mSelectedItem.contains(position) ? selectBg : normalBg);
                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {

                        if (isSingle) {
                            mSelectedItem.clear();
                        }

                        if (mSelectedItem.contains(position)) {
                            mSelectedItem.remove(position);
                        } else {
                            mSelectedItem.add(position);

                            mOnTagClickListener.onTagClick(TagFlowBoxLayout.this, v, position);
                        }

                        mAdapter.notifyDataSetChanged();

                        setAdapter();
                    }
                });
            }
        };

        removeAllViews();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View view = mAdapter.getView(i, null, this);
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            //    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //params.topMargin = 66;
            //params.bottomMargin = 88;
            //params.gravity = Gravity.CENTER;
            //view.setLayoutParams(params);
            addView(view);
            //addView(mAdapter.getView(i, null, this));
        }
    }


    public void setSelect(int pos) {
        mSelectedItem.add(pos);
        setAdapter();
    }


    private OnTagClickListener mOnTagClickListener;


    public interface OnTagClickListener {
        void onTagClick(ViewGroup parent, View view, int position);
    }


    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        mOnTagClickListener = onTagClickListener;
    }


    public void setData(List<String> data) {
        this.mDatas = data;
        setAdapter();
    }


    public void setBuilder(Builder builder) {
        this.mDatas = builder.data;
        this.normalColor = builder.normalColor;
        this.selectColor = builder.selectColor;
        this.normalBg = builder.normalBg;
        this.selectBg = builder.selectBg;
        this.isSingle = builder.isSingle;
    }


    public static class Builder {
        private List<String> data;
        private int normalColor = R.color.content_two;
        private int selectColor = R.color.white;
        private int normalBg = R.drawable.bg_box;
        private int selectBg = R.drawable.bg_box_selected;
        private boolean isSingle = true;


        public Builder setData(List<String> data) {
            this.data = data;
            return this;
        }


        public Builder setSingle(boolean flag) {
            this.isSingle = flag;
            return this;
        }


        public Builder setNormalColor(int normalColor) {
            this.normalColor = normalColor;
            return this;
        }


        public Builder setSelectColor(int selectColor) {
            this.selectColor = selectColor;
            return this;
        }


        public Builder setNormalBg(int normalBg) {
            this.normalBg = normalBg;
            return this;
        }


        public Builder setSelectBg(int selectBg) {
            this.selectBg = selectBg;
            return this;
        }

    }

}
