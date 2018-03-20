package cxzx.bdyx.com.retrofitutild

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cxzx.bdyx.com.retrofitutild.permission_utils.PermissionInterface
import cxzx.bdyx.com.retrofitutild.retrofit_utils.api_test.ApiTest
import cxzx.bdyx.com.retrofitutild.retrofit_utils.call_back.SubscriberCallBack
import cxzx.bdyx.com.retrofitutild.retrofit_utils.retrofit_instance.RetrofitInstance
import cxzx.bdyx.com.retrofitutild.tools_utils.bluetooth.BlueToothUtils
import cxzx.bdyx.com.retrofitutild.tools_utils.camera.CameraUtils
import kotlinx.android.synthetic.main.activity_main.*

class BaseActivity : AppCompatActivity(), PermissionInterface {
    override fun onHadPermission() {
        Toast.makeText(this,"isHad",Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionFailed() {
        Toast.makeText(this,"isFailed",Toast.LENGTH_SHORT).show()
    }

    internal var obs: SubscriberCallBack<InfoBean> =
            object : SubscriberCallBack<InfoBean>() {
        override fun onSuccess(info: InfoBean?) {
            tv.setText(info!!.sign)
        }
    }
    var cameraUtils:CameraUtils = CameraUtils("test",this)
    var context = this;

    private lateinit var bluetooth: BlueToothUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RetrofitInstance.request(RetrofitInstance
                .createApiService("http://192.168.43.40:8080",
                        ApiTest::class.java)
                .baidu(),
                obs)
        bluetooth = BlueToothUtils(this)
        bluetooth.getBlueToothInfo()
        bluetooth.ScanDevice()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        bluetooth.onPermissionResult(requestCode,permissions,grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        cameraUtils.onActivityRequest(requestCode,resultCode,data)
        bluetooth.onActivityRequest(requestCode,resultCode,data)
    }
    override fun onDestroy() {
        super.onDestroy()
        obs.unsubscribe()
    }
}
