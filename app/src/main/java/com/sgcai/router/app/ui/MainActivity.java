package com.sgcai.router.app.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sgcai.router.app.R;
import com.sgcai.router.app.compontent.DaggerMainActivityComponent;
import com.sgcai.router.common.app.App;
import com.sgcai.router.common.base.BaseActivity;
import com.sgcai.router.common.component.AppComponent;
import com.sgcai.router.common.retrofit.ServiceGenerator;
import com.sgcai.router.common.utils.EventBusTags;
import com.sgcai.router.common.utils.GlobalConstants;
import com.sgcai.router.common.utils.RouterHub;
import com.sgcai.router.group.ui.GroupDetailActivity;
import com.sgcai.taotao.common.service.GroupService;
import com.sgcai.taotao.common.service.UserService;
import com.sgcai.taotao.common.to.GroupTO;
import com.sgcai.taotao.common.to.UserTO;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import javax.inject.Inject;


@Route(path = RouterHub.APP_MAIN_ACTIVITY)
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Button mButton1;
    private Button mButton2;

    private TextView mTextView;

    @Autowired(name = RouterHub.GROUP_COMMUNITION_SERVICE)
    GroupService groupService;

    @Autowired(name = RouterHub.USER_COMMUNITION_SERVICE)
    UserService userService;


    @Inject
    Toast toast;
    private Button mBtnNetwork;


    @Inject
    ServiceGenerator serviceGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void inject(AppComponent appComponent) {
        DaggerMainActivityComponent.builder().appComponent(appComponent).build().inject(this);
        ARouter.getInstance().inject(this);
    }

    private void initView() {
        EventBus.getDefault().register(this);
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);
        mTextView = (TextView) findViewById(R.id.textView);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);


        Log.e(App.class.getSimpleName(), serviceGenerator.toString());
        Log.e(App.class.getSimpleName(), toast.toString());

        mBtnNetwork = (Button) findViewById(R.id.btn_network);
        mBtnNetwork.setOnClickListener(this);

    }


    @Subscriber(tag = EventBusTags.LOGIN_STATE_TAG)
    public void receiveEvent(boolean loginState) {
        toast.setText("loginState:" + loginState);
        toast.show();
    }

    @Subscriber(tag = EventBusTags.APP_CALLBACK_TAG)
    public void receiveLoginCallback(String callback) {
        toast.setText("callback:" + callback);
        toast.show();
        mTextView.setText(callback);

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                ARouter.getInstance().build(RouterHub.GROUP_GROUPLIST_ACTIVITY)
                        .withString(GlobalConstants.BUNDLE_STR_KEY, "12355453734457")
                        .navigation(this);
                break;
            case R.id.button2:
                if (groupService == null) return;

                GroupTO groupInfo = groupService.getGroupInfo();
                toast.setText(groupInfo.getName());
                toast.show();

                Log.e(TAG, groupService.hello("Connie"));


                if (userService == null) return;

                UserTO userInfo = userService.getUserInfo();
                Log.e(GroupDetailActivity.class.getSimpleName(), userInfo.getUserName());
                break;
            case R.id.btn_network:
                ARouter.getInstance().build(RouterHub.USER_LOGIN_ACTIVITY).navigation(this);
                break;
        }
    }


}
