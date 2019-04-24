package com.xmcc.controller;

import com.google.common.collect.Maps;
import com.lly835.bestpay.model.PayResponse;
import com.xmcc.entity.OrderMaster;
import com.xmcc.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("pay")
@Slf4j
public class PayController {

    @Resource
    private PayService payService;

    @RequestMapping("create")
    public ModelAndView create(@RequestParam("orderId")String orderId,
                               @RequestParam("returnUrl")String returnUrl){

        OrderMaster orderMaster=payService.findByOrderId(orderId);

        PayResponse payResponse = payService.create(orderMaster);
        Map<String, Object> map = Maps.newHashMap();
        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);
//        log.info(, );

        return new ModelAndView("weixin/pay",map);
    }


    @RequestMapping("notify")
    public void weixin_notify(String notifyData){
        log.info("回调成功 ,正在检验参数  ->  {}", 22222222);
        payService.WeixinNotify(notifyData);


    }


    @RequestMapping("test")
    public void test(){

        log.info("回调成功 ->  {}", 1111111111);
    }

}
