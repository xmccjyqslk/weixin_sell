package com.xmcc.controller;


import com.xmcc.common.ResultResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("weixin")
@Slf4j
public class WeiXinController {

    @RequestMapping("/getCode")
    public void getCode(@RequestParam("code") String code){

        log.info("成功回调，获得授权码：-> {}",code );

        String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxcec0b9e65c084712&" +
                "secret=05a7e861c1985ced86af77fb8f7163bc&code="+code+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String token = restTemplate.getForObject(url, String.class);
        log.info("根据code获取令牌 -> {}",token );

    }


    @RequestMapping("authorize")
    @ApiOperation(value = "创建授权接口", httpMethod = "POST", response =ResultResponse.class)//json返回
    public ResultResponse authorize(){
        return null;
    }

}
