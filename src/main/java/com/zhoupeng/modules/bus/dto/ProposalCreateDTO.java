package com.zhoupeng.modules.bus.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZhouPP
 * create_time 2025/12/1:20:31
 */
@Data
public class ProposalCreateDTO {
    @ApiModelProperty(value = "必填：前端从产品列表选出来")
    private Integer productId;

    @ApiModelProperty(value = "保种")
    private String type;

    @ApiModelProperty(value = "保费")
    private String money;

    @ApiModelProperty(value = " 与受保人关系")
    private String relation;

    @ApiModelProperty(value = "受保人")
    private String insuredName;

    @ApiModelProperty(value = "受保人年龄")
    private String insuredAge;

    @ApiModelProperty(value = "受保人生日")
    private String insuredBirthday;

    @ApiModelProperty(value = "受保人性别")
    private String insuredGender;

    @ApiModelProperty(value = "受保人籍贯")
    private String insuredLocation;

    @ApiModelProperty(value = "受保人习惯， 吸烟/不吸烟")
    private String insuredHabit;

    @ApiModelProperty(value = "备注")
    private String remark;
}
