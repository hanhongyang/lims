package com.gmlimsqi.sap.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmlimsqi.common.annotation.FunLog;
import com.gmlimsqi.common.enums.BusinessType;
import com.gmlimsqi.common.enums.FunTypeEnums;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.sap.accept.domain.dto.*;
import com.gmlimsqi.sap.accept.domain.material.BsSapinfo;
import com.gmlimsqi.sap.accept.domain.vo.CheckResRetJudgeVO;
import com.gmlimsqi.sap.accept.domain.vo.FrozenSemenQcDetailVO;
import com.gmlimsqi.sap.accept.domain.vo.InOutInfoVO;
import com.gmlimsqi.sap.accept.domain.vo.InboundInspectionDetailVO;
import com.gmlimsqi.sap.accept.mapper.material.BsSapinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gmlimsqi.sap.util.HttpUtils.HttpPost;

/**
 * SAP工具类
 */
@Service
@Component
public class SapUtils {

    @Autowired
    private BsSapinfoMapper bsSapinfoMapper;

    /**
     * 查询出入库信息
     */
    @FunLog(title = "SAP工具类", systemName = FunTypeEnums.SAP, description = "查询SAP订单", businessType = BusinessType.OTHER)
    public List<InOutInfoVO> queryInOutInfo(InOutInfoDTO inOutInfoDTO) throws JsonProcessingException {
        // 校验参数信息
        if (StringUtils.isBlank(inOutInfoDTO.getWERKS())) {
            throw new IllegalArgumentException("工厂编号不能为空");
        }
        if (StringUtils.isBlank(inOutInfoDTO.getBUDAT())) {
            throw new IllegalArgumentException("日期不能为空");
        }
        if (StringUtils.isBlank(inOutInfoDTO.getCHARG())) {
            throw new IllegalArgumentException("批次号不能为空");
        }
        if (StringUtils.isBlank(inOutInfoDTO.getZLEIX())) {
            throw new IllegalArgumentException("类型不能为空");
        }

        // 构建body参数
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("WERKS", inOutInfoDTO.getWERKS());
        bodyParams.put("BUDAT", inOutInfoDTO.getBUDAT());
        bodyParams.put("CHARG", inOutInfoDTO.getCHARG());
        bodyParams.put("ZLEIX", inOutInfoDTO.getZLEIX());

        // 构建dataBody参数
        Map<String, Object> dataBody = new HashMap<>();
        dataBody.put("DATA", bodyParams);

        String jsonString = JSON.toJSONString(dataBody);

        // 查询SAP接口配置信息
        BsSapinfo bsSapinfo = bsSapinfoMapper.getSapConfig("QueryInOut");

        if (bsSapinfo == null) {
            throw new IllegalArgumentException("未配置查询出入库信息接口");
        }

        String gmResult;

        try {
            gmResult = HttpPost(bsSapinfo.getCusername(), bsSapinfo.getCpwd(), bsSapinfo.getCurl(), jsonString);
        } catch (IOException e) {
            throw new RuntimeException("调用光明接口发生异常" + e);
        }

        List<InOutInfoVO> inOutInfoList = new ArrayList<>();

        try {
            // 解析JSON到实体类列表
            inOutInfoList = JsonParseUtils.parseInOutInfo(gmResult);
        }catch (IOException e){
            throw new RuntimeException("解析出入库信息失败" + e);
        }

        if (inOutInfoList.isEmpty()) {
            throw new ServiceException("查询出入库信息为空");
        }

        return inOutInfoList;
    }

    /**
     * 检验批结果回传&判定
     */
    @FunLog(title = "SAP工具类", systemName = FunTypeEnums.SAP, description = "检验批结果回传&判定", businessType = BusinessType.OTHER)
    public CheckResRetJudgeVO checkResRetJudge(List<CheckResRetJudgeDTO> checkResRetJudgeDTOList) throws JsonProcessingException {
        if (CollectionUtils.isEmpty(checkResRetJudgeDTOList)) {
            throw new IllegalArgumentException("检验批结果回传&判定参数不能为空");
        }

        int i = 10;

        for (CheckResRetJudgeDTO checkResRetJudgeDTO : checkResRetJudgeDTOList){
            // 校验参数信息
            if (StringUtils.isBlank(checkResRetJudgeDTO.getWERK())) {
                throw new IllegalArgumentException("工厂编号不能为空");
            }
            if (StringUtils.isBlank(checkResRetJudgeDTO.getZTYPE())) {
                throw new IllegalArgumentException("类型不能为空");
            }
            if (StringUtils.isBlank(checkResRetJudgeDTO.getPRUEFLOS())) {
                throw new IllegalArgumentException("检验批次不能为空");
            }
            if (StringUtils.isBlank(checkResRetJudgeDTO.getVCODE())) {
                throw new IllegalArgumentException("判定结果不能为空");
            }

            if (!CollectionUtils.isEmpty(checkResRetJudgeDTO.getITEMS())) {
                for (CheckResRetJudgeItemDTO checkResRetJudgeItemDTO : checkResRetJudgeDTO.getITEMS()){
                    // 累加行项，每次加10
                    checkResRetJudgeItemDTO.setITEM(String.valueOf(i));
                    i += 10;
                }
            }

        }

        // 获取SAP接口配置信息
        BsSapinfo bsSapinfo = bsSapinfoMapper.getSapConfig("CheckResRetJudge");

        if (bsSapinfo == null) {
            throw new IllegalArgumentException("未配置检验批结果回传&判定接口");
        }

        // 构建请求参数
        Map<String, Object> headerBody = new HashMap<>();
        headerBody.put("HEADER", checkResRetJudgeDTOList);

        String jsonString = JSON.toJSONString(headerBody);

        System.out.println(jsonString + "2222222222");

        String result;

        try {
            result = HttpPost(bsSapinfo.getCusername(), bsSapinfo.getCpwd(), bsSapinfo.getCurl(), jsonString);
        } catch (IOException e) {
            throw new RuntimeException("调用光明接口发生异常" + e);
        }

        CheckResRetJudgeVO checkResRetJudgeVO = new CheckResRetJudgeVO();

        try {
            // 解析JSON到实体类
            checkResRetJudgeVO = JsonParseUtils.parseCheckResRetJudge(result);
        } catch (IOException e) {
            throw new RuntimeException("解析检验批结果回传&判定失败" + e);
        }

        return checkResRetJudgeVO;
    }

