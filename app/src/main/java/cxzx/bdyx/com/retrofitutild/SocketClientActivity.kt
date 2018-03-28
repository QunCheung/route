package cxzx.bdyx.com.retrofitutild

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import cxzx.bdyx.com.retrofitutild.date_api.tcp_ip.SocketConstant
import cxzx.bdyx.com.retrofitutild.frame_work.permission_utils.PermissionInterface
import cxzx.bdyx.com.retrofitutild.frame_work.tools_utils.bluetooth.BlueToothUtils
import cxzx.bdyx.com.retrofitutild.frame_work.tools_utils.camera.CameraUtils
import kotlinx.android.synthetic.main.activity_main.*

class SocketClientActivity : AppCompatActivity(), PermissionInterface {

    val TAG:String = "activity"
    private var handler:Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            if (msg!!.what == 0){
                Log.i(TAG, msg.obj as String?)
                tv.setText(msg.obj as String)
            }
            super.handleMessage(msg)
        }
    }

    private var listener:Socket2Message = object : Socket2Message.Stub() {
        override fun onNewMessageArrive(message: String?) {
            //此处回调的消息是异步的,需要handler带入UI线程
            handler.obtainMessage(0,message).sendToTarget()
        }
    }

    private var messageManager: SocketAidl? = null
    var conn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messageManager = SocketAidl.Stub.asInterface(service)
            messageManager!!.registerListener(listener)
        }
    }

    override fun onHadPermission() {
        Toast.makeText(this,"isHad",Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionFailed() {
        Toast.makeText(this,"isFailed",Toast.LENGTH_SHORT).show()
    }

    var cameraUtils:CameraUtils = CameraUtils("test",this)
    var count:Int = 0
    private lateinit var bluetooth: BlueToothUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var intent = Intent()
        intent.action = "re_service"
        intent.`package` = "cxzx.bdyx.com.retrofitutild"
        bindService(intent,conn,Context.BIND_AUTO_CREATE)
        tv.setOnClickListener(View.OnClickListener {
            messageManager!!.sendMessage(SocketConstant.SEND_MSG,""+count++)
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        bluetooth.onPermissionResult(requestCode,permissions,grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        cameraUtils.onActivityRequest(requestCode,resultCode,data)
        bluetooth.onActivityRequest(requestCode,resultCode,data)
    }
    override fun onDestroy() {
        messageManager!!.unregisterListener(listener)
        messageManager = null
        super.onDestroy()
    }

}
