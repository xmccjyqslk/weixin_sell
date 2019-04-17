package com.xmcc.repository;


import com.xmcc.entity.ProductCategory;
import com.xmcc.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {

//    @Query(value = "select * from product_category where category_id=?1 and category_type=?2",nativeQuery = true)
//    ProductCategory queryByIdandType(Integer id,Integer type);

}
