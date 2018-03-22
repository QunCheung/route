package cxzx.bdyx.com.retrofitutild.date_api.retrofit_utils.call_back;

import android.util.Log;

import cxzx.bdyx.com.retrofitutild.date_api.retrofit_utils.api_test.Test;
import rx.Subscriber;

/**
 * Created by QunCheung on 2018/3/14.
 */

/**
 * 重写的网络请求回调接口.统一返回值json外层.可以统一处理返回码等信息
 * @param <T> info内容的实体类
 */
public abstract class SubscriberCallBack<T> extends Subscriber<Test<T>>{
    @Override
    public void onNext(Test<T> tTest) {
        //可根据code自行除了返回结果.过滤失败
        if (tTest.getRspCode().equals("-100")) {
            onSuccess(tTest.getInfo());
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.i("http", "onError: "+e.toString());
    }


    public abstract void onSuccess(T info);
}
