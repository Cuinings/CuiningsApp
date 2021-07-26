package com.cn.cuinings.permission.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cn.cuinings.utils.EmptyUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestUtil {

    public static boolean isRequestSuccess(int[] grantResults) {
        if (!EmptyUtil.isEmpty(grantResults)) {
            for (int grantResult: grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isRequestRationale(Activity activity, String[] permissions) {
        if (!EmptyUtil.isEmpty(permissions)) {
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasRequestedSuccess(Context context, String[] permissions) {
        if (EmptyUtil.isEmpty(permissions)) return false;
        for (String permission : permissions) {
            if (!isRequested(context, permission)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isRequested(Context context, String permission) {
        if (EmptyUtil.isEmpty(permission)) {
            return false;
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void invokeAnnotation(Object object, Class permissionClass) {
        try {
            Class<?> clazz = object.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            if (EmptyUtil.isEmpty(methods)) return;
            for (Method method : methods) {
                Log.d("RequestUtil", "invokeAnnotation: " + method.getName());
                method.setAccessible(true);
                if (method.isAnnotationPresent(permissionClass)) {
                    method.invoke(object);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
