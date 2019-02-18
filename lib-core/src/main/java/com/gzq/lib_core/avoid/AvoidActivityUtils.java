package com.gzq.lib_core.avoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gzq.lib_core.utils.ActivityUtils;

/**
 * 检查有没有登录的Activity跳转工具类
 * 如果还没有登录，先跳转到登录页面登录，登录成功后，再继续之前的操作
 * 记住：一定要在登录页面登录成功之后，setResult(Activity.RESULT_OK)
 */
public class AvoidActivityUtils {
    /**
     * @param trueActivity
     * @param loginActivity
     * @param condition
     * @param result
     */
    public static void startActivityWithCheck(final Class<?> trueActivity,
                                              Class<?> loginActivity,
                                              SkipActivityCondition condition,
                                              final SkipActivityResult result) {
        startActivityWithCheck(trueActivity, null, loginActivity, condition, result);
    }

    /**
     * @param trueActivity
     * @param bundle
     * @param loginActivity
     * @param condition
     * @param result
     */
    public static void startActivityWithCheck(final Class<?> trueActivity,
                                              final Bundle bundle,
                                              Class<?> loginActivity,
                                              SkipActivityCondition condition,
                                              final SkipActivityResult result) {
        if (condition != null) {
            if (condition.onCondition()) {
                if (result != null) {
                    result.onSuccess();
                }
                if (bundle != null) {
                    ActivityUtils.skipActivity(trueActivity, bundle);
                } else {
                    ActivityUtils.skipActivity(trueActivity);
                }
                return;
            }
            new AvoidOnResult(ActivityUtils.currentActivity())
                    .startForResult(loginActivity, new AvoidOnResult.Callback() {
                        @Override
                        public void onActivityResult(int resultCode, Intent data) {
                            if (result != null) {
                                result.onState(resultCode, data);
                            }
                            if (resultCode == Activity.RESULT_OK) {
                                if (result != null) {
                                    result.onSuccess();
                                }
                                if (bundle != null) {
                                    ActivityUtils.skipActivity(trueActivity, bundle);
                                } else {
                                    ActivityUtils.skipActivity(trueActivity);
                                }
                            } else if (resultCode == Activity.RESULT_CANCELED) {
                                if (result != null) {
                                    result.onCancel();
                                }
                            }
                        }
                    }, 1000);
        } else {
            throw new IllegalArgumentException("Must implements SkipActivityCondition");
        }


    }

    /**
     * @param trueActivity
     * @param requestCode
     * @param loginActivity
     * @param condition
     * @param result
     */
    public static void startActivityForResultWithCheck(final Class<?> trueActivity,
                                                       final int requestCode,
                                                       Class<?> loginActivity,
                                                       SkipActivityCondition condition,
                                                       final SkipActivityResult result) {
        startActivityForResultWithCheck(trueActivity, null, requestCode, loginActivity, condition, result);
    }

    /**
     * @param trueActivity
     * @param bundle
     * @param requestCode
     * @param loginActivity
     * @param condition
     * @param result
     */
    public static void startActivityForResultWithCheck(final Class<?> trueActivity,
                                                       final Bundle bundle,
                                                       final int requestCode,
                                                       Class<?> loginActivity,
                                                       SkipActivityCondition condition,
                                                       final SkipActivityResult result) {
        if (condition != null) {
            if (condition.onCondition()) {
                if (result != null) {
                    result.onSuccess();
                }
                if (bundle != null) {
                    ActivityUtils.skipActivity(trueActivity, bundle);
                } else {
                    ActivityUtils.skipActivity(trueActivity);
                }
                return;
            }
            new AvoidOnResult(ActivityUtils.currentActivity())
                    .startForResult(loginActivity, new AvoidOnResult.Callback() {
                        @Override
                        public void onActivityResult(int resultCode, Intent data) {
                            if (result != null) {
                                result.onState(resultCode, data);
                            }
                            if (resultCode == Activity.RESULT_OK) {
                                if (result != null) {
                                    result.onSuccess();
                                }
                                if (bundle != null) {
                                    ActivityUtils.skipActivityForResult(trueActivity, bundle, requestCode);
                                } else {
                                    ActivityUtils.skipActivityForResult(trueActivity, requestCode);
                                }
                            } else if (resultCode == Activity.RESULT_CANCELED) {
                                if (result != null) {
                                    result.onCancel();
                                }
                            }
                        }
                    }, 1000);
        } else {
            throw new IllegalArgumentException("Must implements SkipActivityCondition");
        }
    }
}
