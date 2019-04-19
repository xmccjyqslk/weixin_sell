package com.xmcc;

import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductCategory;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.repository.ProductCategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeixinSellApplicationTests {

    @Resource
    private ProductCategoryRepository productCategoryRepository;

    @Resource
    private OrderMasterRepository orderMasterRepository;


    @Test
    public void contextLoads() {

//        List<ProductCategory> all = productCategoryRepository.findAll();
        PageRequest pageRequest = PageRequest.of(0, 5);
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setBuyerOpenid("oXDaO1RMGiRJACn5Bsp0nkHEqQ_w");
        Example<OrderMaster> example = Example.of(orderMaster);
//        Page<OrderMaster> allByBuyerOpenid = orderMasterRepository.findAllByBuyerOpenid("oXDaO1RMGiRJACn5Bsp0nkHEqQ_w",pageRequest );
        Page<OrderMaster> allByBuyerOpenid = orderMasterRepository.findAll(example, pageRequest);
        System.out.println("----------");
        System.out.println(allByBuyerOpenid.getTotalElements());
        System.out.println(allByBuyerOpenid.getTotalPages());
        System.out.println("------------");
//        ProductCategory productCategory = productCategoryRepository.queryByIdandType(2, 2);
//        System.out.println(productCategory.toString());


    }

}
