package com.life.jigsaw.enums;

/*
 * api response code
 * */

import lombok.Getter;

@Getter
public enum ErrCode {

    SUCCESS("0000", "成功"), // 成功
    FAILED("500", "失败"), // 失败
    UnAuthorizedException("401", "Not Authorized"), //  用户未登录或未授权
    APIException("0001", "通用的业务逻辑响应异常"),//通用的业务逻辑响应异常
    ParamException("0002", "参数异常"), // 参数异常
    DBException("0003", "数据库异常"),//数据库异常
    InternalError("0004", "内部错误"),//内部错误
    SystemException("0100", "未知的系统异常");//未知的系统异常

    private final String code;
    private final String message;

    ErrCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
