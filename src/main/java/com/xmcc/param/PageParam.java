package com.xmcc.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {

    @NotBlank(message = "Openid不能为空")
    @ApiModelProperty(value = "微信id",dataType = "String")
    private String openid;


    @ApiModelProperty(value = "当前页数",dataType = "Integer")
    private Integer page=0;

    @ApiModelProperty(value = "当前页面的显示的条数",dataType = "Integer")
    private Integer size;
}
