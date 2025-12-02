package com.zhoupeng.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ZhouPP
 * create_time 2025/12/1:14:11
 */
@Data
public class BaseEntity implements Serializable {
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "修改时间")
    private String updateBy;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;
}
