package com.xmcc.service.Impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.BestPayService;
import com.xmcc.common.Constant;
import com.xmcc.common.OrderEnum;
import com.xmcc.common.PayEnum;
import com.xmcc.common.ResultEnums;
import com.xmcc.entity.OrderMaster;
import com.xmcc.repository.OrderMasterRepository;

import com.xmcc.service.PayService;
import com.xmcc.utils.BigDecimalUtil;
import com.xmcc.utils.CustomException;
import com.xmcc.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Resource
    private OrderMasterRepository orderMasterRepository;

    @Resource
    private  BestPayService bestPayService;

    @Override
    public OrderMaster findByOrderId(String orderId) {
        Optional<OrderMaster> byId = orderMasterRepository.findById(orderId);
        if ( ! byId.isPresent()){
            throw new CustomException(OrderEnum.ORDER_ERROR.getMsg());
        }
        OrderMaster orderMaster = byId.get();

        return orderMaster;
    }

    @Override
    public PayResponse create(OrderMaster orderMaster) {
        PayRequest payRequest = new PayRequest();

        payRequest.setOpenid(orderMaster.getBuyerOpenid());
        payRequest.setOrderAmount(orderMaster.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderMaster.getOrderId());
        payRequest.setOrderName(Constant.ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        log.info("微信支付的请求:{}", JsonUtil.object2string(payRequest));

        PayResponse payResponse = bestPayService.pay(payRequest);

        log.info("微信支付的返回结果为:{}", JsonUtil.object2string(payResponse));

        return payResponse;
    }

    @Override
    public void WeixinNotify(String notifyData) {
        log.info("notifyData  ->  ", notifyData);
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        if (payResponse==null){

            throw new CustomException(ResultEnums.FAIL.getMsg());
        }
        OrderMaster orderMaster = findByOrderId(payResponse.getOrderId());
        //如果 orderMaster 不存在
        if (orderMaster==null){
            throw new CustomException(OrderEnum.ORDER_ERROR.getMsg());
        }
        //如果金额不一致
        if ( ! BigDecimalUtil.equals2(orderMaster.getOrderAmount(),
                new BigDecimal(String.valueOf(payResponse.getOrderAmount())))){
            throw new CustomException(OrderEnum.AMOUNT_CHECK_ERROR.getMsg());
        }
        //如果支付状态异常
        if (orderMaster.getPayStatus()!=PayEnum.WAIT.getCode()){
            throw new CustomException(PayEnum.STATUS_ERROR.getMsg());
        }
        //实际项目中 这儿还需要把交易流水号与订单的对应关系存入数据库，比较简单，这儿不做了,大家需要知道
        orderMaster.setPayStatus(PayEnum.FINSH.getCode());
        orderMaster.setOrderStatus(OrderEnum.FINSH.getCode());
        orderMasterRepository.save(orderMaster);
        log.info("微信支付异步回调,订单支付状态修改完成 ->  {}",orderMaster.toString() );
    }

    @Override
    public RefundResponse refund(OrderMaster orderMaster) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderAmount(orderMaster.getOrderAmount().doubleValue());
        refundRequest.setOrderId(orderMaster.getOrderId());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("微信退款请求:{}",refundRequest);
        //执行退款
        RefundResponse refund = bestPayService.refund(refundRequest);
        log.info("微信退款请求响应:{}",refund);
        return refund;
    }
}
