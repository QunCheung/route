// Socket2Message.aidl
package cxzx.bdyx.com.retrofitutild;
// Declare any non-default types here with import statements

interface Socket2Message {
//service 远程回调
    void onNewMessageArrive(String message);
}
