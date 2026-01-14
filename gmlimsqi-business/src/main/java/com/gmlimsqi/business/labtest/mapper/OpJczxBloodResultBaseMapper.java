package com.gmlimsqi.business.labtest.mapper;


import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultInfo;
import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultBase;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultBase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检测中心血样化验单主Mapper接口
 * 
 * @author hhy
 * @date 2025-10-14
 */
public interface OpJczxBloodResultBaseMapper 
{
    /**
     * 查询检测中心血样化验单主
     * 
     * @param opJczxPcrResultBaseId 检测中心血样化验单主主键
     * @return 检测中心血样化验单主
     */
    public OpJczxBloodResultBase selectOpJczxBloodResultBaseByOpJczxPcrResultBaseId(String opJczxPcrResultBaseId);

    /**
     * 查询检测中心血样化验单主列表
     * 
     * @param opJczxBloodResultBase 检测中心血样化验单主
     * @return 检测中心血样化验单主集合
     */
    public List<OpJczxBloodResultBase> selectOpJczxBloodResultBaseList(OpJczxBloodResultBase opJczxBloodResultBase);

    /**
     * 新增检测中心血样化验单主
     * 
     * @param opJczxBloodResultBase 检测中心血样化验单主
     * @return 结果
     */
    public int insertOpJczxBloodResultBase(OpJczxBloodResultBase opJczxBloodResultBase);

    /**
     * 修改检测中心血样化验单主
     * 
     * @param opJczxBloodResultBase 检测中心血样化验单主
     * @return 结果
     */
    public int updateOpJczxBloodResultBase(OpJczxBloodResultBase opJczxBloodResultBase);

    /**
     * 通过检测中心血样化验单主主键更新删除标志
     *
     * @param opJczxPcrResultBaseId 检测中心血样化验单主ID
     * @return 结果
     */
    public int updateDeleteFlagById(String opJczxPcrResultBaseId);

    /**
     * 批量通过检测中心血样化验单主主键更新删除标志
     *
     * @param opJczxPcrResultBaseId 检测中心血样化验单主ID
     * @return 结果
     */
    public int updateDeleteFlagByIds(String[] opJczxPcrResultBaseIds);



    int batchOpJczxBloodResultInfo(List<OpJczxBloodResultInfo> list);

    List<OpJczxBloodResultBase> selectOpJczxBloodResultBaseListBySampleNoList(@Param("sampleNoList") List<String> sampleNoList);

    void updateFileNameByResultNo(@Param("resultNo")String resultNo,@Param("fileName")String fileName);

    void updateExamineFlagByResultNo(@Param("examinePassFlag")String examinePassFlag, @Param("resultNo")String resultNo);

    OpJczxBloodResultBase selectByNo(@Param("resultNo")String resultNo);

    void updateCancelExamine(@Param("opJczxBloodResultBaseId")String opJczxBloodResultBaseId);


}
