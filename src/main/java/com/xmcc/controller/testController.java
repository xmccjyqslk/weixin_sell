package com.xmcc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class testController {

    @RequestMapping("/hello")
    public String ff(){
        log.info("info -> {}","欢迎来到邮箱");
        return "hello. booyt";
    }

    @RequestMapping("/hello1")
    public String gg(){
        log.info("info -> {}","欢迎来到邮箱");
        return "hello. booyt";
    }
}
