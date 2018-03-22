package cxzx.bdyx.com.retrofitutild.date_api.retrofit_utils.api_test;

import cxzx.bdyx.com.retrofitutild.InfoBean;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by QunCheung on 2018/3/13.
 * retrofit 接口书写位置
 * 建议,每个业务module中添加自己的api.底层用来测试
 */

public interface ApiTest {
    @GET("/login")
    Observable<Test<InfoBean>> baidu();
}
