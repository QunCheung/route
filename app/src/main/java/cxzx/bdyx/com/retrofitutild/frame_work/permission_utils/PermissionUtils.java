package cxzx.bdyx.com.retrofitutild.frame_work.permission_utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by QunCheung on 2018/3/14.
 * android 本地权限请求工具类,封装在各个本地工具中
 * 1.check权限
 * 2.设置回调
 * 3.activity 必须调用permissionResult()方法
 */

public class PermissionUtils {
    private static final int REQUEST_CODE = 10001;
    private PermissionInterface permissionInterface;
    /**
     * 检查当前是否拥有该权限,没有权限则申请
     * @param activit 当前activity,或者fragment依附的activity
     * @param permission 需要检查的权限
     */
    public void checkoutPermission(Activity activit, String permission, PermissionInterface permission_intetface){
        permissionInterface = permission_intetface;
        if (Build.VERSION.SDK_INT >= 23) {
            //我是在Fragment里写代码的，因此调用getActivity
            //如果不想判断SDK，可以使用ActivityCompat的接口来检查和申请权限
            int hasReadContactsPermission = activit.checkSelfPermission(
                    permission);

            if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
                //这里就会弹出对话框
                activit.requestPermissions(
                        new String[] {permission},
                        REQUEST_CODE);
                return;
            }
            //高版本中检查是否有运行时权限，具有权限时才调用
            permissionInterface.onHadPermission();
        } else {
            //在AndroidManifest.xml中仍然声明使用"android.permission.READ_CONTACTS"
            //在低版本中直接调用该函数
            permissionInterface.onHadPermission();
        }
    }

    /**
     * 必须走回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionInterface.onHadPermission();
                } else {
                    permissionInterface.onPermissionFailed();
                }
                return;
            default:
        }
    }
}
