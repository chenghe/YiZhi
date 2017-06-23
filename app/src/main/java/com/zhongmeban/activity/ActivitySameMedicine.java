package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.zhongmeban.R;
import com.zhongmeban.adapter.SameMedicineAdapter;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.SameMeidicne;
import com.zhongmeban.net.HttpService;
import java.util.List;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chengbin He on 2016/6/6.
 */
public class ActivitySameMedicine extends BaseActivityToolBar {

    private ListView listView;
    private SameMedicineAdapter mAdapter;
    private Activity mContext = ActivitySameMedicine.this;
    private String medicineId;
    private Subscription mSubscription;
    private List<SameMeidicne.SameMeidicneData> httpList;


    @Override protected int getLayoutId() {
        return R.layout.activity_same_medicine;
    }


    @Override protected void initToolbar() {
        initToolbarCustom("同类药品","");
    }


    @Override protected void initView() {
        Intent intent = getIntent();
        medicineId = intent.getStringExtra("medicineId");
        listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(onItemClickListener);
        initEmptyView();

        getHttpData();
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String medicineId = httpList.get(position).getMedicineId()+"";
            String medicineName = httpList.get(position).getMedicineName();
            String chemicalName = httpList.get(position).getChemicalName();
            Intent intent = new Intent();
            intent.putExtra("medicineId",medicineId);
            intent.putExtra("medicineName",medicineName);
            intent.putExtra("chemicalName",chemicalName);
            mContext.setResult(300,intent);
            mContext.finish();
        }
    };


    /**
     * 联网获取网络数据
     *
     */
    public void getHttpData(){
        mSubscription = HttpService.getHttpService().getSameMedicines(medicineId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SameMeidicne>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","http onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","http onError");
                        Log.i("hcb","e " +e);
                        showEmptyLayoutState(LOADING_STATE_NO_NET);
                    }

                    @Override
                    public void onNext(SameMeidicne sameMeidicne) {
                        Log.i("hcb","http onNext");
                        httpList= sameMeidicne.getData();
                        mAdapter = new SameMedicineAdapter(mContext,httpList);
                        listView.setAdapter(mAdapter);
                        showEmptyLayoutState(LOADING_STATE_SUCESS);
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) mSubscription.unsubscribe();
    }
}
