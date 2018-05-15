package com.test.springcachewithredis.service;

import com.test.springcachewithredis.config.CacheConfig.CacheNames;
import com.test.springcachewithredis.entity.Room;
import com.test.springcachewithredis.entity.User;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public HelloService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Interface	Description Key Type Operations
     *
     * GeoOperations
     *
     * Redis geospatial operations, such as GEOADD, GEORADIUS,…​
     *
     * HashOperations
     *
     * Redis hash operations
     *
     * HyperLogLogOperations
     *
     * Redis HyperLogLog operations, such as PFADD, PFCOUNT,…​
     *
     * ListOperations
     *
     * Redis list operations
     *
     * SetOperations
     *
     * Redis set operations
     *
     * ValueOperations
     *
     * Redis string (or value) operations
     *
     * ZSetOperations
     *
     * Redis zset (or sorted set) operations
     *
     * Key Bound Operations
     *
     * BoundGeoOperations
     *
     * Redis key bound geospatial operations
     *
     * BoundHashOperations
     *
     * Redis hash key bound operations
     *
     * BoundKeyOperations
     *
     * Redis key bound operations
     *
     * BoundListOperations
     *
     * Redis list key bound operations
     *
     * BoundSetOperations
     *
     * Redis set key bound operations
     *
     * BoundValueOperations
     *
     * Redis string (or value) key bound operations
     *
     * BoundZSetOperations
     *
     * Redis zset (or sorted set) key bound operations
     */
    public User sayHi(String id) {
        Object v = redisTemplate.opsForValue().get(id);
        if (v != null) {
            return (User) v;
        }

        final Random random = new Random();
        final User user = new User();
        user.setId(id);
        user.setValue(random.nextInt());
        redisTemplate.opsForValue().set(id, user);

        return user;
    }

    @Cacheable(value = CacheNames.USERS)
    public User sayHello(String id) {
        final Random random = new Random();
        final User user = new User();
        user.setId(id);
        user.setValue(random.nextInt());
        return user;
    }

    @Cacheable(value = CacheNames.ROOMS)
    public Room getRoom(String id) {
        final Random random = new Random();
        final Room room = new Room();
        room.setId(id);
        room.setValue(random.nextInt());
        return room;
    }
}
