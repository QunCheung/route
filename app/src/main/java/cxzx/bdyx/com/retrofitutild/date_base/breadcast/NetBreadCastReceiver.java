package cxzx.bdyx.com.retrofitutild.date_base.breadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houyongliang on 2018/3/28 17:24.
 * Function(功能):
 */

public class NetBreadCastReceiver extends BroadcastReceiver {
    public static final int WIFI = 100;
    public static final int MOBIO = 101;
    public static final int DISCONNECT = 102;
    public static final int NOAVAILABLE = 103;
    //    public static final int BLUETOOTH = 104;
    private List<OnNetChangeListener> list;
    private static NetBreadCastReceiver defaultInstance;

    public NetBreadCastReceiver(OnNetChangeListener mListener) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(mListener);
    }

    public NetBreadCastReceiver() {

    }

    public static NetBreadCastReceiver getDefault() {
        if (defaultInstance == null) {
            synchronized (NetBreadCastReceiver.class) {
                if (defaultInstance == null) {
                    defaultInstance = new NetBreadCastReceiver();
                }
            }
        }
        return defaultInstance;
    }

    public void addObserver(OnNetChangeListener mListener) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (list.contains(mListener)) {
            return;
        }
        list.add(mListener);
    }

    public void removeObserver(OnNetChangeListener mListener) {
        if (list == null) {
            return;
        }
        if (list.contains(mListener)) {
            list.remove(mListener);
        }

    }


    private OnNetChangeListener listener;
    private int STATE = NOAVAILABLE;//默认不连接

    @Override
    public void onReceive(Context context, Intent intent) {
        if (list == null) {
            return;
        }
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo == null) {
                /** 没有任何网络 */
                if (STATE != DISCONNECT) {
                    for (OnNetChangeListener onNetChangeListener : list) {
                        onNetChangeListener.onDisConnect();
                    }
                    STATE = DISCONNECT;
                }

                return;
            }
            if (!networkInfo.isConnected()) {
                /** 网络断开或关闭 */
                if (STATE != DISCONNECT) {
                    for (OnNetChangeListener onNetChangeListener : list) {
                        onNetChangeListener.onDisConnect();
                    }
                    STATE = DISCONNECT;
                }
                return;
            }

            if (NetworkInfo.State.CONNECTED == networkInfo.getState() && networkInfo.isAvailable()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    /** wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接 */
                    if (STATE != WIFI) {
                        for (OnNetChangeListener onNetChangeListener : list) {
                            onNetChangeListener.onWifi();
                        }
                        STATE = WIFI;
                    }

                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    /** 移动数据连接,不能与连接共存,如果wifi打开，则自动关闭 */
                    if (STATE != MOBIO) {
                        for (OnNetChangeListener onNetChangeListener : list) {
                            onNetChangeListener.onMobile();
                        }
                        STATE = MOBIO;
                    }

                }
            } else {
                /** 未知网络 */
                if (STATE != DISCONNECT) {
                    for (OnNetChangeListener onNetChangeListener : list) {
                        onNetChangeListener.onDisConnect();
                    }
                    STATE = DISCONNECT;
                }
            }
        }

    }


    public interface OnNetChangeListener {
        // wifi
        void onWifi();

        // 手机
        void onMobile();

        // 网络断开
        void onDisConnect();

//        // 网路不可用
//        void onNoAvailable();
        /*蓝牙*/
//        void onBluetooth();
    }
}
