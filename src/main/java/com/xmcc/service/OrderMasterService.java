package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.param.DetailParam;
import com.xmcc.param.PageParam;

public interface OrderMasterService {

    ResultResponse insertOrder(OrderMasterDto orderMasterDto);

    ResultResponse getOrderMasterList(PageParam pageParam);

    ResultResponse getOrderDetail(DetailParam param);

    ResultResponse cancel(DetailParam param);
}