    /**
     * 入库检验批查询
     */
    @FunLog(title = "SAP工具类", systemName = FunTypeEnums.SAP, description = "入库检验批查询", businessType = BusinessType.OTHER)
    public List<InboundInspectionDetailVO> queryInboundInspection(List<InboundInspectionDTO> inboundInspectionDTOList) {
        if (CollectionUtils.isEmpty(inboundInspectionDTOList)) {
            throw new IllegalArgumentException("入库检验批查询参数不能为空");
        }
        for (InboundInspectionDTO inboundInspectionDTO : inboundInspectionDTOList){
            // 校验参数信息
            if (StringUtils.isBlank(inboundInspectionDTO.getWERK())) {
                inboundInspectionDTO.setWERK(SecurityUtils.getSapCode());
            }
            /*if (StringUtils.isBlank(inboundInspectionDTO.getPRUEFLOS())) {
                throw new IllegalArgumentException("检验批次不能为空");
            }*/
        }

        // 查询SAP接口配置信息
        BsSapinfo bsSapinfo = bsSapinfoMapper.getSapConfig("QueryInboundInspection");

        if (bsSapinfo == null) {
            throw new IllegalArgumentException("未配置入库检验批查询接口");
        }

        // 构建请求参数
        Map<String, Object> bodyData = new HashMap<>();
        bodyData.put("DATA", inboundInspectionDTOList);

        String jsonString = JSON.toJSONString(bodyData);

        String result;

        try {
            result = HttpPost(bsSapinfo.getCusername(), bsSapinfo.getCpwd(), bsSapinfo.getCurl(), jsonString);
        } catch (IOException e) {
            throw new RuntimeException("调用光明接口发生异常" + e);
        }

        // 解析JSON到实体类列表
        List<InboundInspectionDetailVO> inboundInspectionDetailVOList = JsonParseUtils.parseInboundInspectionDetail(result);

        if (CollectionUtils.isEmpty(inboundInspectionDetailVOList)) {
            return null;
        }

        return inboundInspectionDetailVOList;
    }

    /**
     * 冻精质量控制查询
     */
    @FunLog(title = "SAP工具类", systemName = FunTypeEnums.SAP, description = "冻精质检数据查询", businessType = BusinessType.OTHER)
    public List<FrozenSemenQcDetailVO> queryFrozenSemenQc(List<FrozenSemenQcQueryDTO> frozenSemenQcQueryDTOList) {
        if (frozenSemenQcQueryDTOList.isEmpty()){
            throw new IllegalArgumentException("参数不能为空");
        }

        for (FrozenSemenQcQueryDTO frozenSemenQcQueryDTO : frozenSemenQcQueryDTOList){
            // 校验参数信息
            if (StringUtils.isBlank(frozenSemenQcQueryDTO.getWERK())) {
                throw new IllegalArgumentException("工厂编号不能为空");
            }
        }

        // 查询SAP接口配置信息
        BsSapinfo bsSapinfo = bsSapinfoMapper.getSapConfig("QueryFrozenSemenQc");

        if (bsSapinfo == null) {
            throw new IllegalArgumentException("未配置冻精质检数据查询接口");
        }

        Map<String, Object> bodyData = new HashMap<>();
        bodyData.put("DATA", frozenSemenQcQueryDTOList);

        String jsonString = JSON.toJSONString(bodyData);

        String result;

        try {
            result = HttpPost(bsSapinfo.getCusername(), bsSapinfo.getCpwd(), bsSapinfo.getCurl(), jsonString);
        } catch (IOException e) {
            throw new RuntimeException("调用光明接口发生异常" + e);
        }

        // 解析JSON到实体类列表
        List<FrozenSemenQcDetailVO> frozenSemenQcDetailVOList = JsonParseUtils.parseFrozenSemenQcDetail(result);

        if (CollectionUtils.isEmpty(frozenSemenQcDetailVOList)) {
            return null;
        }

        return frozenSemenQcDetailVOList;

    }
}
