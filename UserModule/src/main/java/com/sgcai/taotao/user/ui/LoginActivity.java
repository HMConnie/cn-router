package com.sgcai.taotao.user.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sgcai.router.common.app.App;
import com.sgcai.router.common.dialog.ProgresDialog;
import com.sgcai.taotao.user.network.api.UserServices;
import com.sgcai.taotao.user.network.model.req.LoginVerifyParam;
import com.sgcai.taotao.user.network.model.resp.UserResult;
import com.sgcai.router.common.retrofit.HttpTimeException;
import com.sgcai.router.common.retrofit.NetWorkSubscriber;
import com.sgcai.router.common.retrofit.ServiceGenerator;
import com.sgcai.router.common.utils.RouterHub;
import com.sgcai.taotao.common.service.AppService;
import com.sgcai.taotao.user.R;
import com.sgcai.taotao.user.compontent.DaggerLoginActivityComponent;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Route(path = RouterHub.USER_LOGIN_ACTIVITY)
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnLogin;

    @Autowired(name = RouterHub.APP_COMMUNITION_SERVICE)
    AppService appService;

    @Inject
    ServiceGenerator serviceGenerator;

    @Inject
    Toast toast;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_login);
        initView();
    }

    private void initView() {
        DaggerLoginActivityComponent.builder().appComponent(App.getInstance().getAppComponent()).build().inject(this);
        ARouter.getInstance().inject(this);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);

        Log.e(App.class.getSimpleName(), serviceGenerator.toString());
        Log.e(App.class.getSimpleName(), sharedPreferences.toString());
        Log.e(App.class.getSimpleName(), toast.toString());
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBtnLogin)) {
            login();
        }
    }

    private void login() {
        Log.e(App.class.getSimpleName(), serviceGenerator.toString());

        final ProgresDialog progresDialog = new ProgresDialog(this);
        progresDialog.show();
        LoginVerifyParam loginVerifyParam = new LoginVerifyParam("18311380063", "123456789");
        serviceGenerator.createService(loginVerifyParam, UserServices.class).login(loginVerifyParam.getBodyParams())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetWorkSubscriber<UserResult>() {
                    @Override
                    protected void onError(HttpTimeException ex) {
                        progresDialog.dismiss();
                        toast.setText(ex.getMessage());
                        toast.show();
                    }

                    @Override
                    public void onNext(UserResult userResult) {
                        progresDialog.dismiss();
                        Log.e(LoginActivity.class.getSimpleName(), userResult.toString());
                        if (appService == null) return;

                        appService.loginCallback("登录成功了");
                    }
                });
    }
}
