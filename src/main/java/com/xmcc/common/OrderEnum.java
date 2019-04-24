package com.xmcc.common;

import lombok.Getter;

@Getter
public enum  OrderEnum {
    NEW(0,"新建订单"),
    FINSH(1,"已完成订单"),
    CANCEL(2,"已取消"),
    ORDER_ERROR(3,"订单异常"),
    AMOUNT_CHECK_ERROR(4,"订单金额不一致");
    private int code;
    private String msg;

    OrderEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
}