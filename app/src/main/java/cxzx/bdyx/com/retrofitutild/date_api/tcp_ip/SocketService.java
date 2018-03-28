package cxzx.bdyx.com.retrofitutild.date_api.tcp_ip;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;


/**
 * Created by QunCheung on 2018/3/22.
 */

public class SocketService extends Service {

    private Socket tcpClient;
    //命令状态
    public String Status;
    public final String STATUS_NEW = "New";
    public final String STATUS_PROCESSING = "Processing";
    public final String STATUS_INVALID = "Invalid";
    public final String STATUS_SUCCESS = "Success";
    public final String STATUS_FAILED = "Failed";
    public final String STATUS_OFFLINE = "Offline";
    SocketAddress socAddress;
    public boolean on = true;//代表各线程运行，如果=false则停掉本类所有线程
    private boolean need = false;//记录是否需要在连接通信时鉴权
    public boolean IsConnected = false;//是否开始通信
    public double icut = 0;//发送数据时记录时间戳，收到数据时清为0，每次发送时判断是否持续无应答，如果超过系统参数，则重新鉴权，鉴权次数超过系统参数值，则重新连接
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //开启socket client 连接
        SocketClientThrear socketClientThrear = new SocketClientThrear();
        socketClientThrear.start();
        //监听发送给客户端,需要客户端发送给服务器的事件
        obsCliendSendData();
        obsClientReConnect();
    }
    /**
     * 监听activity是否调动重启连接方法
     */
    private void obsClientReConnect() {
//        RxBus.getInstance().subscribe(SocketReConnectBean.class, new Consumer<SocketReConnectBean>() {
//            @Override
//            public void accept(@NonNull SocketReConnectBean socketReConnectBean) throws Exception {
//                if (socketReConnectBean.isReConnedt()) {
//                    ReConnect(socketReConnectBean.getIP(),socketReConnectBean.getPort(),socketReConnectBean.getBackTime());
//                }
//            }
//        });
    }

    /**
     * 监听,Activity是否调用发送信息接口
     */
    private void obsCliendSendData() {
//        RxBus.getInstance().subscribe(DataMsgBean.class, new Consumer<DataMsgBean>() {
//            @Override
//            public void accept(@NonNull DataMsgBean dataMsgBean) throws Exception {
//                if (tcpClient.isConnected()){
//                    boolean result = sendData(dataMsgBean.getBytes(), dataMsgBean.getMessageId());
//
//                }else{
//
//                }
//            }
//        });
//        RxBus.getInstance().subscribe(SendFailingBean.class, new Consumer<SendFailingBean>() {
//            @Override
//            public void accept(@NonNull SendFailingBean sendFailingBean) throws Exception {
//                if (tcpClient.isConnected()){
//                    sendFailingData(sendFailingBean.getBytes(),sendFailingBean.getCallBack());
//                }else{
//                    UpCommunication();
//                }
//            }
//        });
    }
    /**
     * 返回传递数据后的结果
     * @param result
     */
    private void returnSendMsgResult(boolean result) {
//        SendMsgReultBean sendMsgReultBean = new SendMsgReultBean();
//        if (result){
//            sendMsgReultBean.setResult(SendMsgReultBean.SEND_SUCCESS);
//        }else{
//            sendMsgReultBean.setResult(SendMsgReultBean.SEND_FAILED);
//        }
//        RxBus.getInstance().send(sendMsgReultBean);
    }

    /**
     * 客户端线程
     */
    public class SocketClientThrear extends Thread {
        @Override
        public void run() {
            UpCommunication();
        }
    }

    /**
     * client 开启socket的方法
     */
    public void UpCommunication() {
//        SocketConnectedStatusBean socketConnectedStatusBean = new SocketConnectedStatusBean();
//        try {
//            //建立Socket客户端
//            if (tcpClient == null){
//                tcpClient = new Socket();
//            }
//            if (!tcpClient.isConnected()){
//                socAddress = new InetSocketAddress(App.parameters.sMainServerIPName,
//                        Integer.valueOf(App.parameters.sServerTCPPort));
////            socAddress = new InetSocketAddress("192.168.1.78",18090);
//                tcpClient.connect(socAddress, 1000);//1秒超时
//            }
//            if (tcpClient.isConnected()) {
//                //  2017/7/14 socket 连接成功,向外传递消息
//                socketConnectedStatusBean.setStatus(SocketConnectedStatusBean.CONNECT_SUCCESS);
//                openReceived();
//            } else {
//                //  2017/7/14 socket 连接失败,向外传递消息
//                socketConnectedStatusBean.setStatus(SocketConnectedStatusBean.CONNECT_FAILED);
//                try {
//                    Thread.sleep(2000);
//                    UpCommunication();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        } catch (IOException e) {
//            //  2017/7/14 socket 连接出现异常,向外传递消息
//            socketConnectedStatusBean.setStatus(SocketConnectedStatusBean.CONNECT_EXCEPTION);
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//            UpCommunication();
//        }
//        RxBus.getInstance().send(socketConnectedStatusBean);
    }

    private void openReceived() {
        /**
         * 开启接收数据线程
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (tcpClient.isConnected()){
                    readData();
                }
                UpCommunication();
            }
        }).start();
    }

    /**
     * 重新连接client的socket
     * @param IP
     * @param port
     * @param backTime
     * @throws IOException
     * @throws InterruptedException
     */
    public void ReConnect(String IP, int port, String backTime) throws IOException, InterruptedException {
        //停止现有数据发送功能
//        App.tcpObject.IsConnected = false;
        //关闭现有连接
        try {
            if (tcpClient != null)
                tcpClient.close();
            //重新开始新连接
            tcpClient = new Socket(IP, port);
            //tc    pClient.Client.Blocking = true;
            tcpClient.setTcpNoDelay(true);
//            App.tcpObject.IsConnected = true;
            //如果连接到指定服务器时限不为0，则，在时限到达时，重新连接回原服务器
            if (!backTime.equals("0")) {
                int tick = Integer.parseInt(backTime);
                Thread.sleep(1000 * tick * 60);
//                ReConnect(App.parameters.sMainServerIPName, Integer.parseInt(App.parameters.sServerTCPPort), "0");
            }
        } catch (RuntimeException ex) {
//            TerminalLog.e("ReConnect Error!" + ex.getMessage());
            //Close();
        }
    }

    /**
     * 发送数据
     * @param bytes
     * @param messageID
     * @return
     */
    private boolean sendData(byte[] bytes,String messageID){
        Status = STATUS_PROCESSING;
        try {
            //发送
            OutputStream os = tcpClient.getOutputStream();
            os.write(bytes);
            os.flush();
            Status = STATUS_SUCCESS;
            return true;
        } catch (Exception e) {
//            TerminalLog.e("发送数据：" + messageID + "消息发送失败！  " + e.getMessage());
            Status = STATUS_OFFLINE;
            return false;
        }
    }

    /**
     * 发送failingData
     */
    private void sendFailingData(byte[] bytes/*, RxBusCallBack callBack*/){
        //发送
        try {
            OutputStream os = tcpClient.getOutputStream();
            os.write(bytes);
            os.flush();
//            if (callBack != null){
//                callBack.success("");
//            }
        } catch (IOException e) {
//            if (callBack != null){
//                callBack.failed("");
//            }
        }
    }

    private int bytesLen;
    /**
     * 接收数据并处理
     */
    private void readData(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
