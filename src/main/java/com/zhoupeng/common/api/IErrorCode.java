package com.zhoupeng.common.api;

/**
 * 封装API的错误码
 * Created by Paris_Zhou on 2022/4/19.
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}
