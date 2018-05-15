package com.test.springcachewithredis.config;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisTemplate redisTemplate)
        throws IllegalAccessException {
        RedisCacheManager cm = new RedisCacheManager(redisTemplate);

        cm.setDefaultExpiration(CacheNames.GLOBAL_DEFAULT_EXPIRE); //秒
        cm.setUsePrefix(true);
        cm.setCachePrefix(new DefaultRedisCachePrefix());
        cm.setCacheNames(CacheNames.getAllCacheNames());
        cm.setExpires(CacheNames.getAllExpires());

        return cm;
    }


    /**
     * 定义缓存名称，一定配套添加过期时间，不然报错.
     *
     * 过期时间变量名=缓存变量+"_EXPIRE".
     */
    @Getter
    public static class CacheNames {

        private static final Long GLOBAL_DEFAULT_EXPIRE = 60L; //默认过期时间为1分钟。

        public static final String USERS = "users";
        public static final Long USERS_EXPIRE = 60L; //1min

        public static final String ROOMS = "rooms";
//        public static final Long ROOMS_EXPIRE = 60 * 60L;  //1h


        private static final Logger LOGGER = LoggerFactory.getLogger(CacheNames.class);

        /**
         * 获取缓存过期时间配置
         *
         * @return map K=cacheName, V=expire秒
         */
        static Map<String, Long> getAllExpires() throws IllegalAccessException {
            Field[] fields = CacheNames.class.getDeclaredFields();

            Map<String, String> stringFieldNameWithValue = new HashMap<>();
            Map<String, Long> longFieldNameWithValue = new HashMap<>();
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                if (fieldType == String.class) {
                    stringFieldNameWithValue.put(field.getName(), (String) field.get(null));
                    continue;
                }

                if (fieldType == Long.class) {
                    longFieldNameWithValue.put(field.getName(), (Long) field.get(null));
                }
            }

            Map<String, Long> cacheExpires = new HashMap<>();
            for (Entry<String, String> stringField : stringFieldNameWithValue.entrySet()) {
                String cacheFiled = stringField.getKey();
                String expireFiled = cacheFiled + "_EXPIRE";
                String cacheName = stringField.getValue();
                Long expire = longFieldNameWithValue.get(expireFiled);
                if (expire == null) {
                    LOGGER.error("没有找到匹配的缓存[{}]的过期时间[{}]，将使用默认过期时间[{}s].",
                        cacheFiled, expireFiled, GLOBAL_DEFAULT_EXPIRE);
                    expire = GLOBAL_DEFAULT_EXPIRE;
                }
                cacheExpires.put(cacheName, expire);
            }

            return cacheExpires;
        }

        static Set<String> getAllCacheNames() throws IllegalAccessException {
            return getAllExpires().keySet();
        }

    }

}
