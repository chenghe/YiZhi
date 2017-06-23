package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.R;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.base.baseadapterlist.CommonAdapter;
import com.zhongmeban.base.baseadapterlist.ViewHolder;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.DeleteIndexRecordBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SpannableStringUtils;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.view.MyListView;
import de.greenrobot.dao.attention.MarkerSource;
import de.greenrobot.dao.attention.RecordIndex;
import de.greenrobot.dao.attention.RecordIndexItem;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 标志物详情Activity
 * Created by Chengbin He on 2016/6/30.
 */
public class ActivityMarkerDetail extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_MARKER_INFO = "extra_marker_info";

    private Activity mContext = ActivityMarkerDetail.this;
    private int indexItemId;
    private Subscription dbSubscription;
    private Button btDelete;
    private Button btEdit;
    private String token;
    private String pationId;
    private DeleteIndexRecordBody body;
    private MaterialDialog progressDiaglog;
    private TextView tvMarkerInfo;
    private MyListView listView;
    private MarkerSource markerSource;//显示List


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_markerdetail);

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        token = sp.getString("token", "");
        pationId = sp.getString("patientId", "");

        Intent intent = getIntent();
        indexItemId = intent.getIntExtra("indexItemId", 0);
        markerSource = (MarkerSource) intent.getSerializableExtra(EXTRA_MARKER_INFO);
        body = new DeleteIndexRecordBody();
        body.setRecordId(indexItemId);

        initView();
        initTitle();

        initData();
    }


    private void initView() {
        btDelete = (Button) findViewById(R.id.bt_delete);
        btDelete.setOnClickListener(this);
        btEdit = (Button) findViewById(R.id.bt_edit);
        btEdit.setOnClickListener(this);

        tvMarkerInfo = (TextView) findViewById(R.id.tv_marker_info_detail);
        listView = (MyListView) findViewById(R.id.lv_marker_info);
    }


    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("标志物详情");
        title.backSlid(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }


    private void initData() {

        setTvMarkerInfo();
        listView.setAdapter(
            new CommonAdapter<RecordIndex>(mContext, R.layout.item_item_marker_info,
                markerSource.getIndexList()) {
                @Override
                protected void convert(ViewHolder viewHolder, RecordIndex item, int pos) {
                    TextView tvAbnormal = viewHolder.getView(R.id.tv_marker_id_abnormal);
                    TextView tvName = viewHolder.getView(R.id.tv_marker_name);
                    tvName.setText(
                        item.getIndexName());
                    viewHolder.setText(R.id.tv_marker_id_normal,
                        item.getNormalMin() + "-" + item.getNormalMax());
                    tvAbnormal.setText(item.getValue() + item.getUnitName());
                    tvAbnormal.setTextColor(item.getStatus() == 2
                                            ? ContextCompat.getColor(mContext, R.color.text_red)
                                            : ContextCompat.getColor(mContext,
                                                R.color.content_two));

                }
            });
    }


    private String getType(long purposeType) {
        switch ((int) purposeType) {
            case 1:
                return "确诊";
            case 2:
                return "复查";
            case 3:
                return "疗效评估";
            case 4:
                return "不良反应监测";
        }

        return "";
    }


    /**
     * 删除标志物记录
     */
    private void deleteIndexRecord(DeleteIndexRecordBody body, String token) {
        HttpService.getHttpService().getDeleteIndexRecord(body, token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<CreateOrDeleteBean>() {
                @Override
                public void onCompleted() {
                    Log.i("hcb", "ActivityMarkerDetail deleteIndexRecord onCompleted");
                }


                @Override
                public void onError(Throwable e) {
                    Log.i("hcb", "ActivityMarkerDetail deleteIndexRecord onError");
                    Log.i("hcb", "e" + e);
                }


                @Override
                public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    Log.i("hcb", "ActivityMarkerDetail deleteIndexRecord onNext");
                    if (createOrDeleteBean.getResult()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDiaglog.dismiss();
                                Intent intent = new Intent(mContext, ActivityAttentionList.class);
                                mContext.setResult(300, intent);
                                mContext.finish();
                            }
                        }, 1000);
                    }
                }
            });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbSubscription != null && dbSubscription.isUnsubscribed()) {
            dbSubscription.unsubscribe();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete://删除
                showDeleteDialog(mContext, positiveCallback, "是否删除这条记录？");

                break;

            case R.id.bt_edit://编辑
                Intent intent = new Intent(mContext, ActivityAddNewMarker.class);
                intent.putExtra("ISEDIT", true);
                intent.putExtra("indexItemId", indexItemId);
                startActivityForResult(intent, 1);
                break;
        }
    }


    /**
     * 删除Dialog回调接口
     */
    MaterialDialog.SingleButtonCallback positiveCallback
        = new MaterialDialog.SingleButtonCallback() {

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            progressDiaglog = showProgressDialog("正在删除数据，请稍后", mContext);
            deleteIndexRecord(body, token);
            dialog.dismiss();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 300) {
            Intent intent = new Intent(this, ActivityAttentionList.class);
            setResult(300, intent);
            finish();
        }
    }


    // 设置标记物各种信息
    private void setTvMarkerInfo() {
        RecordIndexItem indexItem = markerSource.getIndexItem();
        int content = ContextCompat.getColor(this, R.color.content_two);
        int textOne = ContextCompat.getColor(this, R.color.text_one);
        SpannableStringBuilder builder = SpannableStringUtils.getBuilder("开始日期：\n")
            .setForegroundColor(content)
            .append(TimeUtils.formatTime(indexItem.getTime()) + "\n")
            .setForegroundColor(textOne)
            .append("检查医院：\n")
            .setForegroundColor(content)
            .append(indexItem.getHospitalName() + "\n")
            .setForegroundColor(textOne)
            .append("检查目的：\n")
            .setForegroundColor(content)
            .append(getType(indexItem.getType()))
            .setForegroundColor(textOne)
            .create();
        tvMarkerInfo.setText(builder);
    }

}