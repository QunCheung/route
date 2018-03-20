package cxzx.bdyx.com.retrofitutild.tools_utils.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by QunCheung on 2018/3/16.
 */

public class BlueToothReceive extends BroadcastReceiver{
    public void setBlueToothDeviceFound(OnBlueToothDeviceFound blueToothDeviceFound) {
        this.blueToothDeviceFound = blueToothDeviceFound;
    }

    private OnBlueToothDeviceFound blueToothDeviceFound;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // When discovery finds a device
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            // Get the BluetoothDevice object from the Intent
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            // Add the name and address to an array adapter to show in a ListView
            if (null != blueToothDeviceFound){
                blueToothDeviceFound.onDeviceFound(device);
            }
        }
    }
}
