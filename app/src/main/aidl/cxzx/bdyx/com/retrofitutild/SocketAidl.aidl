// SocketAidl.aidl
package cxzx.bdyx.com.retrofitutild;
import cxzx.bdyx.com.retrofitutild.Socket2Message;
interface SocketAidl {
//client向service发送消息
    void sendMessage(int type,String message);
//client通过此方法,对service处理信息,活的回调
    String reMessage();
//service 2 client
    void registerListener(Socket2Message listener);
    void unregisterListener(Socket2Message listener);
}
