package cxzx.bdyx.com.retrofitutild.date_base.sqlite.bean;

/**
 * Created by houyongliang on 2018/3/26 15:58.
 * Function(功能):
 */

public class JsonBean {
    private int _id;//自增长主键
    private int id;//int 类型标识
    private String tag;//String 类型标识
    private String json;//需要存取的 json 串
    private long date;//时间  可以为 存取数据的当前时间
    private String url;//可以存放 url 等基本数据
    private String userId;//客户的标识 级别等的区分 备用

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "JsonBean{" +
                "_id=" + _id +
                ", id=" + id +
                ", tag='" + tag + '\'' +
                ", json='" + json + '\'' +
                ", date=" + date +
                ", url='" + url + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
