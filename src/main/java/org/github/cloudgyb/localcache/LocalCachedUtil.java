package org.github.cloudgyb.localcache;

import com.alibaba.fastjson.JSON;

import java.util.Iterator;
import java.util.Map;

/**
 * @Author: 耿远博
 * @Date: 2018/9/20
 */
public class LocalCachedUtil {
    /**
     * 构造函数私有化，禁止外部创建对象
     */
    private LocalCachedUtil(){}

    static{
        //启动清理过期缓存的线程
        new CacheTimer(LocalCached.getLocalCached()).start();
    }
    /**
     * 将数据放入对应的缓存块中
     * 当cachedTime <=0时，代表数据永不过期
     * @param blockName  缓存块名称
     * @param key        缓存数据key
     * @param value      缓存数据
     * @param cachedTime 有效时常 单位：ms
     */
    public static void put(String blockName, Object key, Object value, long cachedTime) {
        LocalCachedBlock localCachedBlock = LocalCached.getLocalCachedBlock(blockName);
        if (localCachedBlock == null) {
            localCachedBlock = new LocalCachedBlock();
            LocalCached.put(blockName, localCachedBlock);
        }
        long expireTime = 0;
        if (cachedTime > 0) {
            expireTime = System.currentTimeMillis() + cachedTime;
        }
        localCachedBlock.put(key, new LocalCachedItem(expireTime, value));
    }

    /**
     * 将数据放入对应的缓存块中，并且数据永不过期
     * @param blockName
     * @param key
     * @param value
     */
    public static void put(String blockName, Object key, Object value) {
        put(blockName, key, value, 0);
    }

    /**
     * 将数据放入默认的缓存块中
     * @param key
     * @param value
     * @param cachedTime
     */
    public static void put(Object key, Object value,long cachedTime) {
        put(LocalCached.DEFAULT_CACHED_BLOCKNAME, key, value, cachedTime);
    }

    /**
     * 将数据放入默认的缓存块中，并永不过期
     * @param key
     * @param value
     */
    public static void put(Object key, Object value) {
        put(key, value, 0);
    }

    public static Object get(String blockName,Object key){
        LocalCachedBlock localCachedBlock = LocalCached.getLocalCachedBlock(blockName);
        if(localCachedBlock == null)
            return null;
        LocalCachedItem localCachedItem = localCachedBlock.get(key);
        if(localCachedItem == null)
            return null;
        long expireTime = localCachedItem.getExpireTime();
        //如果数据过期了，可能是缓存定时任务没来得及清楚，实际上已经过期了
        if(expireTime!=0 && expireTime < System.currentTimeMillis())
            return null;
        return localCachedItem.getValue();

    }

    public static <T> T get(String blockName,Object key,Class<T> clazz){
        Object o = get(blockName,key);
        if(o == null)
            return null;
        return JSON.parseObject(o.toString(),clazz);
    }

    public static Object get(Object key){
        return get(LocalCached.DEFAULT_CACHED_BLOCKNAME,key);
    }

    public static void remove(String blockName, Object key){
        LocalCachedBlock localCachedBlock = LocalCached.getLocalCachedBlock(blockName);
        if(localCachedBlock != null){
            localCachedBlock.remove(key); //从数据块中删除缓存
        }
    }
    public static void remove(Object key){
        remove(LocalCached.DEFAULT_CACHED_BLOCKNAME,key);
    }

    public static void clearAllCache(){
        LocalCached.clearCache();
    }

    /**
     * 私有静态内部类，用于定时轮询清除过期的缓存
     */
    private static class CacheTimer extends Thread {
        private LocalCached localCached;
        public CacheTimer(LocalCached localCached){
            this.localCached = localCached;
        }
        @Override
        public void run() {
            System.out.println("定时轮询删除过期的缓存开始......");
            while(true) {
                Map<String,LocalCachedBlock> blockMap = localCached.getCacheMap();
                Iterator<String> blockMapIter = blockMap.keySet().iterator();
                while (blockMapIter.hasNext()) {
                    String blockKey = blockMapIter.next();
                    LocalCachedBlock lcb = blockMap.get(blockKey);
                    Map<Object, LocalCachedItem> cacheMap = lcb.getCacheMap();
                    Iterator<Object> cacheKeyIter = cacheMap.keySet().iterator();
                    while (cacheKeyIter.hasNext()) {
                        Object cacheKey = cacheKeyIter.next();
                        LocalCachedItem localCachedItem = cacheMap.get(cacheKey);
                        long expireTime = localCachedItem.getExpireTime();
                        if (expireTime < System.currentTimeMillis()) { //如果已经过时
                            cacheMap.remove(cacheKey);
                        }
                    }
                    if(cacheMap.isEmpty()){ //如果此缓存块没有数据了，删除这个块
                        blockMap.remove(blockKey);
                    }
                }
                try {
                    Thread.sleep(10 * 1000); //10s一次轮询
                }catch (Exception e){
                    continue;
                }
                System.out.println("1次轮询结束。。。");
            }
        }
    }


}
