package com.juzi.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yzw on 2016/8/17 0017.
 */
@RestController
public class HelloResource {
    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }

}
