package com.zhoupeng.modules.bus.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhouPP
 * create_time 2025/12/1:13:56
 */
@Data
public class PlatformAccountDTO implements Serializable {

    private String loginUrl;
    private String username;
    private String password;
}
