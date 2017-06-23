package com.zhongmeban.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityBaseChemotherapy;
import com.zhongmeban.attentionmodle.adapter.AttentionMedicineIndexAdapter;
import com.zhongmeban.adapter.OnIndexClickListener;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SideLetterBar;
import com.zhongmeban.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AttentionMedicine;
import de.greenrobot.dao.Medicine;
import de.greenrobot.dao.MedicineDao;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 化疗部分用药
 * Created by Chengbin He on 2016/8/24.
 */
public class FragmentChemotherapyMedicine extends BaseFragment implements View.OnClickListener {

    private ActivityBaseChemotherapy parentActivity;
//    private List<Medicine> daoList;//数据库list
    private List<AttentionMedicine> dataList = new ArrayList<AttentionMedicine>();//显示用list
    public List<AttentionMedicine> mChooseMedicineList = new ArrayList<AttentionMedicine>();//操作list
    private ListView listView;
    private AttentionMedicineIndexAdapter mAdapter;
    private TextView overlay;
    private EditText searchBox;//搜索editText
    private ImageView clearBtn;//清除搜索内容
    private ViewGroup emptyView;
    private SideLetterBar mLetterBar;//字母索引栏
    private MedicineDao dao;//药品对应数据库表
    private LinearLayout llSreach;
    private int checkPosition = -1;

    private long beginTime;
    private long endTime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityBaseChemotherapy) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attention_medicine_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mChooseMedicineList.clear();
        Log.i("hcbtest","parentActivity.chooseMedicineList" +parentActivity.chooseMedicineList.size());
        mChooseMedicineList.addAll(parentActivity.chooseMedicineList);
        initTitle(view);
        initView(view);

    }


    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.listview);

        getDataFromeDB();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                InputMethodManager imm = (InputMethodManager) parentActivity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(listView.getWindowToken(), 0);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        overlay = (TextView) view.findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) view.findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);


        emptyView = (ViewGroup) view.findViewById(R.id.empty_view);
        clearBtn = (ImageView) view.findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(this);
        searchBox = (EditText) view.findViewById(R.id.et_search);
        searchBox.requestFocus();
        searchBox.setOnClickListener(this);

