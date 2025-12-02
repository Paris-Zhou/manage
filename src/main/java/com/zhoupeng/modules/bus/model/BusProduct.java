package com.zhoupeng.modules.bus.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhoupeng.common.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author ZhouPP
 * create_time 2025/12/1:14:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bus_product")
@ApiModel(value = "保险产品类型对象", description = "保险产品类型表")
public class BusProduct extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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
