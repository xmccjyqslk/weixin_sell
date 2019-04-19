package com.xmcc.repository;

import com.xmcc.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {


    OrderMaster findByOrderIdAndBuyerOpenid(String orderId,String openid);
//    Page<OrderMaster> findAllByBuyerOpenid(String openid,PageRequest pageRequest);

}
