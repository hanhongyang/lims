package com.gmlimsqi.business.labtest.service;

import java.util.List;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultBase;
import com.gmlimsqi.business.labtest.domain.OpJczxPcrResultInfo;
import com.gmlimsqi.business.labtest.domain.OpPcrEntrustOrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

/**
 * 样品化验PCRService接口
 * 
 * @author hhy
 * @date 2025-10-13
 */
public interface IOpJczxPcrResultBaseService 
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

    public String importPcrResult(MultipartFile file) throws Exception;

    /**
     * 审核PCR项目结果（更新备注和流转状态）
     * @param items 包含项目ID和备注的列表
     * @return 结果行数
     */
    int examinePcrResultBase(List<OpPcrEntrustOrderItem> items,String examinePassFlag,String resultNo);
    int cancelExaminePcrResult(String resultNo);
}
