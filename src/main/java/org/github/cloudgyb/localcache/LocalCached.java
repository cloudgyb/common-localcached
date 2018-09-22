package org.github.cloudgyb.localcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存，为了节省资源，这个类设计成单利模式
 *
 * @Author: 耿远博
 * @Date: 2018/9/20
 */
public class LocalCached {
    /**
     * 缓存块容器，key为缓存块名称，value为LocalCachedBlock对象
     * @see LocalCachedBlock
     */
    private Map<String,LocalCachedBlock> cacheMap = new ConcurrentHashMap<>();
    private static LocalCached localCached;
    /**
     * 默认缓存块常量名称
     */
    public static final String DEFAULT_CACHED_BLOCKNAME = "default";

    private LocalCached(){
    }

    public static LocalCached getLocalCached(){
        if(localCached==null){
            synchronized (LocalCached.class){
                if(localCached==null){
                    localCached = new LocalCached();
                }
            }
        }
        return localCached;
    }

    /**
     * 获取缓存块对象,如果对应的缓存块不存在，则返回null
     * @param blockName 缓存块名称
     * @return LocalCachedBlock
     */
    public static LocalCachedBlock getLocalCachedBlock(String blockName){
        return getLocalCached().cacheMap.get(blockName);
    }

    public static void put(String blockName,LocalCachedBlock localCachedBlock){
        getLocalCached().cacheMap.put(blockName,localCachedBlock);
    }

    public static void clearCache(){
        getLocalCached().cacheMap.clear();
    }

    public Map<String, LocalCachedBlock> getCacheMap() {
        return cacheMap;
    }
}
