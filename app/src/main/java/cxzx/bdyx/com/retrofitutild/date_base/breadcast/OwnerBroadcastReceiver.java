package cxzx.bdyx.com.retrofitutild.date_base.breadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class OwnerBroadcastReceiver extends BroadcastReceiver {
    /*使用的时候 添加 自定义的 权限 可在app 内传输数据*/
//        <permission
//    android:name="cxzx.bdyx.com.retrofitutild.PRIVATE"
//    android:protectionLevel="signature"></permission>
//    <uses-permission android:name="cxzx.bdyx.com.retrofitutild.PRIVATE"></uses-permission>

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "接受阿凡达发送了", Toast.LENGTH_SHORT).show();
        Log.e("adf", "接受了");
    }
}
