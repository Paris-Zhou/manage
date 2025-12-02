package com.zhoupeng.modules.bus.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZhouPP
 * create_time 2025/12/1:20:30
 */
@Data
public class ProposalPageQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    @ApiModelProperty(value = "计划书编号（模糊）")
    private String code;
    @ApiModelProperty(value = "受保人（模糊）")
    private String insuredName;
    @ApiModelProperty(value = "状态（正常/取消）")
    private String status;
}
