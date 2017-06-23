package com.zhongmeban.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.afollestad.materialdialogs.MaterialDialog;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActHome;
import com.zhongmeban.activity.ActivityAddAttention;
import com.zhongmeban.adapter.FragmentAddAttentionAdapter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.CreatePatientBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.ImageUrl;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 新增患者Fragment第二步
 * Created by Chengbin He on 2016/6/20.
 */
public class FragmentAddAttentionStep2 extends BaseFragment {

    private RecyclerView recyclerView;
    private ActivityAddAttention parentActivity;
    private FragmentAddAttentionAdapter mAdapter;
    private Button button;
    private MaterialDialog progressDiaglog;

    public static FragmentAddAttentionStep2 newInstance() {

        Bundle args = new Bundle();

        FragmentAddAttentionStep2 fragment = new FragmentAddAttentionStep2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAddAttention) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_attention_step2, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(parentActivity, 2));
        if (parentActivity.gender == 1) {
            mAdapter = new FragmentAddAttentionAdapter(parentActivity, true);
        } else if (parentActivity.gender == 2) {
            mAdapter = new FragmentAddAttentionAdapter(parentActivity, false);
        }

        mAdapter.setRelation(parentActivity.relation);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new AdapterClickInterface() {
            @Override
            public void onItemClick(View v, int position) {


//                if (parentActivity.relation > 0) {
                button.setEnabled(true);
                button.setTextColor(getResources().getColor(R.color.white));
                parentActivity.relation = mAdapter.getRelation(position);
                Log.i("hcbtest", " parentActivity.relation" + parentActivity.relation);
//                } else {
//                    button.setEnabled(false);
//                    button.setTextColor(getResources().getColor(R.color.content_two));
//                }
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });

        button = (Button) view.findViewById(R.id.bt_save);
        if (parentActivity.relation > 0) {
            button.setEnabled(true);
            button.setTextColor(getResources().getColor(R.color.white));
        } else {
            button.setEnabled(false);
            button.setTextColor(getResources().getColor(R.color.content_two));
        }
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parentActivity.initPatientBody();
                        progressDiaglog = showProgressDialog("正在上传数据，请稍后", parentActivity);
                        String url = parentActivity.uploadPicUrl(parentActivity.photo);
                        if (!TextUtils.isEmpty(url)) {
                            //判断是否有头像要上传
                            uploadIcon(url);
                        } else {
                            if (parentActivity.ISADD) {
                                createPatient(parentActivity.createPatientBody, parentActivity.token);
                            } else {
                                updatePatient(parentActivity.createPatientBody, parentActivity.token);
                            }

                        }
                    }
                });

    }


    /**
     * 创建患者信息
     *
     * @param body
     * @param token
     */

    private void createPatient(CreatePatientBody body, String token) {
        HttpService.getHttpService().postCreatePatient(body, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "createPatient onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "createPatient onError");
                        Log.i("hcb", "e is" + e);
                        ToastUtil.showText(parentActivity,"创建失败");
                        progressDiaglog.dismiss();
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb", " createPatient onNext");
//                        String url = parentActivity.uploadPicUrl(parentActivity.photo);
//                        if (!TextUtils.isEmpty(url)) {
//                            //判断是否有头像要上传
//                            uploadIcon(url);
//                        } else {
                        if (createOrDeleteBean.getResult()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDiaglog.dismiss();
                                    Intent intent = new Intent();
                                    parentActivity.setResult(Activity.RESULT_OK, intent);
                                    parentActivity.finish();
                                }
                            }, 1000);
                        }
                    }

//                    }
                });
    }

    /**
     * 修改患者信息
     *
     * @param body
     * @param token
     */
    private void updatePatient(CreatePatientBody body, String token) {
        HttpService.getHttpService().postUpdatePatient(body, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "updatePatient onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "updatePatient onError");
                        Log.i("hcb", "e is" + e);
                        ToastUtil.showText(parentActivity,"更新失败");
                        progressDiaglog.dismiss();

                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb", " updatePatient onNext");
//                        String url = parentActivity.uploadPicUrl(parentActivity.photo);
//                        if (!TextUtils.isEmpty(url)) {
//                            //判断是否有头像要上传
//                            uploadIcon(url);
//                        } else {
                        if (createOrDeleteBean.getResult()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDiaglog.dismiss();
                                    Intent intent = new Intent();
                                    parentActivity.setResult(Activity.RESULT_OK, intent);
                                    parentActivity.finish();
                                }
                            }, 1000);
                        }
                    }

//                    }
                });

    }


    /**
     * 上传头像
     */
    private void uploadIcon(String iconUrl) {
        File file = new File(iconUrl);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("11", "11.png", fileBody);
        HttpService.getHttpService().postUploadPatientIcon(ImageUrl.PatientKey, part)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", " uploadIcon onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", " uploadIcon onError");
                        Log.i("hcb", " e" + e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb", " uploadIcon onNext");
                        if (createOrDeleteBean.getResult()) {
                            parentActivity.createPatientBody.setAvatar(createOrDeleteBean.getData());
                            if (parentActivity.ISADD) {
                                createPatient(parentActivity.createPatientBody, parentActivity.token);
                            } else {
                                updatePatient(parentActivity.createPatientBody, parentActivity.token);
                            }
                        }
                    }
                });
    }

}
