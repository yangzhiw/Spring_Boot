package com.juzi;

import com.juzi.service.SimpleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yzw on 2016/8/21 0021.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class DubboTest {

    @Autowired
    SimpleService simpleService;

    @Test
    public void test() throws Exception {
        System.out.println(simpleService.add(3,4));
    }

}

