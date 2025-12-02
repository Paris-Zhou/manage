package com.zhoupeng.modules.bus.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZhouPP
 * create_time 2025/12/1:20:33
 */
@Data
public class ProposalPageVO {
    @ApiModelProperty(value = "计划书id")
    private Integer id;

    @ApiModelProperty(value = "计划书编号")
    private String code;             //

    @ApiModelProperty(value = "保种")
    private String type;

    @ApiModelProperty(value = "保费")
    private String money;

    @ApiModelProperty(value = "产品id")
    private String productId;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "产品编码")
    private String productCode;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "受保人")
    private String insuredName;

    @ApiModelProperty(value = "受保人年龄")
    private String insuredAge;

    @ApiModelProperty(value = "生日")
    private String insuredBirthday;

    @ApiModelProperty(value = "受保人性别")
    private String insuredGender;

    @ApiModelProperty(value = "原籍")
    private String insuredLocation;

    @ApiModelProperty(value = "习惯")
    private String insuredHabit;

    @ApiModelProperty(value = "币种")
    private String status;
}
