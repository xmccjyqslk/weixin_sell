package com.xmcc.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class PayConfig {

    @Resource
    private WeixinProperties weixinProperties;

    @Bean
    public BestPayService PayService(){

        WxPayH5Config wxPayH5Config = new WxPayH5Config();

        wxPayH5Config.setAppId(weixinProperties.getAppid());
        wxPayH5Config.setAppSecret(weixinProperties.getSecret());
        wxPayH5Config.setMchId(weixinProperties.getMchId());
        wxPayH5Config.setKeyPath(weixinProperties.getKeyPath());
        wxPayH5Config.setMchKey(weixinProperties.getMchKey());
        wxPayH5Config.setNotifyUrl(weixinProperties.getNotifyUrl());

        BestPayServiceImpl bestPayService = new BestPayServiceImpl();

        bestPayService.setWxPayH5Config(wxPayH5Config);
        return bestPayService;
    }

}
