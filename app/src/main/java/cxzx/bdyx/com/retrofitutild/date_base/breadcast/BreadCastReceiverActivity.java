package cxzx.bdyx.com.retrofitutild.date_base.breadcast;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cxzx.bdyx.com.retrofitutild.R;

import static cxzx.bdyx.com.retrofitutild.Constant.ACTION_SHOW;
import static cxzx.bdyx.com.retrofitutild.Constant.PERM_PRIVATE;

public class BreadCastReceiverActivity extends AppCompatActivity implements NetBreadCastReceiver.OnNetChangeListener{
    private String TAG=BreadCastReceiverActivity.class.getSimpleName();
    OwnerBroadcastReceiver ownerBroadcastReceiver;
    private NetBreadCastReceiver netBreadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bread_cast_receiver);
        Log.e(TAG,"ASKDFJLAK");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        netBreadCastReceiver = new NetBreadCastReceiver(this);
//        registerReceiver(netBreadCastReceiver,filter);


        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(ACTION_SHOW);

        ownerBroadcastReceiver = new OwnerBroadcastReceiver();
        registerReceiver(ownerBroadcastReceiver,filter1,PERM_PRIVATE,null);
//        registerReceiver(ownerBroadcastReceiver,filter1);

//        sendBroadcast(new Intent(ACTION_SHOW));
        sendBroadcast(new Intent(ACTION_SHOW),PERM_PRIVATE);
        registerReceiver(NetBreadCastReceiver.getDefault(),filter);

    }

    @Override
    protected void onResume() {
        NetBreadCastReceiver.getDefault().addObserver(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        NetBreadCastReceiver.getDefault().removeObserver(this);
        super.onPause();
    }

    @Override
    public void onWifi() {
        Log.e(TAG,"wifi");
    }

    @Override
    public void onMobile() {
        Log.e(TAG,"onMobile");
    }

    @Override
    public void onDisConnect() {
        Log.e(TAG,"onDisConnect");
    }

//    @Override
//    public void onNoAvailable() {
//        Log.e(TAG,"onNoAvailable");
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(NetBreadCastReceiver.getDefault());
        unregisterReceiver(ownerBroadcastReceiver);
    }
}
