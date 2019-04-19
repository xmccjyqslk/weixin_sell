package com.xmcc.dto;

import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分页 page 订单参数实体类")
public class OrderMasterPageDto extends OrderMaster {

    @NotEmpty(message = "订单项不能为空")
    @Valid //表示需要嵌套验证
    @ApiModelProperty(value = "订单项集合",dataType = "List")
    private List<OrderDetail> orderDetailList;

    public static OrderMasterPageDto toOrderMasterPageDto(OrderMaster orderMaster){
        OrderMasterPageDto pageDto = new OrderMasterPageDto();
        BeanUtils.copyProperties(orderMaster, pageDto);
        return pageDto;
    }
}
