package cxzx.bdyx.com.retrofitutild.date_api.retrofit_utils.retrofit_instance

import cxzx.bdyx.com.retrofitutild.date_api.retrofit_utils.https_utils.SSLUtils
import cxzx.bdyx.com.retrofitutild.date_api.retrofit_utils.https_utils.Tls12SocketFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext

/**
 * Created by QunCheung on 2018/3/13.
 * 多服务器地址Retrofit请求工具类.
 * 1.自定义位置,书写api
 * 2.生成实体类infoBean
 * 3.创建SubscriberCallBack<infoBean>实例对象obs
 * 4.RetrofitInstance.request(RetrofitInstance
        .createApiService("http://192.168.43.40:8080",
        ApiTest::class.java).baidu(),obs)
 * 5.其中obs,可使用obs.unsubscribe()取消请求回调的订阅,防止Activity,finish后崩溃
 */
object RetrofitInstance{
    private lateinit var instance : RetrofitInstance
    private var retrofits: MutableMap<String, Retrofit> = hashMapOf()

    private operator fun invoke(): RetrofitInstance {
        return instance
    }

    private val INTERNET_REQUEST_TIME: Long = 120000

    /**
     * 获取不同BaseUrl地址下的Retrofit实例
     */
    private fun initRetrofit(baseUrl:String):Retrofit{
        if (retrofits.containsKey(baseUrl)){
            return retrofits.getValue(baseUrl)
        }else{
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            //配置SSL
            var sslContext: SSLContext? = null
            try {
                sslContext = SSLContext.getInstance("TLS")
                try {
                    sslContext!!.init(null, null, null)
                } catch (e: KeyManagementException) {
                    e.printStackTrace()
                }

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            val socketFactory = Tls12SocketFactory(sslContext!!.socketFactory)
            val okHttpClient = OkHttpClient.Builder()
                    .readTimeout(INTERNET_REQUEST_TIME, TimeUnit.MILLISECONDS)
                    .connectTimeout(INTERNET_REQUEST_TIME, TimeUnit.MILLISECONDS)
                    .addInterceptor(logging)
                    .sslSocketFactory(socketFactory, SSLUtils.UnSafeTrustManager())
                    .build()
            //此处可添加log拦截器等自定义功能
            var retrofit:Retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .baseUrl(baseUrl)
                    .build()
            retrofits.put(baseUrl,retrofit)
            return retrofit
        }
    }

    /**
     * 创建服务
     */
    fun <T>createApiService(baseUrl: String,apiService:Class<T> ):T{
        return initRetrofit(baseUrl).create(apiService)
    }

    fun <T> request(observable: Observable<T>, subscriber: Subscriber<T>) {
        observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
    }

}
