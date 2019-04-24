package com.xmcc.service.Impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.common.OrderEnum;
import com.xmcc.common.PayEnum;
import com.xmcc.common.ResultEnums;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderDetailDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.dto.OrderMasterPageDto;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;
import com.xmcc.param.DetailParam;
import com.xmcc.param.PageParam;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.OrderDetailService;
import com.xmcc.service.OrderMasterService;
import com.xmcc.service.PayService;
import com.xmcc.service.ProductInfoService;
import com.xmcc.utils.BigDecimalUtil;
import com.xmcc.utils.CustomException;
import com.xmcc.utils.IDUtils;
import com.xmcc.utils.Laji;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {

    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private OrderMasterRepository orderMasterRepository;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private PayService payService;


    @Transactional
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

    @Override
    public ResultResponse getOrderMasterList(PageParam pageParam) {

        //获取pageRequest对象
        PageRequest pageRequest = PageRequest.of(pageParam.getPage(),pageParam.getSize() );

        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setBuyerOpenid(pageParam.getOpenid());
        //获取example对象
        Example<OrderMaster> example = Example.of(orderMaster);

        Page<OrderMaster> allByBuyerOpenid = orderMasterRepository.findAll(example, pageRequest);

        List<OrderMaster> pageOrderMaster = allByBuyerOpenid.getContent();
        List<OrderMasterPageDto> orderMasterPageDtos = pageOrderMaster.stream().map(orderMaster1 ->
                OrderMasterPageDto.toOrderMasterPageDto(orderMaster1)).collect(Collectors.toList());

//        Laji.laige(orderMasterPageDtos.toString());
        return ResultResponse.success(orderMasterPageDtos);
    }

    @Override
    public ResultResponse getOrderDetail(DetailParam param) {

        OrderMaster orderMaster = orderMasterRepository.findByOrderIdAndBuyerOpenid(
                param.getOrderId(),param.getOpenid() );
        if (orderMaster==null ){
            return ResultResponse.fail(ResultEnums.NOT_EXITS.getMsg());
        }
        OrderMasterPageDto masterPageDto = OrderMasterPageDto.toOrderMasterPageDto(orderMaster);
        ResultResponse byOrderId = orderDetailService.findByOrderId(param.getOrderId());

        if (byOrderId.getCode()==ResultEnums.FAIL.getCode()){
            throw new CustomException(ResultEnums.FAIL.getMsg());
        }
        OrderDetail orderDetail = (OrderDetail) byOrderId.getData();
        List<OrderDetail> orderDetails = Lists.newArrayList(orderDetail);
        masterPageDto.setOrderDetailList(orderDetails);
        return ResultResponse.success(masterPageDto);
    }

    @Transactional
    @Override
    public ResultResponse cancel(DetailParam param) {
        //获取 Example
        OrderMaster master = new OrderMaster();
        master.setBuyerOpenid(param.getOpenid());
        master.setOrderId(param.getOrderId());
        Example<OrderMaster> masterExample = Example.of(master);
        //查询OrderMaster
        Optional<OrderMaster> optionalOrderMaster = orderMasterRepository.findOne(masterExample);
        if ( ! optionalOrderMaster.isPresent()){
            throw new CustomException(ResultEnums.NOT_EXITS.getMsg());
        }
        OrderMaster orderMaster = optionalOrderMaster.get();
        if (orderMaster.getOrderStatus()!=OrderEnum.NEW.getCode()){
            throw new CustomException(OrderEnum.ORDER_ERROR.getMsg());
        }
//        if (orderMaster.getPayStatus()!=PayEnum.FINSH.getCode()){
//            throw new CustomException(PayEnum.STATUS_ERROR.getMsg());
//        }
        payService.refund(orderMaster);
        //将状态设置成 1
        orderMaster.setOrderStatus(OrderEnum.CANCEL.getCode());
        orderMaster.setPayStatus(PayEnum.FAIL.getCode());
        orderMasterRepository.save(orderMaster);

        return ResultResponse.success();
    }


}
