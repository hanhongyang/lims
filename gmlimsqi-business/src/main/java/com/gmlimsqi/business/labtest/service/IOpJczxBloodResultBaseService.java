package com.gmlimsqi.business.labtest.service;


import com.gmlimsqi.business.labtest.bo.BloodResultImportBo;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderItem;
import com.gmlimsqi.business.labtest.domain.OpBloodEntrustOrderSample;
import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultBase;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 检测中心血样化验单主Service接口
 * 
 * @author hhy
 * @date 2025-10-14
 */
public interface IOpJczxBloodResultBaseService 
{

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


    int examineBloodResultBase(List<OpBloodEntrustOrderSample> sampleList,String examinePassFlag,String resultNo);

    int cancelExamineBloodResult(String resultNo);

    String importBloodResult(MultipartFile file) throws IOException;

    String importBloodResultNew(MultipartFile file, String username) throws Exception;
}
