package com.gmlimsqi.sap.accept.controller;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gmlimsqi.common.annotation.Anonymous;
import com.gmlimsqi.common.core.domain.R;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.sap.accept.domain.dto.CheckResRetJudgeDTO;
import com.gmlimsqi.sap.accept.domain.dto.FrozenSemenQcQueryDTO;
import com.gmlimsqi.sap.accept.domain.dto.InOutInfoDTO;
import com.gmlimsqi.sap.accept.domain.dto.InboundInspectionDTO;
import com.gmlimsqi.sap.accept.domain.sapresponse.FrozenSemenQcResponse;
import com.gmlimsqi.sap.accept.domain.vo.CheckResRetJudgeVO;
import com.gmlimsqi.sap.accept.domain.vo.FrozenSemenQcDetailVO;
import com.gmlimsqi.sap.accept.domain.vo.InOutInfoVO;
import com.gmlimsqi.sap.accept.domain.vo.InboundInspectionDetailVO;
import com.gmlimsqi.sap.util.SapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SAP接口
 */
@RestController
@RequestMapping("/sap")
public class SapController {

    @Autowired
    private SapUtils sapUtils;

    /**
     * 查询出入库信息
     */
    @Anonymous
    @PostMapping("/queryInOutInfo")
    public R queryInOutInfo(@RequestBody InOutInfoDTO inOutInfoDTO) {
         try {
             List<InOutInfoVO> inOutInfoVOList = sapUtils.queryInOutInfo(inOutInfoDTO);

             if (inOutInfoVOList == null) {
                 return R.fail("查询出入库信息为空");
             }

             return R.ok(inOutInfoVOList);
         } catch (Exception e) {
             return R.fail(e.getMessage());
         }
    }

    /**
     * 检验批结果回传&判定
     */
    @Anonymous
    @PostMapping("/checkResRetJudge")
    public CheckResRetJudgeVO checkResRetJudge(@RequestBody List<CheckResRetJudgeDTO> checkResRetJudgeDTOList) {
        try {
            CheckResRetJudgeVO checkResRetJudgeVO = sapUtils.checkResRetJudge(checkResRetJudgeDTOList);
            return checkResRetJudgeVO;
        } catch (Exception e) {
            CheckResRetJudgeVO checkResRetJudgeVO = new CheckResRetJudgeVO();
            checkResRetJudgeVO.setReturnInfo(new CheckResRetJudgeVO.RETURN());
            checkResRetJudgeVO.getReturnInfo().setType("E");
            checkResRetJudgeVO.getReturnInfo().setMessage(e.getMessage());
            return checkResRetJudgeVO;
        }
    }

    /**
     * 入库检验批查询
     */
    @Anonymous
    @PostMapping("/queryInboundInspection")
    public R queryInboundInspection(@RequestBody List<InboundInspectionDTO> inboundInspectionDTOList) {
        try {
            List<InboundInspectionDetailVO> inboundInspectionDetailVOList = sapUtils.queryInboundInspection(inboundInspectionDTOList);

            if (inboundInspectionDetailVOList == null) {
                return R.fail("查询入库检验批结果为空");
            }

            return R.ok(inboundInspectionDetailVOList);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 冻精质检数据查询
     * @param frozenSemenQcQueryDTOList
     * @return {@link List<FrozenSemenQcDetailVO>}
     */
    @Anonymous
    @PostMapping("/queryFrozenSemenQc")
    public R queryFrozenSemenQc(@RequestBody List<FrozenSemenQcQueryDTO> frozenSemenQcQueryDTOList) {
        FrozenSemenQcQueryDTO frozenSemenQcQueryDTO = frozenSemenQcQueryDTOList.get(0);
        if (frozenSemenQcQueryDTO == null || frozenSemenQcQueryDTO.getBUDAT() == null){
            return R.ok(null);
        }
        try {
            List<FrozenSemenQcDetailVO> frozenSemenQcResponse = sapUtils.queryFrozenSemenQc(frozenSemenQcQueryDTOList);

            if (frozenSemenQcResponse == null) {
                return R.fail("查询冻精质检数据为空");
            }

            return R.ok(frozenSemenQcResponse);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }
}
