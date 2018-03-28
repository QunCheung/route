package cxzx.bdyx.com.retrofitutild.date_api.tcp_ip;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cxzx.bdyx.com.retrofitutild.Socket2Message;
import cxzx.bdyx.com.retrofitutild.SocketAidl;

/**
 * Created by QunCheung on 2018/3/26.
 */

public class AidlService extends Service{

    private static final String TAG = "BMS";
    //存client(Activity)传递过来的信息
    private List<String> messageList = new ArrayList<>();
    //aidl回调list
    private RemoteCallbackList<Socket2Message> listenerList = new RemoteCallbackList<>();
    //aidl接口生成的binder
    private Binder binder = new SocketAidl.Stub() {
        @Override
        public void sendMessage(int type,String message) throws RemoteException {
            messageList.add(message);
            switch (type){
                case SocketConstant.CONNECT_SOCKET:
                    try {
                        socketConnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case SocketConstant.SEND_MSG:
                    sendMessageBySocket(message.getBytes());
                    break;
                case SocketConstant.DISCONNECT_SOCKET:
                    disConnectSocket();
                    break;
            }
        }

        @Override
        public String reMessage() throws RemoteException {
            return messageList.size() == 0?"消息是空的":messageList.get(messageList.size()-1);
        }

        @Override
        public void registerListener(Socket2Message listener) throws RemoteException {
            listenerList.register(listener);
        }

        @Override
        public void unregisterListener(Socket2Message listener) throws RemoteException {
            listenerList.unregister(listener);
        }
    };
    //socket对象
    private Socket tcpClient;
    //socket地址对象
    private InetSocketAddress socAddress;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Th()).start();
    }

    /**
     * 回收list
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        listenerList.kill();
    }

    /**
     * 测试aidl正常工作,从service传递信息到Activity
     */
    class Th implements Runnable{

        @Override
        public void run() {
            int a = 0;
            while (true){
                sendMsg2Client(a+"");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                a++;
            }
        }
    }

    /**
     * 通过aidl传递信息到activity
     * @param msg
     */
    private void sendMsg2Client(String msg) {
        messageList.add(msg);
        int N = listenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            Socket2Message item = listenerList.getBroadcastItem(i);
            if (null != item){
                try {
                    item.onNewMessageArrive(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        listenerList.finishBroadcast();
    }

    /**
     * 连接socket,或者重连socket
     * @throws RemoteException
     * @throws IOException
     * @throws InterruptedException
     */
    private void socketConnect() throws IOException, InterruptedException {
        if (null == tcpClient)
            tcpClient = new Socket();
        if (tcpClient.isConnected()){
            sendMsg2Client("connect success");
        }else{
            socAddress = new InetSocketAddress("192.168.1.78",18090);
            tcpClient.connect(socAddress, 1000);//1秒超时
            if (tcpClient.isConnected()){
                sendMsg2Client("connect success");
                //监听socket的信息或者心跳包
                beginObsSocketService();
                //发送心跳包
                keepSocketConnect();
            }else{
                sendMsg2Client("connect failed , and 2 second late will reconnected");
                Thread.sleep(2000);
                socketConnect();
            }
        }
    }

    /**
     * 定时发送心跳包
     */
    private void keepSocketConnect() {
        //todo 改线程需要优化
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(20000);
                        sendMessageBySocket("heart".getBytes());
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 开启监听socketservice的线程
     */
    private void beginObsSocketService() {
        //todo 线程需要优化
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    obsSocketMessage();
                }
            }
        }).start();
    }

    /**
     * 通过socket发送消息给service
     * @param bytes
     * @throws RemoteException
     */
    private void sendMessageBySocket(byte[] bytes){
        if (null == tcpClient || !tcpClient.isConnected()){
            sendMsg2Client("send msg failed , socket is disconnected !");
            return;
        }
        OutputStream os = null;
        try {
            os = tcpClient.getOutputStream();
            os.write(bytes);
            os.flush();
            sendMsg2Client("send msg success");
        } catch (IOException e) {
            e.printStackTrace();
            sendMsg2Client("send msg failed , cause by"+e.toString());
        }
    }

    /**
     * 读取socket返回的数据
     */
    private void obsSocketMessage(){
        if (null == tcpClient || !tcpClient.isConnected()){
            sendMsg2Client("socket is not connect !");
            return;
        }
        //定义长度很大的数组,用来存储socket传过来的数据.
        byte[] byteArrayIn = new byte[1400];
        // 接收数据
        // message head 长度
        int headerLen = 0;
        try {
            headerLen = tcpClient.getInputStream().read(byteArrayIn, 0, 1);
            //保证开始的第一个字节为 0x7E,如果不是，继续读下一个
            //以空格--"0x7e"为消息的开始标志.寻找信息开始位置
            if (headerLen != 1 || byteArrayIn[0] != 0x7E) {
                return;
            }
            int pos = 1;
            while (true) {
                tcpClient.getInputStream().read(byteArrayIn, pos++, 1);
                // 处理连包时，上一包只有后半部分，和新包连在一起的情况。上半包全丢弃，从下一个完整包开始
                if (byteArrayIn[1] == 0x7E) {
                    pos--;
                    continue;
                }
                if (byteArrayIn[pos - 1] == 0x7E)
                    break;
            }
            int bytesLen = pos;
            // 以上接收信息,一下信息处理
            if (bytesLen > 0) {
                byte[] bytes = new byte[bytesLen];
                System.arraycopy(byteArrayIn, 0, bytes, 0, bytesLen);
                //todo 发送字节,可以先做处理
                sendMsg2Client(bytes.toString());
            }else{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("客户端数据接收失败", e.getMessage());
        }
    }

    /**
     * 断开socket连接
     */
    private void disConnectSocket(){
        if (null != tcpClient){
            if (tcpClient.isConnected()) {
                try {
                    tcpClient.close();
                    sendMsg2Client("disconnect socket success !");
                } catch (IOException e) {
                    e.printStackTrace();
                    sendMsg2Client("disconnect socket failed !");
                }
            }
            tcpClient = null;
        }
    }
}
