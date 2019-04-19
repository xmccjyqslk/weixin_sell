package com.xmcc.service.Impl;

import com.xmcc.common.ResultEnums;
import com.xmcc.common.ResultResponse;
import com.xmcc.dao.Impl.BatchDaoImpl;
import com.xmcc.entity.OrderDetail;
import com.xmcc.repository.OrderDetailRepository;
import com.xmcc.service.OrderDetailService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl extends BatchDaoImpl<OrderDetail> implements OrderDetailService {


    @Resource
    private OrderDetailRepository orderDetailRepository;

    @Transactional
    @Override
    public void batchInsert(List<OrderDetail> orderDetailList) {

        super.batchInsert(orderDetailList);
    }

    @Override
    public ResultResponse findByOrderId(String orderId) {
        //获取 Example 对象以用来查询 订单详情对象OrderDetail
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(orderId);
        Example<OrderDetail> detailExample = Example.of(detail);
        Optional<OrderDetail> optionalOrderDetail = orderDetailRepository.findOne(detailExample);
        if ( ! optionalOrderDetail.isPresent()){
            return ResultResponse.fail(ResultEnums.FAIL.getMsg());
        }
        OrderDetail orderDetail = optionalOrderDetail.get();

        return ResultResponse.success(orderDetail);
    }
}
