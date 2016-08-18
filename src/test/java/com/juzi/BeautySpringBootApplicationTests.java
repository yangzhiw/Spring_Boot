package com.juzi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles({"dev"})
public class BeautySpringBootApplicationTests {

	@Value("${aaa.bbb}")
	private String  ccc;

    @Before
	public  void  before(){
		System.out.println("==========before===========");
	}

	@Test
	public void app2() {
      System.out.println(ccc);
	}

	@Test
	public void app1() {
		System.out.println("dddd");
	}

	@After
	public  void after() {
		System.out.println("==========after==============");
	}

}
