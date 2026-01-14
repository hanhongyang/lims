package com.gmlimsqi.business.rmts.entity.pojo;

import com.gmlimsqi.business.rmts.entity.vo.PlanSyncVO;
import lombok.Data;

import java.util.List;

/**
 * 计划同步响应
 */
@Data
public class ApiResponse<T> {

    /**
     * 返回状态码，0000表示成功，其他为调用失败
     */
    private String status;

    /**
     * 对status的中文描述
     */
    private String message;

    /**
     * 数据实体列表
     */
    private T data;

}
