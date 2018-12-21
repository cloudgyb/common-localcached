package org.github.cloudgyb.localcache;

import com.alibaba.fastjson.JSON;
import org.github.cloudgyb.localcache.entity.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for LocalCached.
 */
public class LocalCachedUtilTest {

    @BeforeClass
    public static void beforeTest(){
        LocalCachedUtil.startCache();
        //判断数据是否按照预期的被清除
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Object bbb = LocalCachedUtil.get("bbb");
                    System.out.println(bbb);
                    try {
                        Thread.sleep(1 * 1000);
                    }catch (Exception e){}
                }
            }
        }).start();
    }
    @Test
    public void putTest() {
        long l = Runtime.getRuntime().freeMemory() / 1024;
        System.out.println("初始内存:"+l+"kb");

        LocalCachedUtil.put("block1","aaa","aaa test value",1*60*1000);
        LocalCachedUtil.put("bbb","bbb test value",2*60*1000);
        LocalCachedUtil.put("ccc","bb" + "ccc test value");
        LocalCachedUtil.put("ddd","ddd test value");
        for (int i=0;i<100000;i++){
            LocalCachedUtil.put("ddd"+i,"ddd"+i+" test value");
        }
        long l1 = Runtime.getRuntime().freeMemory() / 1024;
        System.out.println("使用后内存："+l1+"kb");
        System.out.println("消耗量："+(l-l1)/1024.0+"Mb");
    }


    @Test
    public void test3(){
        User user = new User();
        user.setId(1);
        user.setName("geng");
        String s = JSON.toJSONString(user);
        LocalCachedUtil.put("user",s);
    }
    /*@Test*/
    public void test4(){
        User user = LocalCachedUtil.get(LocalCached.DEFAULT_CACHED_BLOCKNAME,"user", User.class);
        System.out.println(user);
        try {
            Thread.currentThread().join();
        }catch (Exception e){

        }

    }
    @AfterClass
    public static void afterTest(){
        LocalCachedUtil.stopCache();
    }
}
