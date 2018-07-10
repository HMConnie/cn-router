package com.sgcai.router.group.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sgcai.router.common.utils.GlobalConstants;
import com.sgcai.router.common.utils.RouterHub;
import com.sgcai.router.group.R;


/**
 * Created by hinge on 18/6/21.
 */

@Route(path = RouterHub.GROUP_GROUPLIST_ACTIVITY)
public class GroupListActivity extends AppCompatActivity implements View.OnClickListener {

    @Autowired(name = GlobalConstants.BUNDLE_STR_KEY)
    String data;


    private TextView mTextView;
    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity_list);
        initView();

    }

    private void initView() {
        ARouter.getInstance().inject(this);
        mTextView = (TextView) findViewById(R.id.textView);
        mButton = (Button) findViewById(R.id.btn_detail);
        mButton.setOnClickListener(this);

        Log.e(GroupListActivity.class.getSimpleName(), data != null ? data : "");
        mTextView.setText(data != null ? data : "hello world");

    }

    @Override
    public void onClick(View v) {
        if (v.equals(mButton)) {
            ARouter.getInstance().build(RouterHub.GROUP_GROUPDETAIL_ACTIVITY)
                    .withString(GlobalConstants.BUNDLE_STR_KEY, "123")
                    .navigation(this);
        }
    }
}
