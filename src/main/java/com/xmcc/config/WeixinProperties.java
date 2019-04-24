package com.xmcc.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "wechat")
@Component
public class WeixinProperties {

    private String appid;

    private String secret;

    private String mchId;

    private String mchKey;

    private String keyPath;

    private String notifyUrl;



}
