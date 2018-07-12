package com.sgcai.router.common.base;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.sgcai.router.common.utils.DefaultEvent;
import com.sgcai.router.common.utils.Events;
import com.sgcai.router.common.utils.GlobalConstants;
import com.sgcai.router.common.utils.PermissionUtil;
import com.sgcai.router.common.utils.RxBus;


/**
 * Created by hinge on 17/4/12.
 */

public class PermissionShadowActivity extends AppCompatActivity {


    private static final int REQUEST_CODE = 100;//请求吗
    private String mPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        mPermission = intent.getStringExtra("permission");
        if (TextUtils.equals(Manifest.permission.ACCESS_FINE_LOCATION, mPermission)) {//如果当前传输过来是定位
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE);
        } else if (TextUtils.equals(Manifest.permission.CAMERA, mPermission)) {
            startActivityForResult(PermissionUtil.getAppDetailSettingIntent(this), REQUEST_CODE);
        } else {
            finish(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean isOpen = false;
        if (requestCode == REQUEST_CODE) {
            if (TextUtils.equals(Manifest.permission.ACCESS_FINE_LOCATION, mPermission)) {//如果当前传输过来是定位
                isOpen = PermissionUtil.isOPenGPS(this);
            } else if (TextUtils.equals(Manifest.permission.CAMERA, mPermission)) {
                isOpen = PermissionUtil.isCameraCanUse();
            }
            finish(isOpen);
        }

    }

    private void finish(boolean isOpen) {
        RxBus.getInstance().send(Events.DEFAULT, new DefaultEvent(GlobalConstants.EventCode.PERMISSION_INFO, isOpen));
        finish();
    }


}
