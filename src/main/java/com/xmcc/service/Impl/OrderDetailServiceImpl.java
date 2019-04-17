package com.xmcc.service.Impl;

import com.xmcc.dao.Impl.BatchDaoImpl;
import com.xmcc.entity.OrderDetail;
import com.xmcc.service.OrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class OrderDetailServiceImpl extends BatchDaoImpl<OrderDetail> implements OrderDetailService {

    @Transactional
    @Override
    public void batchInsert(List<OrderDetail> orderDetailList) {

        super.batchInsert(orderDetailList);
    }
}
