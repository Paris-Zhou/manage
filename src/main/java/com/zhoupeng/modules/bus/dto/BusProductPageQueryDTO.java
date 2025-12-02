package com.zhoupeng.modules.bus.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZhouPP
 * create_time 2025/12/2:17:59
 */
@Data
@ApiModel("产品分页查询条件")
public class BusProductPageQueryDTO {

    @ApiModelProperty("页码，从1开始")
    private Integer pageNum = 1;

    @ApiModelProperty("每页条数")
    private Integer pageSize = 10;

    @ApiModelProperty("产品名称（模糊）")
    private String name;

    @ApiModelProperty("产品编码（模糊）")
    private String code;

    @ApiModelProperty("保险种类（模糊）")
    private String type;

    @ApiModelProperty("公司名称（模糊）")
    private String company;
}
