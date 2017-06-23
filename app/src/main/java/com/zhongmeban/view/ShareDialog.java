package com.zhongmeban.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityCardLogin;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.TreatMentShareBean;
import com.zhongmeban.bean.postbody.DeleteFavoriteBody;
import com.zhongmeban.bean.treatbean.UShareBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.BottomDialog.BottomDialog;

import org.w3c.dom.Text;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Chengbin He on 2016/10/20.
 */

public class ShareDialog {

//    public static final String UN_LOGIN_TOKEN = "0";

    private  BottomDialog dialog;
    private  TextView cancel;//取消
    private  LinearLayout llWeChatFriend;//微信分享
    private  LinearLayout llWeChatCircle;//微信朋友圈
    private  LinearLayout llWeChatFavorite;//微信收藏
    private  LinearLayout llWeiBo;//微博
    private  LinearLayout llQzone;//QQ空间
    private  LinearLayout llQQFriend;//QQ好友
    private  LinearLayout llFavorite;//收藏
    private  LinearLayout llCopyUrl;//复制链接
    private ImageView ivCollection;//收藏ICON
    private  Activity mContext;
    private  TreatMentShareBean bean;
    private UShareBean uShareBean;
    private  String mToken;
    private UMImage image;
    private boolean ISFAVORITE = false;//判断是否已经被收藏
    private boolean EXFAVORITE = false;//该功能是否能收藏

    /**
     * 未登录分享 不能收藏
     * @param context
     * @param uShareBean
     */
    public ShareDialog(Activity context, UShareBean uShareBean) {
        mContext = context;
        EXFAVORITE = false;
        this.uShareBean = uShareBean;
        TreatMentShareBean treatMentShareBean = new TreatMentShareBean();
        image = new UMImage(mContext.getApplicationContext(),R.drawable.icon_share_white);
        showShareDialog(context,treatMentShareBean,"");
    }



    /**
     * 登录分享 能收藏
     * @param context
     * @param treatMentShareBean
     * @param uShareBean
     * @param token
     */
    public ShareDialog(Activity context, TreatMentShareBean treatMentShareBean,
                       UShareBean uShareBean, String token,boolean ISFAVORITE,boolean EXFAVORITE) {
        this.uShareBean = uShareBean;
        this.ISFAVORITE = ISFAVORITE;
        this.EXFAVORITE = EXFAVORITE;
        mContext = context;
        image = new UMImage(mContext.getApplicationContext(),R.drawable.icon_share_white);
        showShareDialog(context,treatMentShareBean,token);
    }

    public void setLoginData(TreatMentShareBean treatMentShareBean,String token){
        bean = treatMentShareBean;
        mToken = token;
    }
    public void showShareDialog(Activity context, TreatMentShareBean treatMentShareBean, String token) {

        mContext = context;
        bean = treatMentShareBean;
        mToken = token;
        //创建分享Dialog
        BottomDialog.Builder builder = new BottomDialog.Builder(mContext);
        dialog = new BottomDialog(mContext, builder);
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_share_dialog, null);
        cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
        cancel.setOnClickListener(onClickListener);
        //朋友圈
        llWeChatCircle = (LinearLayout) view.findViewById(R.id.ll_weichat_circle);
        llWeChatCircle.setOnClickListener(onClickListener);
        //收藏
        llFavorite = (LinearLayout) view.findViewById(R.id.ll_collected);
        llFavorite.setOnClickListener(onClickListener);
        ivCollection = (ImageView) view.findViewById(R.id.iv_collection);
        initISFavorite(ISFAVORITE);
        if (EXFAVORITE){
            llFavorite.setVisibility(View.VISIBLE);
        }else {
            llFavorite.setVisibility(View.INVISIBLE);
        }
        //微信好友
        llWeChatFriend= (LinearLayout) view.findViewById(R.id.ll_weichat_friend);
        llWeChatFriend.setOnClickListener(onClickListener);
        //微信收藏
        llWeChatFavorite = (LinearLayout) view.findViewById(R.id.ll_weichat_favorite);
        llWeChatFavorite.setOnClickListener(onClickListener);
        //微博
        llWeiBo = (LinearLayout) view.findViewById(R.id.ll_weibo);
        llWeiBo.setOnClickListener(onClickListener);
        //QQ空间
        llQzone = (LinearLayout) view.findViewById(R.id.ll_qzone);
        llQzone.setOnClickListener(onClickListener);
        //QQ好友
        llQQFriend = (LinearLayout) view.findViewById(R.id.ll_qqfriend);
        llQQFriend.setOnClickListener(onClickListener);
        //复制链接
        llCopyUrl = (LinearLayout) view.findViewById(R.id.ll_copyurl);
        llCopyUrl.setOnClickListener(onClickListener);

