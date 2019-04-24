package com.xmcc.service;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.xmcc.entity.OrderMaster;

public interface PayService {
    OrderMaster findByOrderId(String orderId);

    PayResponse create(OrderMaster orderMaster);

    void WeixinNotify(String notifyData);

    RefundResponse refund(OrderMaster orderMaster);
}
