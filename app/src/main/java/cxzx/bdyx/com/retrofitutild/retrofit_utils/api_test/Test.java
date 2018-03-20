package cxzx.bdyx.com.retrofitutild.retrofit_utils.api_test;

/**
 * Created by QunCheung on 2018/3/13.
 * json串外层结构类
 * 可随意命名,注意:需要与SubscriberCallBack中保持一致
 */

public class Test<T>{
    /**
     * rspCode : -100
     * rspDesc : 系统错误,请刷新重试
     * info : {}
     * url : http://sspuat.life.taikang.com/customer/mark/saveMark
     * sign : A8802A18A12EBE4D1ABB6EAB41751979
     * signStr : {}
     */

    private String rspCode;
    private String rspDesc;
    private T info;

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }


    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspDesc() {
        return rspDesc;
    }

    public void setRspDesc(String rspDesc) {
        this.rspDesc = rspDesc;
    }
}
