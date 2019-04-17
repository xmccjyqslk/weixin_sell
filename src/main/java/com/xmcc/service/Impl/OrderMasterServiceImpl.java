package com.xmcc.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.common.OrderEnum;
import com.xmcc.common.PayEnum;
import com.xmcc.common.ResultEnums;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderDetailDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.repository.ProductInfoRepository;
import com.xmcc.service.OrderDetailService;
import com.xmcc.service.OrderMasterService;
import com.xmcc.service.ProductInfoService;
import com.xmcc.utils.BigDecimalUtil;
import com.xmcc.utils.CustomException;
import com.xmcc.utils.IDUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {

    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private OrderMasterRepository orderMasterRepository;

    @Resource
    private OrderDetailService orderDetailService;


    @Override
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {
        //获取传入的 参数ordermaster 的 OrderDetailDto集合
        List<OrderDetailDto> items = orderMasterDto.getItems();

        List<OrderDetail> orderDetailList = Lists.newArrayList();

        BigDecimal totalParice = new BigDecimal("0");
        for (OrderDetailDto orderDetailDto:items) {
            //根据dto的 productId 获取对应的ProductInfo
            ResultResponse<ProductInfo> ResultproductInfo = productInfoService.queryById(orderDetailDto.getProductId());
            if (ResultproductInfo.getCode()== ResultEnums.FAIL.getCode()){
                throw new CustomException(ResultEnums.FAIL.getMsg());
            }
            ProductInfo productInfo = ResultproductInfo.getData();
            if (productInfo.getProductStock()<orderDetailDto.getProductQuantity()){
                throw new CustomException(ResultEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            OrderDetail orderDetail = OrderDetail.builder().detailId(IDUtils.createIdbyUUID())
                    .orderId(productInfo.getProductId()).productIcon(productInfo.getProductIcon())
                    .productName(productInfo.getProductName()).productPrice(productInfo.getProductPrice())
                    .productQuantity(orderDetailDto.getProductQuantity()).productId(productInfo.getProductId()).build();
            orderDetailList.add(orderDetail);
            totalParice= BigDecimalUtil.add( totalParice,
                    BigDecimalUtil.multi(productInfo.getProductPrice(),orderDetailDto.getProductQuantity() ) );
            productInfo.setProductStock(productInfo.getProductStock()-orderDetailDto.getProductQuantity());
            productInfoService.updateProduct(productInfo);
        }
        //生成订单id
        String orderId = IDUtils.createIdbyUUID();

        OrderMaster orderMaster = OrderMaster.builder().buyerAddress(orderMasterDto.getAddress())
                .buyerName(orderMasterDto.getName()).buyerOpenid(orderMasterDto.getOpenid())
                .orderAmount(totalParice).orderId(orderId).orderStatus(OrderEnum.NEW.getCode())
                .payStatus(PayEnum.WAIT.getCode()).buyerPhone(orderMasterDto.getPhone()).build();

        List<OrderDetail> orderDetails = orderDetailList.stream().map(orderDetail -> {
            orderDetail.setOrderId(orderId);
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.batchInsert(orderDetails);
        orderMasterRepository.save(orderMaster);
        HashMap<String, String> map = Maps.newHashMap();
        map.put("orderId",orderId );
        return ResultResponse.success(map);
    }
}
