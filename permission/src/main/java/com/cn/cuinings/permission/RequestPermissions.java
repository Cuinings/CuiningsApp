package com.cn.cuinings.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.cn.cuinings.permission.em.EmPermissionResult;
import com.cn.cuinings.permission.listener.RequestResult;
import com.cn.cuinings.permission.util.RequestUtil;

public class RequestPermissions extends Activity {

    private static final String TAG = RequestPermissions.class.getSimpleName();

    public static final String PARAM_PERMISSION = "param_permission";
    public static final String PARAM_PERMISSION_CODE = "param_permission_code";
    public static final int PARAM_PERMISSION_DEFAULT_CODE = -1;

    private String[] permissions;
    private int requestCode = PARAM_PERMISSION_DEFAULT_CODE;
    private static RequestResult request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_request);
        permissions = getIntent().getStringArrayExtra(PARAM_PERMISSION);
        requestCode = getIntent().getIntExtra(PARAM_PERMISSION_CODE, PARAM_PERMISSION_DEFAULT_CODE);
        if (null == request) {
            Log.e(TAG, "onCreate: null == request");
            finish();
            return;
        }
        if (null == permissions && requestCode < 0) {
            Log.e(TAG, "onCreate: null == permissions && requestCode < 0");
            request.result(EmPermissionResult.ERR);
            finish();
            return;
        }
        if (RequestUtil.hasRequestedSuccess(this, permissions)) {
            Log.e(TAG, "onCreate: RequestUtil.hasRequestedSuccess(this, permissions)");
            request.result(EmPermissionResult.HAS);
            finish();
            return;
        }
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (null == request) return;
        if (RequestUtil.isRequestSuccess(grantResults)) {
            request.result(EmPermissionResult.SUCCESS);
        } if (RequestUtil.isRequestRationale(this, permissions)) {
            request.result(EmPermissionResult.DENIED);
        } else {
            request.result(EmPermissionResult.CANCEL);
        }
        finish();
    }


    @Override
    public void finish() {
        super.finish();
        //关闭时去除动画
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        request = null;
        permissions = null;
        requestCode = 0;
    }

    public static void request(Context context, String[] value, int requestCode, RequestResult request) {
        RequestPermissions.request = request;
        Intent intent = new Intent();
        intent.setClass(context, RequestPermissions.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray(PARAM_PERMISSION, value);
        bundle.putInt(PARAM_PERMISSION_CODE, requestCode);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
