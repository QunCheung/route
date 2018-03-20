package cxzx.bdyx.com.retrofitutild.permission_utils;

/**
 * Created by QunCheung on 2018/3/14.
 * 本地权限请求,回调接口
 */

public interface PermissionInterface {
    //拥有当前需要的权限
    void onHadPermission();
    //不能获取当前需要的权限,或者获取失败
    void onPermissionFailed();
}
