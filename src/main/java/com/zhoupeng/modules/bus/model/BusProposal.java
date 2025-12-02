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
 * create_time 2025/12/1:14:02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bus_proposal")
@ApiModel(value = "计划书对象", description = "产品计划书表")
public class BusProposal extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "产品id")
    private Integer id;

    @ApiModelProperty(value = "产品id")
    private Integer productId;

    @ApiModelProperty(value = "计划书编号")
    private String code;

    @ApiModelProperty(value = " 保种")
    private String type;

    @ApiModelProperty(value = " 保费")
    private String money;

    @ApiModelProperty(value = "与受保人关系")
    private String relation;

    @ApiModelProperty(value = "受保人")
    private String insuredName;

    @ApiModelProperty(value = "年龄")
    private String insuredAge;

    @ApiModelProperty(value = "生日")
    private String insuredBirthday;

    @ApiModelProperty(value = " 性别")
    private String insuredGender;

    @ApiModelProperty(value = "原籍")
    private String insuredLocation;

    @ApiModelProperty(value = "习惯（吸烟/不吸烟）")
    private String insuredHabit;

    @ApiModelProperty(value = "状态（正常/取消）")
    private String status;

}
