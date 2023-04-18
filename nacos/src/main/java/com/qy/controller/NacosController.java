package com.qy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nacos")
@RefreshScope   // 动态刷新
public class NacosController {

    @Value("${qy.name}")
    private String name;

    @Value("${qy.password}")
    private String password;

    @GetMapping
    public String get() {
        return name + password;
    }
}
