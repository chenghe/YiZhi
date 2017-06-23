package com.zhongmeban.shopmodle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.shop.AddressListBean;
import com.zhongmeban.bean.shop.post.CreatAddressBody;
import com.zhongmeban.bean.shop.post.DeleteAddressBody;
import com.zhongmeban.net.HttpShopMethods;
import com.zhongmeban.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import rx.Subscriber;

/**
 * 地址管理Activity
 * Created by User on 2016/10/9.
 */

public class ActAddressList extends BaseActivityRefresh implements View.OnClickListener {

    private RecyclerView mRvList;
    private Button mBtnAdd;
    private CommonAdapter mAdapter;

    private AddressListBean lastDefalutAddressBean;
    private List<AddressListBean> mDatas = new ArrayList<>();

    private Long memberId = 1l;


    @Override protected int getLayoutIdRefresh() {

        return R.layout.activity_act_address_list;
    }


    @Override protected void initToolbarRefresh() {
        initToolbarCustom("收货地址", "");

        initEmptyView();
        //showEmptyLayoutState(LOADING_STATE_PEOGRESS);
    }


    @Override protected void initViewRefresh() {

        mBtnAdd = (Button) findViewById(R.id.id_act_address_list_add);
        mBtnAdd.setOnClickListener(this);
        mRvList = (RecyclerView) findViewById(R.id.id_act_address_list_recycler);
        mRvList.setLayoutManager(new LinearLayoutManager(this));

        getAddressList(1, memberId, false);

    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_act_address_list_add:
                Intent intent = new Intent(ActAddressList.this, ActAddAddress.class);
                startActivityForResult(intent, 666);
                break;
        }
    }


    @Override public void onRefresh() {
        super.onRefresh();
        getAddressList(1, memberId, true);
    }


    private void getAddressList(int pageNumber, Long memid, final boolean isUpdate) {
        HttpShopMethods.getInstance().getAddressList(pageNumber, memid,
            new Subscriber<HttpResult<List<AddressListBean>>>() {
                @Override public void onCompleted() {
                    mRefreshLayout.post(new Runnable() {
                        @Override public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    });
                    showEmptyLayoutState(LOADING_STATE_SUCESS);
                }


                @Override public void onError(Throwable e) {
                    mRefreshLayout.post(new Runnable() {
                        @Override public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    });

                }


                @Override public void onNext(HttpResult<List<AddressListBean>> listHttpResult) {

                    if (listHttpResult.data != null) {
                        if (isUpdate) {
                            mDatas.clear();
                        }
                        mDatas.addAll(listHttpResult.data);

                        Logger.i("List--" + listHttpResult.data.toString());
                        if (mDatas.size() == 0) {
                            showEmptyLayoutState(LOADING_STATE_SUCESS);
                        }
                        setListData();
                    }
                }
            });
    }


    private void setListData() {

        mRvList.setAdapter(
            mAdapter = new CommonAdapter<AddressListBean>(this, R.layout.item_address_list,
                mDatas) {

                @Override
                protected void convert(ViewHolder holder, final AddressListBean bean, final int position) {
                    ImageView ivAddress = holder.getView(R.id.id_item_address_list_iv_default);
                    TextView tvDefault = holder.getView(R.id.id_item_address_list_tv_default);

                    holder.setText(R.id.id_item_address_list_name, bean.getConsignee());
                    holder.setText(R.id.id_item_address_list_phone, bean.getPhone());
                    holder.setText(R.id.id_item_address_list_detail_address, bean.getAreaName());

                    if (bean.isIsDefault()) {
                        lastDefalutAddressBean = bean;

                        ivAddress.setImageResource(R.drawable.choose_address_select);
                        tvDefault.setTextColor(getResources().getColor(R.color.app_green));
                    } else {
                        tvDefault.setTextColor(getResources().getColor(R.color.content_two));
                        ivAddress.setImageResource(R.drawable.choose_address_normal);
                    }

                    //点击设置为默认地址
                    holder.setOnClickListener(R.id.id_item_address_list_layout_default,
                        new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                if (bean.isIsDefault()) return;

                                updateAddress(bean, lastDefalutAddressBean);
                            }
                        });
                    //删除地址点击事件
                    holder.setOnClickListener(R.id.id_item_address_list_delete,
                        new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                new AlertDialog.Builder(ActAddressList.this).setTitle("是否删除当前地址")
                                    .setPositiveButton(R.string.agree,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                deleteAddress(bean, position);
                                            }
                                        })
                                    .setNegativeButton(R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                    .show();
                            }
                        });

                    //编辑更新地址
                    holder.setOnClickListener(R.id.id_item_address_list_edit,
                        new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                Intent intent = new Intent(ActAddressList.this,
                                    ActAddAddress.class);
                                intent.putExtra(ActAddAddress.EXTRA_IS_UPDATE_ADDRESS, true);
                                intent.putExtra(ActAddAddress.EXTRA_OLD_ADDRESS, bean);
                                startActivityForResult(intent, 666);
                            }
                        });

                }
            });

    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 666 && resultCode == RESULT_OK) {
            getAddressList(1, memberId, true);
            mRefreshLayout.setRefreshing(true);
        }

    }


    private void updateAddress(final AddressListBean bean, final AddressListBean beanLast) {

        bean.setIsDefault(true);
        if (beanLast != null) {
            beanLast.setIsDefault(false);
        }

        CreatAddressBody body = new CreatAddressBody(bean);
        HttpShopMethods.getInstance().updateAddress(body, new Subscriber<HttpResult>() {
            @Override public void onCompleted() {

            }


            @Override public void onError(Throwable e) {
                ToastUtil.showText(ActAddressList.this, "操作失败");

                bean.setIsDefault(false);
                if (beanLast != null) {
                    beanLast.setIsDefault(true);
                }
                mAdapter.notifyDataSetChanged();
            }


            @Override public void onNext(HttpResult httpResult) {
                if (httpResult.errorCode == 0) {
                    bean.setIsDefault(true);

                    if (beanLast != null) {
                        beanLast.setIsDefault(false);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void deleteAddress(final AddressListBean bean, final int pos) {

        DeleteAddressBody body = new DeleteAddressBody(bean.getId());
        HttpShopMethods.getInstance().deleteAddress(body, new Subscriber<HttpResult>() {
            @Override public void onCompleted() {

            }


            @Override public void onError(Throwable e) {
                ToastUtil.showText(ActAddressList.this, "操作失败");
            }


            @Override public void onNext(HttpResult httpResult) {
                if (httpResult.errorCode == 0) {
                    mDatas.remove(pos);
                    mAdapter.notifyItemRemoved(pos);
                }
            }
        });
    }

}
