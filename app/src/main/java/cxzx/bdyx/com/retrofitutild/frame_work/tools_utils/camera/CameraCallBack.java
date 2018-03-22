package cxzx.bdyx.com.retrofitutild.frame_work.tools_utils.camera;

/**
 * Created by QunCheung on 2018/3/16.
 * 相机相册工具类,回调接口
 */

public interface CameraCallBack {
    //path 相机:String类型文件路径.相册:Uri图片类型路径
    void photoPath(String path);
}
