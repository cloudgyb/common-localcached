package org.github.cloudgyb.localcache;

/**
 * 缓存数据项
 * @Author: 耿远博
 * @Date: 2018/9/20
 */
public class LocalCachedItem {
    /**
     * 过期时间
     */
    private long expireTime;
    /**
     * 缓存数据
     */
    private Object value;

    public LocalCachedItem() {
    }

    public LocalCachedItem(long expireTime, Object value) {
        this.expireTime = expireTime;
        this.value = value;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
