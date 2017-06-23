package com.zhongmeban.treatmodle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.bean.treatbean.WebViewBean;
import com.zhongmeban.mymodle.adapter.FavoriteInfoListAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.NewsLists;
import com.zhongmeban.bean.postbody.DeleteFavoriteBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.TreatMentShare;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我收藏的内容
 * Created by HeCheng on 2016/11/6.
 */

public class ActivityMyCollectionInfo extends BaseActivity  {

    private RecyclerView recyclerView;
    private FavoriteInfoListAdapter mAdapter;
    private Context mContext = ActivityMyCollectionInfo.this;
    private String token;
    private String userId;
    private SharedPreferences userSp;
    private Button btCancle;
    private LinearLayout llEmptyView;
    private boolean ISEDIT = true;//判断是否为编辑状态


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_common);
        initTitle();
        initData();
        llEmptyView = (LinearLayout) findViewById(R.id.empty_view);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        btCancle = (Button) findViewById(R.id.bt_cancel);
        btCancle.setOnClickListener(onClickListener);
        getFavoriteInfo(userId,token);
    }

    private void initData(){
        userSp = getSharedPreferences("userInfo",MODE_PRIVATE);
        userId = userSp.getString("userId","");
        token = userSp.getString("token","");
    }
    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("收藏的内容");
//        title.slideRighttext("编辑", onClickListener);
        title.backSlid(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.right_button: //title右侧Button
                    if (ISEDIT){
                        ISEDIT =false;
                        title.slideRighttext("删除");
                        mAdapter.setChoose(true);
                        btCancle.setVisibility(View.VISIBLE);
                    }else {
                        if (mAdapter.getChooseList().size()>0){
                            Log.i("hcbtest","mAdapter.getChooseList().size()" +mAdapter.getChooseList().size());
                            ISEDIT =true;
                            title.slideRighttext("编辑");
                            btCancle.setVisibility(View.GONE);
                            DeleteFavoriteBody body = new DeleteFavoriteBody(TreatMentShare.INFOMATION,mAdapter.getChooseList());
                            deleteFavorite(body,token);
                            mAdapter.setChoose(false);
                        }else {
                            ToastUtil.showText(mContext,"请选择删除内容");
                        }

                    }

                    break;

                case R.id.ivTitleBtnback://back 图标
                    finish();
                    break;
                case R.id.bt_cancel:
                    ISEDIT =true;
                    title.slideRighttext("编辑");
                    mAdapter.setChoose(false);
                    btCancle.setVisibility(View.GONE);
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            //内容页返回
            boolean isFavorite  = data.getBooleanExtra("isFavorite",true);
            if (!isFavorite){
                getFavoriteInfo(userId,token);
            }
        }
    }

    /**
     * 获取收藏的内容
     * @param userId
     * @param token
     */
    private void getFavoriteInfo(String userId, String token) {
        HttpService.getHttpService().getFavoriteInformation(1, 100, userId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewsLists>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","getFavoriteInfo onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","getFavoriteInfo onError");
                        Log.i("hcb","e"+e);
                    }

                    @Override
                    public void onNext(NewsLists newsLists) {
                        Log.i("hcb","getFavoriteInfo onNext");
                        if (newsLists.getData().getSourceItems().size()>0){
                            llEmptyView.setVisibility(View.GONE);
                            title.slideRighttext("编辑", onClickListener);

                            final List<NewsLists.NewsItem> list = newsLists.getData().getSourceItems();
                            mAdapter = new FavoriteInfoListAdapter(mContext,list);
                            mAdapter.setItemClickListener(new AdapterClickInterface() {
                                @Override
                                public void onItemClick(View v, int position) {
//                                    Intent intent = new Intent(mContext,ActTipsDetails.class);
//                                    intent.putExtra(ActTipsDetails.EXTRA_NEWS_ID, list.get(position).getId());
//                                    intent.putExtra(ActTipsDetails.EXTRA_FROM_NEWS, true);
//                                    startActivityForResult(intent,1);

                                    Intent intent = new Intent(mContext,TreatNewsDetailActivity.class);
                                    String newsHtml = list.get(position).getUrlAddress();
                                    String title = list.get(position).getTitle();
                                    String newsId = list.get(position).getId();

                                    WebViewBean webViewBean = new WebViewBean(newsHtml,title,newsId,TreatNewsDetailActivity.FROM_INFO);
                                    intent.putExtra(TreatNewsDetailActivity.WEBVIEWBEAN,webViewBean);
                                    startActivity(intent);
                                }

                                @Override
                                public void onItemLongClick(View v, int position) {

                                }
                            });
                        }else {
                            List<NewsLists.NewsItem> list = new ArrayList<NewsLists.NewsItem>();
                            mAdapter = new FavoriteInfoListAdapter(mContext,list);
                            llEmptyView.setVisibility(View.VISIBLE);
                            title.setRightGone();
                        }
                        recyclerView.setAdapter(mAdapter);
                    }
                });
    }

    private void deleteFavorite(DeleteFavoriteBody body , final String token){
        HttpService.getHttpService().postDeleteFavorite(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","deleteFavorite onCompleted");
                        getFavoriteInfo(userId,token);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","deleteFavorite onError");
                        Log.i("hcb","e"+e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","deleteFavoriteonNext");
                    }
                });
    }
}
