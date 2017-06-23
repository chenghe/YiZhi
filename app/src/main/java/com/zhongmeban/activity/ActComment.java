package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.config.APIconfig;

/**
 * Description:医生评价页面
 * user: Chong Chen
 * time：2016/1/20 16:07
 * 邮箱：cchen@ideabinder.com
 */
public class ActComment extends Activity implements View.OnClickListener {

    private ImageButton btnLeft;
    private TextView tvTitle, tvRight;
    private EditText mEditText;
    private String comment;
    private String doctID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_comment);
        initWidget();
    }

    private void initWidget() {
        btnLeft = (ImageButton) findViewById(R.id.btn_left);
        btnLeft.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvTitle.setText("评价一下");
        tvRight.setText("提交");
        tvRight.setOnClickListener(this);
        tvRight.setTextColor(getResources().getColor(R.color.main_color));
        mEditText = (EditText) findViewById(R.id.et_comment);
        checkIntent();
    }

    private void checkIntent() {
        Intent intent = getIntent();
        doctID = intent.getStringExtra("doctId");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left://返回键
                this.finish();
                this.overridePendingTransition(R.anim.act_entry_from_left, R.anim.act_out_from_right);
                break;
            case R.id.tv_right://提交
                comment = mEditText.getText().toString().trim();
                postCommentData(this, APIconfig.URL_GET_DOCTOR_COMMENT, doctID, comment);
                break;
        }
    }

    private void postCommentData(Context ctx, String url, String doctId, String comment) {
    //    JSONObject params = new JSONObject();
    //    try {
    //        params.put("doctorId", doctId);
    //        params.put("content", comment);
    //        params.put("writer", "android");
    //    } catch (JSONException e) {
    //        e.printStackTrace();
    //    }
    //    JsonObjectRequest request = new JsonObjectRequest(url, params, new Listener<JSONObject>() {
    //        @Override
    //        public void onResponse(JSONObject response) {
    //            if (null != response) {
    //                try {
    //                    int code = response.getInt("errorCode");
    //                    if (0 == code) {
    //                        finish();
    //                    }
    //                } catch (JSONException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        }
    //    }, new ErrorListener() {
    //        @Override
    //        public void onErrorResponse(VolleyError error) {
    //            if (null != error && null != error.getMessage()) {
    //                Toast.makeText(getApplication(), "提交失败", Toast.LENGTH_SHORT).show();
    //            }
    //        }
    //    }) {
    //        @Override
    //        public Map<String, String> getHeaders() throws AuthFailureError {
    //            HashMap<String, String> headers = new HashMap<>();
    //            headers.put("Content-Type", "application/json");
    //            return headers;
    //        }
    //    };
    //    HttpClientRequest.getInstance(ctx).addRequest(request, url);
     }
}
