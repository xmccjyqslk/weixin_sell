package com.xmcc.controller;


import com.google.common.collect.Maps;
import com.xmcc.common.ResultEnums;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.param.DetailParam;
import com.xmcc.param.PageParam;
import com.xmcc.service.OrderMasterService;
import com.xmcc.utils.JsonUtil;
import com.xmcc.utils.Laji;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("buyer/order")
@Api(value = "订单相关接口",description = "完成订单的增删改查")
public class OrderMasterController {


    @Resource
    private OrderMasterService orderMasterService;


    @PostMapping("/create")
    @ApiOperation(value = "创建订单接口", httpMethod = "POST", response =ResultResponse.class)//json返回
    public ResultResponse create( @Valid @ApiParam(name ="订单对象",value ="json格式",required = true)
                                             OrderMasterDto orderMasterDto, BindingResult bindingResult){
        //参数检验
        HashMap<String, String> map = Maps.newHashMap();
        if (bindingResult.hasErrors()){
            List<String> collect = bindingResult.getFieldErrors().stream().
                    map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(collect));
            return ResultResponse.fail(map);
        }
        //参数正确返回
        return orderMasterService.insertOrder(orderMasterDto);
    }

    @GetMapping("/list")
    @ApiOperation(value = "创建订单列表接口", httpMethod = "GET", response =ResultResponse.class)//json返回
    public ResultResponse list( @ApiParam(name ="page对象",value ="json格式",required = true) PageParam pageParam,
    BindingResult bindingResult){
        //参数检验
        Map<String, String> map = Maps.newHashMap();
        if (bindingResult.hasErrors()){
            List<String> collect = bindingResult.getFieldErrors().stream().
                    map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(collect));
            return ResultResponse.fail(map);
        }
        //参数正确返回
        ResultResponse resultResponse=orderMasterService.getOrderMasterList(pageParam);

        return resultResponse;
    }

    @GetMapping("/detail")
    @ApiOperation(value = "创建查询订单详情接口", httpMethod = "GET", response =ResultResponse.class)//json返回
    public ResultResponse detail( @ApiParam(name ="openid和orderId的集合",value ="json格式",required = true) DetailParam param,
                                  BindingResult bindingResult){
        //参数检验
        HashMap<String, String> map = Maps.newHashMap();
        if (bindingResult.hasErrors()){
            List<String> collect = bindingResult.getFieldErrors().stream().
                    map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(map));
            return  ResultResponse.fail(map);
        }
        //参数正确 返回
        ResultResponse resultResponse=orderMasterService.getOrderDetail(param);

        return resultResponse;
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "创建取消订单接口", httpMethod = "POST", response =ResultResponse.class)//json返回
    public ResultResponse cancel( @ApiParam(name ="openid和orderId的集合",value ="json格式",required = true) DetailParam param,
                                  BindingResult bindingResult){
        //参数检验
        HashMap<String, String> map = Maps.newHashMap();
        if (bindingResult.hasErrors()){
            List<String> collect = bindingResult.getFieldErrors().stream().
                    map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(map));
            return  ResultResponse.fail(map);
        }
        ResultResponse resultResponse=orderMasterService.cancel(param);

        return resultResponse;
    }

}








//todo:实验------
class DemoPredicate {
    public static void main(String[] args) {
        List<String> one = new ArrayList<>(); one.add("迪丽热巴");
        one.add("宋远桥");
        one.add("苏星河");
        one.add("老子");
        one.add("庄子");
        one.add("孙子");
        one.add("洪七公");

        List<String> two = new ArrayList<>(); two.add("古力娜扎");
        two.add("张无忌");
        two.add("张三丰");
        two.add("赵丽颖");
        two.add("张二狗");
        two.add("张天爱");
        two.add("张三");

        List<String> onelist = one.stream().filter(s -> s.length() == 3).limit(3).collect(Collectors.toList());
        List<String> twolist = two.stream().filter(s -> s.startsWith("张")).skip(2).collect(Collectors.toList());
        List<Person> collect = Stream.concat(one.stream().filter(s -> s.length() == 3).limit(3),
                two.stream().filter(s -> s.startsWith("张")).skip(2)).
                map(Person::new).collect(Collectors.toList());
        System.out.println(collect);

    }

    public static void get(){

    }
}

class Person {
    private String name; public Person() {}
    public Person(String name) { this.name = name;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "'}";
    }

    public String getName() { return name;
    }

    public void setName(String name) { this.name = name;
    }
}