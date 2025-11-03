package com.life.jigsaw.common.response;

import com.life.jigsaw.enums.ErrCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回对象
 */
@Data
public class Response<T> implements Serializable {
    @Schema(description = "0000表示成功，其他失败")
    private String code;
    @Schema(description = "成功/报错信息")
    private String message;
    private Boolean isSuccess = true;

    @Schema(description = "返回数据")
    private T data;



    public Response() {
    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
        if(!ErrCode.SUCCESS.getCode().equals(code)){
            isSuccess = false;
        }
    }

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        if(!ErrCode.SUCCESS.getCode().equals(code)){
            isSuccess = false;
        }
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(ErrCode.SUCCESS.getCode(), ErrCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> Response<T>  success(T data, String message) {
        return new Response<>(ErrCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 成功返回结果为空
     *
     */
    public static <T> Response<T> success() {
        return new Response<>(ErrCode.SUCCESS.getCode(), ErrCode.SUCCESS.getMessage());
    }
    
    /**
     * 错误返回结果
     *
     * @param message 错误信息
     */
    public static <T> Response<T> error(String message) {
        return new Response<>(ErrCode.FAILED.getCode(), message, null);
    }
    
    /**
     * 错误返回结果
     *
     * @param code 错误码
     * @param message 错误信息
     */
    public static <T> Response<T> error(String code, String message) {
        return new Response<>(code, message, null);
    }

}
