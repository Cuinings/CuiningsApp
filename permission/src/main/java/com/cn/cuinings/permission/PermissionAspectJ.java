package com.cn.cuinings.permission;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.cn.cuinings.permission.annotation.Permission;
import com.cn.cuinings.permission.annotation.PermissionCancel;
import com.cn.cuinings.permission.annotation.PermissionDenied;
import com.cn.cuinings.permission.annotation.PermissionError;
import com.cn.cuinings.permission.annotation.PermissionExist;
import com.cn.cuinings.permission.em.EmPermissionResult;
import com.cn.cuinings.permission.listener.RequestResult;
import com.cn.cuinings.permission.util.RequestUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @Author: 崔宁 github: https://github.com/Cuinings
 * @Email: 1015597172@qq.com
 * @Date: 2021/7/26
 * @Description: 权限切点处理权限切点处理
 */
@Aspect
public class PermissionAspectJ {

    @Pointcut("execution(@com.cn.cuinings.permission.annotation.Permission * *(..)) && @annotation(permission)")
    public void methodPointPermission(Permission permission) {}


    @Around("methodPointPermission(permission)")
    public void aroundProceedingPoint(final ProceedingJoinPoint point, Permission permission) throws Throwable {
        Context context = null;
        final Object object = point.getThis();
        if (object instanceof Context) {
            context = (Context) object;
        } else if (object instanceof Fragment) {
            context = (Context) object;
        }
        if (null == context || null == permission) {
            throw new IllegalAccessException("Context or permission is null");
        }
        RequestPermissions.request(context, permission.value(), permission.requestCode(), new RequestResult() {
            @Override
            public void result(EmPermissionResult result) {
                try {
                    switch (result) {
                        case SUCCESS:
                            point.proceed();
                            break;
                        case CANCEL:
                            RequestUtil.invokeAnnotation(object, PermissionCancel.class);
                            break;
                        case DENIED:
                            RequestUtil.invokeAnnotation(object, PermissionDenied.class);
                            break;
                        case HAS:
                            RequestUtil.invokeAnnotation(object, PermissionExist.class);
                            break;
                        case ERR:
                            RequestUtil.invokeAnnotation(object, PermissionError.class);
                            break;
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }
}
