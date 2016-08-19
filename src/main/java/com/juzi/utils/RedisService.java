package com.juzi.utils;

import com.wandoulabs.jodis.JedisResourcePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class RedisService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private JedisResourcePool jedisResourcePool;

    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    private JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();

    public Object get(String key) {
        try (Jedis jedis = jedisResourcePool.getResource()) {
            return jdkSerializationRedisSerializer
                    .deserialize(jedis.get(stringRedisSerializer.serialize(key)));
        } catch (Exception e) {
            logger.error("get {} error ! Exception Message : {}", key, e.getMessage());
            return null;
        }
    }

    public String getString(String key) {
        try (Jedis jedis = jedisResourcePool.getResource()) {
            return stringRedisSerializer
                    .deserialize(jedis.get(stringRedisSerializer.serialize(key)));
        } catch (Exception e) {
            logger.error("getString {} error ! Exception Message : {}", key, e.getMessage());
            return null;
        }
    }

    public void set(String key, Object value) {
        try (Jedis jedis = jedisResourcePool.getResource()) {
            jedis.set(stringRedisSerializer.serialize(key),
                    jdkSerializationRedisSerializer.serialize(value));
        } catch (Exception e) {
            logger.error("set {} with {} error ! Exception Message : {}", key, value, e.getMessage());
        }
    }

    public void sadd(String key, Object... objects) {
        try (Jedis jedis = jedisResourcePool.getResource()) {
            for (int i = 0; i < objects.length; i++)
                jedis.sadd(stringRedisSerializer.serialize(key),
                        jdkSerializationRedisSerializer.serialize(objects[i]));
        } catch (Exception e) {
            logger.error("sadd {} error ! Exception Message : {}", key, e.getMessage());
        }
    }

    public void saddForString(String key, String... values) {
        try (Jedis jedis = jedisResourcePool.getResource()) {
            for (int i = 0; i < values.length; i++)
                jedis.sadd(stringRedisSerializer.serialize(key),
                        stringRedisSerializer.serialize(values[i]));
        } catch (Exception e) {
            logger.error("sadd {} error ! Exception Message : {}", key, e.getMessage());
        }
    }

    public Set<String> smembersForString(String key) {
        try (Jedis jedis = jedisResourcePool.getResource()
        ) {
            Set<byte[]> set1 = jedis.smembers(stringRedisSerializer.serialize(key));
            Set<String> set2 = new HashSet<>();

            for (byte[] bytes : set1)
                set2.add(stringRedisSerializer.deserialize(bytes));

            return set2;
        } catch (Exception e) {
            logger.error("smembers {} error ! Exception Message : {}", key, e.getMessage());
            return null;
        }
    }

    public void delete(String key) {
        try (Jedis jedis = jedisResourcePool.getResource()) {
            jedis.del(stringRedisSerializer.serialize(key));
        } catch (Exception e) {
            logger.error("delete {} error ! Exception Message : {}", key, e.getMessage());
        }
    }

    public void expire(String key, long timeout) {
        try (Jedis jedis = jedisResourcePool.getResource()) {
            jedis.expire(stringRedisSerializer.serialize(key),
                    Integer.valueOf(String.valueOf(timeout / 1000)));
        } catch (Exception e) {
            logger.error("expire {} error ! Exception Message : {}", key, e.getMessage());
        }
    }
}
