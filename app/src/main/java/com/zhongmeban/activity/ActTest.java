package com.zhongmeban.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.base.baseadapter.wrapper.LoadMoreWrapper;
import com.zhongmeban.base.weight.LoadingFooter;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.MedicineListBean;
import com.zhongmeban.bean.treatbean.MedicineTypeBean;
import com.zhongmeban.net.HttpResultFunc;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.Constants;
import com.zhongmeban.utils.SpannableStringUtils;
import com.zhongmeban.utils.ZMBUtil;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * Created by User on 2016/11/9.
 */

public class ActTest extends AppCompatActivity {

    RecyclerView rvList;

    private int mIndex = 1;

    private List<MedicineListBean> mDatas = new ArrayList<>();
    private CommonAdapter adapter;
    LoadMoreWrapper mLoadMoreWrapper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_container);

        //  getResources().getColor(R.color.app_green) 过时的替代方法
        int cor = ContextCompat.getColor(this,R.color.content_two);

        rvList = (RecyclerView) findViewById(R.id.test_list);
        initAdapter();
        rvList.setLayoutManager(new LinearLayoutManager(this));
        mLoadMoreWrapper = new LoadMoreWrapper(adapter, this);
        rvList.setAdapter(mLoadMoreWrapper);

        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                //这种模式上来就会调用loadmore，只要不调用停止 会一直加载直到当前item条数超过屏幕
                Logger.d("開始加載更多");
                getData();
            }
        });
        //揽件方式：上门取件\n快递公司：顺丰快递\n预约时间：9月6日 立即取件\n快递费用：等待称重确定价格

        SpannableStringBuilder stringBuilder = SpannableStringUtils.getBuilder("揽件方式：")
            .setForegroundColor(Color.RED)
            .append("上门取件\n")
            .setForegroundColor(Color.BLACK)
            .append("快递公司： ")
            .setForegroundColor(Color.RED)
            .append("顺丰快递\n")
            .setForegroundColor(Color.BLACK)
            .append("预约时间：")
            .setForegroundColor(Color.RED)
            .append("9月6日\n")
            .setForegroundColor(Color.BLACK)

            .append("快递费用： \n")
            .setForegroundColor(Color.RED)
            .append("等待称重确定价格等待称重确定价格等待称重确定价格等待称重确定价格等待称重确定价格等待称重确定价格等待称重确定价格")
            .setForegroundColor(Color.BLACK)
            .setLeadingMargin(140, 140)
            .create();

        TextView tvTest = (TextView) findViewById(R.id.tv_test);
        tvTest.setText(stringBuilder);
    }


    private void getData() {

        HttpService.getHttpService().getMedicinePage("", 0, mIndex, Constants.PAGER_COUNT)
            .compose(RxUtil.<HttpResult<List<MedicineListBean>>>normalSchedulers())
            .subscribe(new Subscriber<HttpResult<List<MedicineListBean>>>() {
                @Override public void onCompleted() {

                }


                @Override public void onError(Throwable e) {
                    mLoadMoreWrapper.setState(LoadingFooter.State.NetWorkError);
                }


                @Override public void onNext(HttpResult<List<MedicineListBean>> httpResult) {
                    mLoadMoreWrapper.setState(LoadingFooter.State.Normal);
                    mIndex++;
                    //if (httpResult.getErrorCode() == 0 && mDatas.size() > 52) {
                    if (httpResult != null && httpResult.getErrorCode() == 0 &&
                        httpResult.getData().size() < Constants.PAGER_COUNT) {

                        mLoadMoreWrapper.setState(LoadingFooter.State.TheEnd);
                    }

                    mDatas.addAll(httpResult.getData());
                    mLoadMoreWrapper.notifyDataSetChanged();
                }
            });
    }


    private void initAdapter() {
        adapter =
            new CommonAdapter<MedicineListBean>(this, R.layout.item_treat_medicine_info, mDatas) {

                @Override
                protected void convert(ViewHolder holder, final MedicineListBean medicineBean, final int position) {

                    TextView tvLeft = holder.getView(R.id.tv_medicalInsurance);
                    TextView tvRight = holder.getView(R.id.tv_is_medical_special);

                    holder.setText(R.id.tv_name, medicineBean.getShowName());
                    holder.setText(R.id.tv_desc, medicineBean.getChemicalName());

                    holder.setText(R.id.tv_cost, ZMBUtil.getFormatPrice(medicineBean.getPrice()));

                    boolean isInsurance = Constants.isMedicalInsurance(medicineBean.getInsurance());
                    boolean isSpecial = Constants.isMedicalInsurance(medicineBean.getSpecial());
                    if (isInsurance && isSpecial) {
                        tvLeft.setText("保");
                        tvRight.setText("特");
                        tvLeft.setVisibility(View.VISIBLE);
                        tvRight.setVisibility(View.VISIBLE);
                    } else if (isInsurance) {
                        tvRight.setText("保");
                        tvLeft.setVisibility(View.GONE);
                        tvRight.setVisibility(View.VISIBLE);
                    } else if (isSpecial) {
                        tvRight.setText("特");
                        tvLeft.setVisibility(View.GONE);
                        tvRight.setVisibility(View.VISIBLE);
                    } else {
                        tvLeft.setVisibility(View.GONE);
                        tvRight.setVisibility(View.GONE);
                    }

                }
            };

        getDataType();

    }


    private void getDataType() {
        HttpService.getHttpService().getMedicineTypeListTest()
            .map(new HttpResultFunc<List<MedicineTypeBean>>())
            .compose(RxUtil.<List<MedicineTypeBean>>normalSchedulers())
            .subscribe(new Subscriber<List<MedicineTypeBean>>() {
                @Override public void onCompleted() {

                }


                @Override public void onError(Throwable e) {
                    Logger.e("异常信息---" + e.toString());
                }


                @Override public void onNext(List<MedicineTypeBean> medicineTypeBeen) {
                    Logger.v("onNext---" + medicineTypeBeen.toString());
                }
            });
    }

}
