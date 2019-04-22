package com.xmcc.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class WechatConfig {

    @Resource
    private WeixinProperties weixinProperties;

    @Bean
    public WxMpService wxMpService(){
        WxMpService wxMpService = new WxMpServiceImpl();
        //设置微信配置的存储
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        //设置appid  这个在项目中肯定是通过配置来实现
        wxMpConfigStorage.setAppId(weixinProperties.getAppid());
        //设置密码
        wxMpConfigStorage.setSecret(weixinProperties.getSecret());
        return wxMpConfigStorage;
    }
}