//        llSreach = (LinearLayout) view.findViewById(R.id.layout_searchbar);
//        llSreach.setOnClickListener(this);
    }

    private void initTitle(View view) {
        LayoutActivityTitle title = new LayoutActivityTitle(view.findViewById(R.id.layout_top));
        title.slideCentertext("请选择用药");
        title.slideRighttext("完成", this);
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                parentActivity.chooseMedicineList.clear();
                parentActivity.onBackPressed();
            }
        });
    }

    /**
     * 数据库请求数据
     */
    public void getDataFromeDB() {
        Observable.create(new Observable.OnSubscribe<Medicine>() {
            @Override
            public void call(Subscriber<? super Medicine> subscriber) {
                Log.i("hcb", " db call");
                //获取治疗部分药箱子数据库类容
                dao = ((MyApplication) parentActivity.getApplication()).getDaoSession().getMedicineDao();
                dataList.clear();
                List<Medicine> daoList = dao.queryBuilder()
                        .where(MedicineDao.Properties.IsActive.eq(true),
                                MedicineDao.Properties.Type.eq(16) )
                        .orderAsc(MedicineDao.Properties.PinyinName)
                        .list();

                //转换数据类型
                for (Medicine medicine : daoList) {
                    AttentionMedicine attentionMedicine = null;
                    if (containerBean(medicine)){
                         attentionMedicine = new AttentionMedicine(medicine, 1, 0, "");
                    }else {
                         attentionMedicine = new AttentionMedicine(medicine, 2, 0, "");
                    }
                    dataList.add(attentionMedicine);
                }

//                ((MyApplication)parentActivity.getApplication()).closeDB();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Medicine>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("db onCompleted()");
                        Log.i("hcb", " db onCompleted()");
//                        mAdapter.updateData(dataList);
//                        mAdapter.notifyDataSetChanged();
                        listView.setAdapter(mAdapter= new AttentionMedicineIndexAdapter(parentActivity, dataList));
                        mAdapter.setIndexClickListener(onIndexClickListener);

                        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
                            @Override
                            public void onLetterChanged(String letter) {
                                int position = mAdapter.getLetterPosition(letter);
                                Log.i("hcbtest","position"+position);
                                listView.setSelection(position);
                            }
                        });

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", " db onCompleted()");
                    }

                    @Override
                    public void onNext(Medicine medicine) {
                        Log.i("hcb", " db onCompleted()");
                    }
                });
    }

    public boolean containerBean(Medicine medicine){
        for (AttentionMedicine bean: mChooseMedicineList) {
            if (bean.getMedicine().getMedicineId()==medicine.getMedicineId()){
                Logger.e("db true"+bean.getMedicine().toString());
                return true;
            }
        }
        return false;
    }
    /**
     * 搜索框输入监听
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String keyword = s.toString();
            if (TextUtils.isEmpty(keyword)) {
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                mLetterBar.setVisibility(View.VISIBLE);
                getDataFromeDB();
            } else {
                clearBtn.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);
                mLetterBar.setVisibility(View.GONE);
                dao = ((MyApplication) parentActivity.getApplication()).getDaoSession().getMedicineDao();
                List<Medicine> daoList = dao.queryBuilder()
                        .where(MedicineDao.Properties.IsActive.eq(true),MedicineDao.Properties.Type.eq(16),
                                dao.queryBuilder().or(MedicineDao.Properties.MedicineName.like("%" + keyword + "%"),
                                        MedicineDao.Properties.PinyinName.like("%" + keyword + "%")))
                        .orderAsc(MedicineDao.Properties.PinyinName)
                        .list();
                //转换数据类型
                dataList.clear();
                for (Medicine medicine : daoList) {
                    AttentionMedicine attentionMedicine = null;
                    if (containerBean(medicine)){
                        attentionMedicine = new AttentionMedicine(medicine, 1, 0, "");
                    }else {
                        attentionMedicine = new AttentionMedicine(medicine, 2, 0, "");
                    }
                    dataList.add(attentionMedicine);
                }
                if (daoList == null || daoList.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(mAdapter= new AttentionMedicineIndexAdapter(parentActivity, dataList));
                    mAdapter.setIndexClickListener(onIndexClickListener);
                    mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
                        @Override
                        public void onLetterChanged(String letter) {
                            int position = mAdapter.getLetterPosition(letter);
                            listView.setSelection(position);
                        }
                    });
                }
            }
        }
    };

    /**
     * ListView 点击事件回调监听
     */
    OnIndexClickListener onIndexClickListener = new OnIndexClickListener() {

        @Override
        public void onNameClick(final int position) {
            checkPosition = position;
            if (dataList.get(position).getCheckMedicine() == 1) {//判断是否选中
                                dataList.get(position).setCheckMedicine(2);
                                long medicineId  = dataList.get(position).getMedicine().getMedicineId();
                                for(int i =0 ; i<mChooseMedicineList.size();i ++){
                                    if(mChooseMedicineList.get(i).getMedicine().getMedicineId()
                                            == medicineId){
                                        mChooseMedicineList.remove(i);
                                    }
                                }
//                                mAdapter.updateData(dataList);
                mAdapter.notifyDataSetChanged();

            } else {
                    if (mChooseMedicineList.size()>9){
                        ToastUtil.showText(parentActivity,"最大用药为10个");
                    }else {
                        dataList.get(position).setCheckMedicine(1);
                        mAdapter.notifyDataSetChanged();
                        mChooseMedicineList.add(dataList.get(position));
                    }



            }
        }

        @Override
        public void onLocateClick() {

        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.et_search:
                searchBox.addTextChangedListener(textWatcher);
                break;

//            case R.id.layout_searchbar:
//                searchBox.addTextChangedListener(textWatcher);
//                break;

            case R.id.right_button://右上角保存
                parentActivity.chooseMedicineList.clear();
                parentActivity.chooseMedicineList.addAll(mChooseMedicineList);
                Log.i("hcbtest","onClick parentActivity.chooseMedicineList.size()"+parentActivity.chooseMedicineList.size());
                parentActivity.backChemotherapyFragment();
                break;
            case R.id.iv_search_clear://搜索框清空按钮
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                break;
        }
    }

    public void clearSearch(){
        searchBox.setText("");
    }
}