package cxzx.bdyx.com.retrofitutild.retrofit_utils.call_back;

import cxzx.bdyx.com.retrofitutild.retrofit_utils.api_test.Test;
import rx.Subscriber;

/**
 * Created by QunCheung on 2018/3/14.
 * retrofit网络请求返回结果处理
 */

/**
 * 自判定失败结果,无统一处理回调接口
 * @param <T>
 */
public abstract class SubscriberCallBackNodispetch<T> extends Subscriber<Test<T>>{
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        onFailed("-500",e.toString());
    }

    @Override
    public void onNext(Test<T> tTest) {
        onSuccess(tTest);
        if (!tTest.getRspCode().equals("0")){
            onFailed(tTest.getRspCode(),tTest.getRspDesc());
        }
    }

    abstract void onSuccess(Test<T> tTest);
    abstract void onFailed(String rspCode, String rspDesc);
}
