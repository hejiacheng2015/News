package com.news.function.card;

import android.content.DialogInterface;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alertdialogpro.AlertDialogPro;
import com.news.R;
import com.news.base.BaseActivity;
import com.news.constant.Local;
import com.news.network.NetWork;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/14.
 */
public class SZCardActivity extends BaseActivity {
    private static final String TAG = "SZCardActivity";
    @Bind(R.id.tb_toolbar)
    Toolbar toolbar;
    @Bind(R.id.account)
    TextView account;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_card_layout;
    }

    @Override
    protected void initView() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_36dp));
        toolbar.setTitle("深圳通");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    public void getData(View view){
        String txt = account.getText().toString().trim();
        if(txt.equals("")){
            AlertDialogPro.Builder builder = new AlertDialogPro.Builder(SZCardActivity.this);
            builder.setTitle("系统提示").setMessage("请输入卡号！").show();
        }else {
            progress.setVisibility(View.VISIBLE);
            NetWork.getNetWorkApi2()
                    .getCard(Local.SPORT_API_KEY, txt, "json")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    Observer<String> observer = new Observer<String>() {
        @Override
        public void onCompleted() {
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            progress.setVisibility(View.GONE);
           e.printStackTrace();
        }

        @Override
        public void onNext(String s) {
            Log.e(TAG, "onNext: " + s);
            progress.setVisibility(View.GONE);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                String code = jsonObject.optString("code","");
                String success = jsonObject.optString("success","");
                if(code.equals("0")&&success.equals("true")){
                    JSONObject obj = jsonObject.getJSONObject("data");
                    String[] CardMsg = new String[5];
                    CardMsg[0] ="卡号:"+ obj.optString("card_number","");
                    CardMsg[1] ="余额:"+ obj.optString("card_balance","");
                    CardMsg[2] ="余额截止时间:"+ obj.optString("balance_time","");
                    CardMsg[3] ="卡有效期:"+ obj.optString("card_validity","");
                    CardMsg[4] ="查询时间:"+ obj.optString("current_time","");
                    AlertDialogPro.Builder builder = new AlertDialogPro.Builder(SZCardActivity.this);
                    builder.setTitle("查询成功")
                            .setItems(CardMsg, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }else {
                    AlertDialogPro.Builder builder = new AlertDialogPro.Builder(SZCardActivity.this);
                    builder.setTitle("查询失败").setMessage("请输入正确卡号信息！").show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    
    @Override
    protected void initData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
