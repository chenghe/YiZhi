package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.MedicineQuestionBean;
import com.zhongmeban.bean.postbody.MedicineQuestionBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 药品提问
 * Created by Chengbin He on 2016/5/26.
 */
public class ActivityMedicieneQuestion extends BaseActivity{

    private EditText etQusetion;
    private MedicineQuestionBody body;
    private String questionContent;
    private Subscription mSubscription;
    private Activity mContext = ActivityMedicieneQuestion.this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_question);

        Intent intent = getIntent();
        String medicineId = intent.getStringExtra("medicineId");

        body = new MedicineQuestionBody();
        body.setMedicineId(medicineId);

        initTitle();
        etQusetion = (EditText) findViewById(R.id.et_question);
        etQusetion.addTextChangedListener(textWatcher);

    }

    @Override
    protected void initTitle() {

        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("提个问题");
        title.backSlid(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.ivTitleBtnback://back 图标
                    finish();
                    break;
                case R.id.right_button: //title右侧Button
                    body.setQuestionContent(questionContent);
                    postHttpQuestion();
                    break;
            }

        }
    };

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            questionContent = s.toString();
            if (questionContent.length()>=5){
                title.slideRighttext("确定", onClickListener);
            }else {
                title.setRightGone();
            }
        }
    };

    public void postHttpQuestion(){
        mSubscription = HttpService.getHttpService().postMedicineQAndA(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MedicineQuestionBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "http onCompleted()");
                        Intent intent = new Intent();
                        mContext.setResult(200,intent);//发送成功
                        mContext.finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "http onError()");
                        Log.i("hcb", "e " + e);
                    }

                    @Override
                    public void onNext(MedicineQuestionBean medicineQuestionBean) {
                        Log.i("hcb", "http onNext()");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
