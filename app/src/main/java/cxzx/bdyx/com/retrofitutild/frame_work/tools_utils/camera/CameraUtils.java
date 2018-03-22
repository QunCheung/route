package cxzx.bdyx.com.retrofitutild.frame_work.tools_utils.camera;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

import cxzx.bdyx.com.retrofitutild.frame_work.permission_utils.PermissionConstract;
import cxzx.bdyx.com.retrofitutild.frame_work.permission_utils.PermissionInterface;
import cxzx.bdyx.com.retrofitutild.frame_work.permission_utils.PermissionUtils;

/**
 * Created by QunCheung on 2018/3/16.
 * 调用系统相机
 * 1.实例化此类
 * 2.调用startCamera()方法
 * 3.重要!!!onPermissionRequest(),onActivityRequest()在activity中调用,回传返回结果
 * 4.调用setCameraCallBack()方法.回调参数为String类型的照片路径
 */

public class CameraUtils implements PermissionInterface {
    private String mTempPhotoPath;
    private static final int CAMERA_REQUEST_CODE = 10099;
    private static final int GALLERY_REQUEST_CODE = 10098;
    private Activity activity;
    private CameraCallBack cameraCallBack;
    private boolean isCamera = true;
    private boolean isPhoto = false;
    private PermissionUtils permissionUtils;

    /**
     * 设置相机相册回调方法
     * @param cameraCallBack
     */
    public void setCameraCallBack(CameraCallBack cameraCallBack) {
        this.cameraCallBack = cameraCallBack;
    }

    /**
     * 实例化相机类,默认,调用相机
     * @param fileName
     * @param activity
     */
    public CameraUtils(String fileName, Activity activity) {
        this.mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() / 1000 +fileName+ ".jpeg";
        this.activity = activity;
        permissionUtils = new PermissionUtils();
    }

    /**
     * 启动相机
     */
    public void startCamera(){
        checkPermission();
    }

    /**
     * 启动相册
     */
    public void startPick(){
        isCamera = false;
        isPhoto = true;
        checkPermission();
    }

    /**
     * 初始化相机路径
     */
    private void checkPermission() {
        //设置图片保存路径,拍照
        permissionUtils.checkoutPermission(activity, PermissionConstract.CAMERA,this);
    }

    /**
     * 调用系统相机
     * @param activity
     */
    private void takeCamera(Activity activity){
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径,适配android N文件权限收回
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, new File(mTempPhotoPath).getAbsolutePath());
        Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(takeIntent,CAMERA_REQUEST_CODE);
    }

    /**
     * 调用系统相册
     * @param activity
     */
    private void takePhoto(Activity activity){
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE);
    }

    /**
     * 权限申请回调,必须在使用的Activity中调用
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onPermissionRequest(int requestCode, String[] permissions, int[] grantResults){
        permissionUtils.onRequestResult(requestCode, permissions, grantResults);
    }

    /**
     * 相机回调,必须在使用的Activity中调用
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityRequest(int requestCode, int resultCode, Intent data){
        if (resultCode == activity.RESULT_OK) {
            switch (requestCode){
                case CAMERA_REQUEST_CODE:
                    //相机回调,照片路径
                    if (null != cameraCallBack)
                        cameraCallBack.photoPath(mTempPhotoPath);
                    break;
                case GALLERY_REQUEST_CODE:
                    //相册回调,照片路径
                        cameraCallBack.photoPath(data.getData().getPath());
                    break;
            }
        }
    }

    /**
     * 判断权限,拥有权限回调
     */
    @Override
    public void onHadPermission() {
        if (isCamera && !isPhoto){
            takeCamera(activity);
        }else{
            takePhoto(activity);
        }
    }

    /**
     * 判断权限,无法过去权限回调
     */
    @Override
    public void onPermissionFailed() {
        Toast.makeText(activity,"未能开启相机权限",Toast.LENGTH_SHORT).show();
    }
}
