## common-localcached是什么

common-localcached是一个开源的基于Java开发的本地缓存组件。
它是一个maven项目，可以方便的引入到项目中，使用及其简单。在你的项目中引入下面的Maven依赖：
```
<dependency>
     <groupId>org.github.cloudgyb</groupId>
     <artifactId>common-localcached</artifactId>
     <version>${version}</version>
</dependency>
```
#### 缓存数据结构
common-localcached缓存系统，将缓存数据划分为不同的LocalCachedBlock（缓存数据块），每一个LocalCahcedBlock可以存储多条LocalCachedItem（缓存数据项），每一个缓存数据项包含两个字段分别为expireTime（缓存过期时间）和value（数据域）。
缓存数据结构图：
![data struct](https://github.com/cloudgyb/common-localcached/blob/master/data%20struct.png)

#### LocalCachedUtil类
LocalCachedUtil是一个工具类，整个缓存系统的入口类，它封装了对缓存数据存取的所有操作，例如get和put方法。所以你无需了解其他类就可以轻松使用。
具体实现见源码。此类中包含一个私有线程类，负责定时轮询清理过期的缓存，当这个类第一次被加载的时候，清理线程类开始工作。


