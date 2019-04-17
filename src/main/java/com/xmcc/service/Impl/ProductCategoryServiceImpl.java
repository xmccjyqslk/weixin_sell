package com.xmcc.service.Impl;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.ProductCategoryDto;
import com.xmcc.entity.ProductCategory;
import com.xmcc.repository.ProductCategoryRepository;
import com.xmcc.service.ProductCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Resource
    private ProductCategoryRepository categoryRepository;


    @Override
    public ResultResponse<List<ProductCategoryDto>> findAll() {
        List<ProductCategory> productCategoryList = categoryRepository.findAll();
        //利用流转换为dto集合
        ResultResponse resultResponse = ResultResponse.success(productCategoryList.stream().map(productCategory ->
                ProductCategoryDto.toProductCategoryDto(productCategory)
        ).collect(Collectors.toList()));
        return resultResponse;
    }
}
