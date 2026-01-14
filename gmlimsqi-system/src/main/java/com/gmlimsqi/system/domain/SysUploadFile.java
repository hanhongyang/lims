package com.gmlimsqi.system.domain;

import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 文件上传存储实体
 */
@Data
public class SysUploadFile extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    /** 文件ID */
    private String fileId;
    
    /** 原始文件名 */
    private String originalName;
    
    /** 存储路径 */
    private String filePath;
    private String url;
    /** 文件大小 */
    private Long fileSize;
    
    /** 文件类型 */
    private String fileType;
    
    /** 文件扩展名 */
    private String fileExtension;
    
    /** 上传用户 */
    private String uploadUser;
    
    /** 上传时间 */
    private Date uploadTime;
    
    /** 引用计数 */
    private Integer referenceCount;
    
    /** 文件状态（0-正式，1-临时） */
    private String status;

    //来源表id
    private String sourceId;

}