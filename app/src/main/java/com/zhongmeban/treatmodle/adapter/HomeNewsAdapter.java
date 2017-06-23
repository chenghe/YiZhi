package com.zhongmeban.treatmodle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.bean.NewsLists;
import com.zhongmeban.utils.genericity.ImageUrl;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Chengbin He on 2016/5/25.
 */
public class HomeNewsAdapter extends BaseAdapter {
    private Context mContext;
    private List<NewsLists.NewsItem> list;
    private String imageUrl = ImageUrl.BaseImageUrl;
    private AdapterClickInterface clickInterface;


    public HomeNewsAdapter(Context mContext, List<NewsLists.NewsItem> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news_list, parent, false);
        ImageView iv = (ImageView) convertView.findViewById(R.id.image_news);
        TextView title = (TextView) convertView.findViewById(R.id.tv_news_title);
        TextView sub = (TextView) convertView.findViewById(R.id.tv_news_sub);

        title.setText(list.get(position).getTitle());
        sub.setText(list.get(position).getAbstractContent());

        if (list.get(position).getPicture() != null) {
            String url = list.get(position).getPicture();
            Log.i("hcb", "url is " + imageUrl + url);
            Picasso.with(mContext)
                .load(imageUrl + url)
                .transform(new RoundedCornersTransformation(15, 0,
                    RoundedCornersTransformation.CornerType.ALL))
                .placeholder(R.drawable.home_news)
                .error(R.drawable.home_news)

                .into(iv);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.onItemClick(v, position);
            }
        });
        return convertView;
    }


    public void setAdapterClickListenter(AdapterClickInterface clickListenter) {
        clickInterface = clickListenter;
    }
}
