package com.xmcc.dto;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.xmcc.entity.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;


import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoDto implements Serializable {
    @JsonProperty("id")
    private String productId;

    /** 名字. */
    @JsonProperty("name")
    private String productName;

    /** 单价. */
    @JsonProperty("price")
    private BigDecimal productPrice;


    /** 描述. */
    @JsonProperty("description")
    private String productDescription;

    /** 小图. */
    @JsonProperty("icon")
    private String productIcon;

    public static ProductInfoDto toProductInfoDto(ProductInfo productInfo){
        ProductInfoDto productInfoDto = new ProductInfoDto();
//        BeanUtils.copyProperties(productInfo,productInfoDto );
        productInfoDto.setProductName(productInfo.getProductName());
        productInfoDto.setProductPrice(productInfo.getProductPrice());
        productInfoDto.setProductIcon(productInfo.getProductIcon());
        productInfoDto.setProductDescription(productInfo.getProductDescription());
        productInfoDto.setProductId(productInfo.getProductId());
        return productInfoDto;
    }

}
