package com.sgcai.router.group.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sgcai.router.common.base.BaseActivity;
import com.sgcai.router.common.component.AppComponent;
import com.sgcai.router.common.utils.EventBusTags;
import com.sgcai.router.common.utils.GlobalConstants;
import com.sgcai.router.common.utils.RouterHub;
import com.sgcai.router.group.R;

import org.simple.eventbus.EventBus;


/**
 * Created by hinge on 18/6/25.
 */
@Route(path = RouterHub.GROUP_GROUPDETAIL_ACTIVITY)
public class GroupDetailActivity extends BaseActivity implements View.OnClickListener {

    @Autowired(name = GlobalConstants.BUNDLE_STR_KEY)
    String data;

    private Button mButton;
    private Button mBtnTologin;


    @Override
    public void inject(AppComponent appComponent) {
        ARouter.getInstance().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity_detail);
        initView();
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.btn_send);
        mButton.setOnClickListener(this);
        mBtnTologin = (Button) findViewById(R.id.btn_tologin);
        mBtnTologin.setOnClickListener(this);

        Log.e(GroupDetailActivity.class.getSimpleName(), data);

    }

    @Override
    public void onClick(View v) {
        if (v.equals(mButton)) {
            EventBus.getDefault().post(true, EventBusTags.LOGIN_STATE_TAG);
        } else if (v.equals(mBtnTologin)) {
            ARouter.getInstance().build(RouterHub.USER_LOGIN_ACTIVITY).navigation(this);
        }
    }
}
