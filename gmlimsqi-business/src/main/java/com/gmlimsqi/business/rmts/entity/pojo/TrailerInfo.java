package com.gmlimsqi.business.rmts.entity.pojo;

import lombok.Data;

/**
 * 挂车信息
 */
@Data
public class TrailerInfo {
    private Integer trailerId; // 挂车id
    private String trailerNumber; // 挂车车牌
}