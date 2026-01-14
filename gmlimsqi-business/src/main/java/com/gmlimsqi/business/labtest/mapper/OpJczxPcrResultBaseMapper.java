package com.gmlimsqi.business.labtest.mapper;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultBase;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 样品化验PCRMapper接口
 * 
 * @author hhy
 * @date 2025-10-13
 */
public interface OpJczxPcrResultBaseMapper 
{
    /**
     * 查询样品化验PCR
     * 
     * @param opJczxPcrResultBaseId 样品化验PCR主键
     * @return 样品化验PCR
     */
    public OpJczxPcrResultBase selectOpJczxPcrResultBaseByOpJczxPcrResultBaseId(String opJczxPcrResultBaseId);

    /**
     * 查询样品化验PCR列表
     * 
     * @param opJczxPcrResultBase 样品化验PCR
     * @return 样品化验PCR集合
     */
    public List<OpJczxPcrResultBase> selectOpJczxPcrResultBaseList(OpJczxPcrResultBase opJczxPcrResultBase);

    /**
     * 新增样品化验PCR
     * 
     * @param opJczxPcrResultBase 样品化验PCR
     * @return 结果
     */
    public int insertOpJczxPcrResultBase(OpJczxPcrResultBase opJczxPcrResultBase);

    /**
     * 修改样品化验PCR
     * 
     * @param opJczxPcrResultBase 样品化验PCR
     * @return 结果
     */
    public int updateOpJczxPcrResultBase(OpJczxPcrResultBase opJczxPcrResultBase);

    /**
     * 通过样品化验PCR主键更新删除标志
     *
     * @param opJczxPcrResultBaseId 样品化验PCRID
     * @return 结果
     */
    public int updateDeleteFlagById(String opJczxPcrResultBaseId);




    /**
     * 批量新增检测中心pce化验单子
     * 
     * @param opJczxPcrResultInfoList 检测中心pce化验单子列表
     * @return 结果
     */
    public int batchOpJczxPcrResultInfo(List<OpJczxPcrResultInfo> opJczxPcrResultInfoList);


    public void updateDeleteFlagByBaseId(@Param("opJczxPcrResultBaseId") String opJczxPcrResultBaseId,@Param("updateUserId")  String updateUserId);

    /**
     * 查询样品化验PCR列表
     *
     * @return 样品化验PCR集合
     */
    public List<OpJczxPcrResultBase> selectOpJczxPcrResultBaseListBySampleNoList(@Param("sampleNoList")List<String> sampleNoList);

    void updateFileNameByResultNo(@Param("resultNo")String resultNo,@Param("fileName")String fileName);

    void updateExamineFlagByResultNo(@Param("examinePassFlag")String examinePassFlag, @Param("resultNo")String resultNo);

    OpJczxPcrResultBase selectByNo(@Param("resultNo")String resultNo);

    void updateCancelExamine(@Param("opJczxPcrResultBaseId")String opJczxPcrResultBaseId);
}
