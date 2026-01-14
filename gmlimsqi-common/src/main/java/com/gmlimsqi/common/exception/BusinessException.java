package com.gmlimsqi.common.exception;

/**
 * 业务异常类
 * 
 * @author ruoyi
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public BusinessException() {
    }

    public BusinessException(String message) {
        this.message = message;
    }

    public BusinessException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BusinessException(String message, Integer code, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public BusinessException setMessage(String message) {
        this.message = message;
        return this;
    }

    public BusinessException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}