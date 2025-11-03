package com.life.jigsaw.exceptions;

import com.life.jigsaw.enums.ErrCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;

/**
 * 业务异常
 * 
 * @author iif
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class ServiceException extends RuntimeException
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 错误提示
     */
    private String message;

    private String code;

    private Object data;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {}

    public ServiceException(String message)
    {
        this.message = message;
        this.code = ErrCode.FAILED.getCode();
    }

    public ServiceException(String code, String message, Object data)
    {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public ServiceException(String code, String message){
        this.code = code;
        this.message = message;
    }

    public ServiceException(ErrCode errCode){
        this.message = errCode.getMessage();
        this.code = errCode.getCode();
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public ServiceException setMessage(String message)
    {
        this.message = message;
        return this;
    }
}