package com.xmcc.service.Impl;

import com.xmcc.common.ResultEnums;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.ProductCategoryDto;
import com.xmcc.dto.ProductInfoDto;
import com.xmcc.entity.ProductInfo;
import com.xmcc.repository.ProductInfoRepository;
import com.xmcc.service.ProductCategoryService;
import com.xmcc.service.ProductInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Resource
    private ProductCategoryService productCategoryService;
    @Resource
    private ProductInfoRepository productInfoRepository;


    @Override
    public ResultResponse queryList() {

        ResultResponse<List<ProductCategoryDto>> categoryServiceResult = productCategoryService.findAll();
        List<ProductCategoryDto> categorydtoList = categoryServiceResult.getData();
        if(CollectionUtils.isEmpty(categorydtoList)){
            return categoryServiceResult;//如果分类列表为空 直接返回了
        }
        //获得类目编号集合
        List<Integer> categoryTypeList = categorydtoList.stream().map(categorydto -> categorydto.getCategoryType()).collect(Collectors.toList());
        //查询商品列表  这里商品上下架可以用枚举 方便扩展
        List<ProductInfo> productInfoList = productInfoRepository.findByProductStatusAndCategoryTypeIn(ResultEnums.PRODUCT_UP.getCode(), categoryTypeList);
        //多线程遍历 取出每个商品类目编号对应的 商品列表 设置进入类目中
        List<ProductCategoryDto> finalResultList = categorydtoList.parallelStream().map(categorydto -> {
            categorydto.setProductInfoDtoList(productInfoList.stream().
                    filter(productInfo -> productInfo.getCategoryType() == categorydto.getCategoryType()).map(productInfo ->
                    ProductInfoDto.toProductInfoDto(productInfo)).collect(Collectors.toList()));
            return categorydto;
        }).collect(Collectors.toList());
        return ResultResponse.success(finalResultList);
    }

    @Override
    public ResultResponse<ProductInfo> queryById(String productId) {
        if (StringUtils.isBlank(productId)){
            return ResultResponse.fail(ResultEnums.PARAM_ERROR.getMsg());
        }
        Optional<ProductInfo> byId = productInfoRepository.findById(productId);
        if ( ! byId.isPresent()){
            return ResultResponse.fail(ResultEnums.NOT_EXITS.getMsg());
        }
        ProductInfo productInfo = byId.get();
        if (productInfo.getProductStatus()==ResultEnums.PRODUCT_DOWN.getCode()){
            return ResultResponse.fail(ResultEnums.PRODUCT_DOWN.getMsg());
        }
        return ResultResponse.success(productInfo);
    }

    @Override
    public void updateProduct(ProductInfo productInfo) {

        productInfoRepository.save(productInfo);

    }
}
