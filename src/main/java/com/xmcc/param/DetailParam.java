package com.xmcc.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailParam {

    @NotBlank(message = "Openid不能为空")
    @ApiModelProperty(value = "微信id",dataType = "String")
    private String openid;

    @NotBlank(message = "orderId不能为空")
    @ApiModelProperty(value = "订单的id",dataType = "String")
    private String orderId;

}
