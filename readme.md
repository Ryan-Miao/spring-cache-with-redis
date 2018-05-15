Springboot Cache集成redis
======



## 1. How to run
```
git clone xxx
gradlew clean build
```

修改配置文件中redis配置；   
运行main方法。


## 2. 关于使用

首先在com.test.springcachewithredis.config.CacheConfig.CacheNames中定义
要使用缓存名称以及过期时间。

然后在需要缓存的service上添加
```java
@Cacheable(value = CacheNames.USERS)
```

