package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    void batchInsert(List<OrderDetail> orderDetailList);

    ResultResponse findByOrderId(String orderId);

}
