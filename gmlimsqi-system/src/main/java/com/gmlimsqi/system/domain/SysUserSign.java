package com.gmlimsqi.system.domain;

import lombok.Data;

/**
 * 用户签名数据
 */
@Data
public class SysUserSign {

    /** 用户id */
    private Long userId;

    /** 签名文件id */
    private String sign;

    /** 签名文件url */
    private String signUrl;

}

