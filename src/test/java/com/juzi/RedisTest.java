package com.juzi;

import com.juzi.domain.p.User;
import com.juzi.utils.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * Created by yzw on 2016/8/18 0018.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class RedisTest {

    @Inject
    private StringRedisTemplate stringRedisTemplate;

    @Inject
    private RedisTemplate<String,User> redisTemplate;

//    @Inject
//    private RedisService redisService;

    @Test
    public void test() throws Exception {
        // 保存字符串
          stringRedisTemplate.opsForValue().set("bbb", "111");
          User user = new User("yangzw", 20);
          redisTemplate.opsForValue().set(user.getName(), user);

         user = new User("蜘蛛侠", 40);
       //  redisTemplate.opsForValue().set(user.getName(), user);

         RedisService redisService = new RedisService();
         redisService.set(user.getName(), user);

//       System.out.println(redisTemplate.opsForValue().get("蜘蛛侠").getAge().longValue());
    }

}
