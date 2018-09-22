package org.github.cloudgyb.localcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存快
 * @Author: 耿远博
 * @Date: 2018/9/20
 */
public class LocalCachedBlock {
    /**
     * 存放缓存数据
     */
    private Map<Object,LocalCachedItem> cacheMap;

    public LocalCachedBlock() {
        this.cacheMap = new ConcurrentHashMap<>();
    }

    public void put(Object key, LocalCachedItem value){
        cacheMap.put(key,value);
    }

    public LocalCachedItem get(Object key){
        return cacheMap.get(key);
    }

    public void remove(Object key){
        cacheMap.remove(key);
    }

    public void clear(){
        cacheMap.clear();
    }

    public Map<Object, LocalCachedItem> getCacheMap() {
        return cacheMap;
    }
}
