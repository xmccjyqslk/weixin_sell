package com.xmcc;

import com.xmcc.entity.ProductCategory;
import com.xmcc.repository.ProductCategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeixinSellApplicationTests {

    @Resource
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void contextLoads() {

//        List<ProductCategory> all = productCategoryRepository.findAll();
        System.out.println();
//        ProductCategory productCategory = productCategoryRepository.queryByIdandType(2, 2);
//        System.out.println(productCategory.toString());


    }

}
