package cxzx.bdyx.com.retrofitutild.tools_utils.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cxzx.bdyx.com.retrofitutild.permission_utils.PermissionConstract;
import cxzx.bdyx.com.retrofitutild.permission_utils.PermissionInterface;
import cxzx.bdyx.com.retrofitutild.permission_utils.PermissionUtils;

/**
 * Created by QunCheung on 2018/3/16.
 */

public class BlueToothUtils implements PermissionInterface, OnBlueToothDeviceFound {
    private final BluetoothAdapter mBluetoothAdapter;
    private Activity activity;
    private int REQUEST_ENABLE = 10010;
    private PermissionUtils permissionUtils;
    private BlueToothReceive blueToothReceive;
    private IntentFilter filter;
    private List<BluetoothDevice> devices;

    /**
     * 打开蓝牙
     * @param activity
     */
    public BlueToothUtils(Activity activity) {
        this.activity = activity;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        permissionUtils = new PermissionUtils();
        checkPermission();
        devices = new ArrayList<>();
    }

    /**
     * 检查蓝牙权限
     */
    private void checkPermission(){
        permissionUtils.checkoutPermission(activity, PermissionConstract.BLUETOOTH,this);
    }

    /**
     * 拥有蓝牙权限且拥有蓝牙模块的情况下,打开蓝牙
     */
    private void openBlueTooth(){

        if(!mBluetoothAdapter.isEnabled()){
            //弹出对话框提示用户是后打开
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enabler, REQUEST_ENABLE);
            //不做提示，直接打开，不建议用下面的方法，有的手机会有问题。
            // mBluetoothAdapter.enable();
        }
    }

    /**
     * 获取本机蓝牙基本信息
     */
    public void getBlueToothInfo(){
        //获取本机蓝牙名称
        String name = mBluetoothAdapter.getName();
        Log.i("bluetooth", "name: "+name);
        //获取本机蓝牙地址
        String address = mBluetoothAdapter.getAddress();
        Log.i("bluetooth", "address: "+address);
        //获取已配对蓝牙设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice bonddevice:devices){
            Log.i("bluetooth", "getBlueToothInfo: "+devices.toString());
        }
    }

    /**
     * 安全权限回调
     */
    @Override
    public void onHadPermission() {
        openBlueTooth();
    }

    /**
     * 安全权限回调
     */
    @Override
    public void onPermissionFailed() {
        Toast.makeText(activity,"未能获取蓝牙权限",Toast.LENGTH_SHORT).show();
    }

    /**
     * 调用方法,将参数传给permissionUtils工具类
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onPermissionResult(int requestCode, String[] permissions, int[] grantResults){
        permissionUtils.onRequestResult(requestCode, permissions, grantResults);
    }

    /**
     * 回调,获取用户时候开启蓝牙
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityRequest(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_ENABLE){
            if(resultCode == activity.RESULT_OK){
                //YES 用户允许
                Toast.makeText(activity,"蓝牙已打开",Toast.LENGTH_SHORT).show();
            }
            if(resultCode == activity.RESULT_CANCELED){
                // NO 用户取消
                Toast.makeText(activity,"用户不同意打开蓝牙",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 扫描设备
     */
    public void ScanDevice(){
        blueToothReceive = new BlueToothReceive();
        blueToothReceive.setBlueToothDeviceFound(this);
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(blueToothReceive,filter);
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * 停止扫描设备
     */
    public void StopScanDevice(){
        mBluetoothAdapter.cancelDiscovery();
        activity.unregisterReceiver(blueToothReceive);
    }

    /**
     * 发现的设备
     * @param device
     */
    @Override
    public void onDeviceFound(BluetoothDevice device) {
        devices.add(device);
    }
}