        builder.setView(view);
        dialog = builder.showDialog();
    }

    private void initISFavorite(boolean ISFAVORITE){
        this.ISFAVORITE = ISFAVORITE;
        if (ISFAVORITE){
            ivCollection.setImageResource(R.drawable.icon_share_collection);
        }else {
            ivCollection.setImageResource(R.drawable.icon_share_uncollection);
        }
    }

     View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {


                case R.id.tv_dialog_cancel:
                    dialog.dismiss();
                    break;

                case R.id.ll_collected:
                    if (EXFAVORITE&&TextUtils.isEmpty(mToken)){
                        Intent intent = new Intent(mContext, ActivityCardLogin.class);
                        mContext.startActivityForResult(intent,1);
                        dialog.dismiss();
                    }else {
                        createUserFavorite(bean,mToken);
                    }
                    break;

                case R.id.ll_weibo:
                    //微博分享
                    new ShareAction(mContext).setPlatform(SHARE_MEDIA.SINA)
                            .withText(uShareBean.getText())
                            .withTitle(uShareBean.getTitle())
                            .withTargetUrl(uShareBean.getTargetUrl())
                            .withMedia(image)
                            .setCallback(umShareListener)
                            .share();
                    break;


                case R.id.ll_weichat_friend:
                    //微信朋友 分享
                    new ShareAction(mContext).setPlatform(SHARE_MEDIA.WEIXIN)
                            .withText(uShareBean.getText())
                            .withTitle(uShareBean.getTitle())
                            .withTargetUrl(uShareBean.getTargetUrl())
                            .withMedia(image)
                            .setCallback(umShareListener)
                            .share();
                    break;
                case R.id.ll_weichat_circle:
                    //微信朋友圈
                    new ShareAction(mContext).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                            .withText(uShareBean.getText())
                            .withTitle(uShareBean.getTitle())
                            .withTargetUrl(uShareBean.getTargetUrl())
                            .withMedia(image)
                            .setCallback(umShareListener)
                            .share();
                    break;

                case R.id.ll_weichat_favorite:
                    //微信朋友圈
                    new ShareAction(mContext).setPlatform(SHARE_MEDIA.WEIXIN_FAVORITE)
                            .withText(uShareBean.getText())
                            .withTitle(uShareBean.getTitle())
                            .withTargetUrl(uShareBean.getTargetUrl())
                            .withMedia(image)
                            .setCallback(umShareListener)
                            .share();
                    break;

                case R.id.ll_qzone:
                    //QQ空间
                    new ShareAction(mContext).setPlatform(SHARE_MEDIA.QZONE)
                            .withText(uShareBean.getText())
                            .withTitle(uShareBean.getTitle())
                            .withTargetUrl(uShareBean.getTargetUrl())
                            .withMedia(image)
                            .setCallback(umShareListener)
                            .share();
                    break;

                case R.id.ll_copyurl:
                    //复制链接
                    ClipboardManager myClipboard = (ClipboardManager)mContext.getSystemService(CLIPBOARD_SERVICE);
                    ClipData myClip;
                    String url = uShareBean.getTargetUrl();//如果有内容直接添加就好
                    myClip = ClipData.newPlainText("url", url);//text是内容
                    myClipboard.setPrimaryClip(myClip);
                    ToastUtil.showText(mContext,"已复制到剪切板");
                    dialog.dismiss();
                    break;

                case R.id.ll_qqfriend:
                    //QQ好友
                    new ShareAction(mContext).setPlatform(SHARE_MEDIA.QQ)
                            .withText(uShareBean.getText())
                            .withTitle(uShareBean.getTitle())
                            .withTargetUrl(uShareBean.getTargetUrl())
                            .withMedia(image)
                            .setCallback(umShareListener)
                            .share();
                    break;
            }
        }
    };

    private  UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(mContext,platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            Log.d("plat","platform"+platform);
            dialog.dismiss();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(mContext,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            if (platform.equals(SHARE_MEDIA.QQ)||platform.equals(SHARE_MEDIA.QZONE)){
//                Toast.makeText(mContext,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(mContext,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
//            }

            dialog.dismiss();
        }
    };


    private  void createUserFavorite(TreatMentShareBean bean , String token){

        HttpService.getHttpService().postCreateUserFavorite(bean,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        android.util.Log.i("hcb","createUserFavorite onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        android.util.Log.i("hcb","createUserFavorite onError");
                        android.util.Log.i("hcb","e" + e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        android.util.Log.i("hcb","createUserFavorite onNext");
                        if (createOrDeleteBean.getResult()){
                            ToastUtil.showText(mContext,"收藏成功");
                            dialog.dismiss();
                        }
                    }
                });
    }

}
