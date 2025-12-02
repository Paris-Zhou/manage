package com.zhoupeng.modules.bus.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZhouPP
 * create_time 2025/12/1:20:32
 */
@Data
public class ProductSimpleDTO {
    @ApiModelProperty(value = "产品id")
    private Integer id;

    @ApiModelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "产品编码")
    private String code;

    @ApiModelProperty(value = "保险种类")
    private String type;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "公司英文名称")
    private String companyEn;

    @ApiModelProperty(value = "公司所在地")
    private String location;

    @ApiModelProperty(value = "国际通用代码")
    private String locationCode;

    @ApiModelProperty(value = "货币种类")
    private String currencyCode;

    @ApiModelProperty(value = "供款年份")
    private String periodYear;
}
