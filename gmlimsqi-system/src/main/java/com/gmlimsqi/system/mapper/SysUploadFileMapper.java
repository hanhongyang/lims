package com.gmlimsqi.system.mapper;

import com.gmlimsqi.system.domain.SysUploadFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface SysUploadFileMapper {

    /**
     * 根据ID查询文件
     */
    SysUploadFile selectFileById(@Param("fileId") String fileId);

    /**
     * 插入文件记录
     */
    int insertFile(SysUploadFile file);

    /**
     * 根据ID删除文件记录
     */
    int deleteFileById(@Param("fileId") String fileId);

    /**
     * 更新文件状态
     */
    int updateFileStatus(SysUploadFile file);

    /**
     * 查询过期的临时文件
     */
    List<SysUploadFile> selectExpiredFiles(Date expireTime);

    int updateByFileId(String fileId);

    String selectFileBySourceId(String sourceId);
    String selectFileUrlBySourceId(String sourceId);

}