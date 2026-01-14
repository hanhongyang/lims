package com.gmlimsqi.business.labtest.service.impl;

import com.gmlimsqi.business.basicdata.domain.UserLabtestItem;
import com.gmlimsqi.business.basicdata.mapper.UserLabtestItemMapper;
import com.gmlimsqi.business.labtest.domain.*;
import com.gmlimsqi.business.labtest.dto.OpSampleReceiveDto;
import com.gmlimsqi.business.labtest.dto.ReturnDTO;
import com.gmlimsqi.business.labtest.mapper.*;
import com.gmlimsqi.business.labtest.service.IOpBloodEntrustOrderService;
import com.gmlimsqi.business.labtest.service.IOpFeedEntrustOrderService;
import com.gmlimsqi.business.labtest.service.IOpPcrEntrustOrderService;
import com.gmlimsqi.business.labtest.vo.OpBloodEntrustVo;
import com.gmlimsqi.business.labtest.vo.OpSampleReceiveDetailVo;
import com.gmlimsqi.business.labtest.vo.OpSampleReceiveVo;
import com.gmlimsqi.business.labtest.service.IOpSampleReceiveService;
import com.gmlimsqi.business.util.CodeGeneratorUtil;
import com.gmlimsqi.common.enums.EntrustOrderStatusEnum;
import com.gmlimsqi.common.enums.EntrustOrderTypeEnum;
import com.gmlimsqi.common.enums.YesNo2Enum;
import com.gmlimsqi.common.utils.CollectionUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class OpSampleReceiveServiceImpl implements IOpSampleReceiveService {

    @Autowired
    private OpSampleReceiceMapper sampleReceiceMapper;
    @Autowired
    private OpFeedEntrustOrderMapper feedEntrustOrderMapper;
    @Autowired
    private OpPcrEntrustOrderMapper pcrEntrustOrderMapper;
    @Autowired
    private OpBloodEntrustOrderMapper bloodEntrustOrderMapper;
    @Autowired
    private OpFeedEntrustOrderItemMapper feedEntrustOrderItemMapper;
    @Autowired
    private OpPcrEntrustOrderItemMapper pcrEntrustOrderItemMapper;
    @Autowired
    private OpBloodEntrustOrderItemMapper bloodEntrustOrderItemMapper;
    @Autowired
    private OpFeedEntrustOrderSampleMapper feedEntrustOrderSampleMapper;
    @Autowired
    private OpPcrEntrustOrderSampleMapper pcrEntrustOrderSampleMapper;
    @Autowired
    private OpBloodEntrustOrderSampleMapper bloodEntrustOrderSampleMapper;
    @Autowired
    private IOpBloodEntrustOrderService bloodEntrustOrderService;
    @Autowired
    private UserLabtestItemMapper userLabtestItemMapper;
    @Autowired
    private OpJczxTestTaskMapper testTaskMapper;
    @Autowired
    private OpBloodEntrustItemConfigMapper bloodEntrustItemConfigMapper;
    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;

    @Override
    public List<OpSampleReceiveVo> selectSampleReceiveList(OpSampleReceiveDto sampleReceiveDto) {
        // 处理结束日期，设置为当天的23:59:59
        if (sampleReceiveDto.getSendSampleDateEnd() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sampleReceiveDto.getSendSampleDateEnd());
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            sampleReceiveDto.setSendSampleDateEnd(calendar.getTime());
        }
        List<OpSampleReceiveVo> opSampleReceiveVos = new ArrayList<>();
        if("1".equals(sampleReceiveDto.getType())){
            if("0".equals(sampleReceiveDto.getIsReceive())){
                opSampleReceiveVos = sampleReceiceMapper.selectSampleReceiveListNotReceive(sampleReceiveDto);
            }else {
                opSampleReceiveVos = sampleReceiceMapper.selectSampleReceiveListIsReceive(sampleReceiveDto);
            }
        }else{
            opSampleReceiveVos = sampleReceiceMapper.selectSampleReceiveList2(sampleReceiveDto);
            return opSampleReceiveVos;
        }


        return opSampleReceiveVos;
    }

    @Override
    public OpSampleReceiveDetailVo selectSampleReceiveByEntrustOrderNo(String type, String entrustOrderNo) {

        OpSampleReceiveDetailVo detailVo = new OpSampleReceiveDetailVo();
        if(EntrustOrderTypeEnum.FEED.getCode().equals(type) || entrustOrderNo.contains("SLWT")){
            detailVo.setType(EntrustOrderTypeEnum.FEED.getCode());
            //根据批号查询orderId
            OpFeedEntrustOrder feedEntrustOrder = feedEntrustOrderMapper.selectByNo(entrustOrderNo);
            if(feedEntrustOrder == null){
                return null;
            }
            OpFeedEntrustOrder opFeedEntrustOrder = feedEntrustOrderMapper.selectOrderDetailById(feedEntrustOrder.getOpFeedEntrustOrderId());
            detailVo.setFeedEntrustOrder(opFeedEntrustOrder);
        }
        else if(EntrustOrderTypeEnum.BLOOD.getCode().equals(type) || entrustOrderNo.contains("XYWT")){
            detailVo.setType(EntrustOrderTypeEnum.BLOOD.getCode());
            //根据批号查询orderId
            OpBloodEntrustOrder bloodEntrustOrder = bloodEntrustOrderMapper.selectByNo(entrustOrderNo);
            if(bloodEntrustOrder == null){
                return null;
            }
            OpBloodEntrustVo detail = bloodEntrustOrderService.selectOpBloodEntrustOrderByOpBloodEntrustOrderId(bloodEntrustOrder.getOpBloodEntrustOrderId());
            detailVo.setBloodEntrustVo(detail);
        }
        else if(EntrustOrderTypeEnum.PCR.getCode().equals(type) || entrustOrderNo.contains("PCRWT")){
            detailVo.setType(EntrustOrderTypeEnum.PCR.getCode());
            //根据批号查询orderId
            OpPcrEntrustOrder pcrEntrustOrder = pcrEntrustOrderMapper.selectByNo(entrustOrderNo);
            if(pcrEntrustOrder == null){
                return null;
            }
            OpPcrEntrustOrder detail = pcrEntrustOrderMapper.selectOrderDetailById(pcrEntrustOrder.getOpPcrEntrustOrderId());
            detailVo.setPcrEntrustOrder(detail);
        }

        return detailVo;
    }

    @Override
    @Transactional
    public void deleteSample(String type, String sampleId) {
        String updateBy = String.valueOf(SecurityUtils.getLoginUser().getUserId());
        if(EntrustOrderTypeEnum.FEED.getCode().equals(type)){
            feedEntrustOrderSampleMapper.updateDeleteById(updateBy,sampleId);
        } else if(EntrustOrderTypeEnum.BLOOD.getCode().equals(type)){
            bloodEntrustOrderSampleMapper.updateDeleteById(updateBy,sampleId);
        }else if(EntrustOrderTypeEnum.PCR.getCode().equals(type)){
            pcrEntrustOrderSampleMapper.updateDeleteById(updateBy,sampleId);
        }
    }

    @Override
    @Transactional
    public void add(OpSampleReceiveDto dto) {
        String receiverId = String.valueOf(SecurityUtils.getLoginUser().getUserId());
        String receiverName = SecurityUtils.getLoginUser().getUser().getNickName();
        //String deptId = String.valueOf(SecurityUtils.getDeptId());
        String type = dto.getType();
        //修改状态为已接收
        if(EntrustOrderTypeEnum.FEED.getCode().equals(type)){

            if(dto.getSampleIds().length>0){
                Set<String> orderIdList = new HashSet<>();
                //更新样品表接收人、样品编号
                for (String sampleId : dto.getSampleIds()) {
                    OpFeedEntrustOrderSample opFeedEntrustOrderSample = feedEntrustOrderSampleMapper.
                            selectOpFeedEntrustOrderSampleByOpFeedEntrustOrderSampleId(sampleId);
                    String sampleNo = codeGeneratorUtil.generateFeedSampleNo();
                    opFeedEntrustOrderSample.setSampleNo(sampleNo);
                    int jhwNo = feedEntrustOrderSampleMapper.selectNextJhwNo();
                    opFeedEntrustOrderSample.setJhwNo(jhwNo);
                    opFeedEntrustOrderSample.setReceiverId(receiverId);
                    opFeedEntrustOrderSample.setReceiveTime(new Date());
                    opFeedEntrustOrderSample.fillUpdateInfo();
                    opFeedEntrustOrderSample.setIsReceive(YesNo2Enum.YES.getCode());
                    feedEntrustOrderSampleMapper.updateOpFeedEntrustOrderSample(opFeedEntrustOrderSample);
                    String orderId = feedEntrustOrderSampleMapper.selectOrderIdById(sampleId);
                    orderIdList.add(orderId);

                }
                //更新主表状态和接收人
                for (String orderId : orderIdList) {
                    OpFeedEntrustOrder opFeedEntrustOrder = new OpFeedEntrustOrder();
                    opFeedEntrustOrder.setOpFeedEntrustOrderId(orderId);
                    opFeedEntrustOrder.setReceiverId(receiverId);
                    opFeedEntrustOrder.setOperatorUserId(receiverId);
                    opFeedEntrustOrder.setReceiveTime(new Date());
                    opFeedEntrustOrder.setStatus(EntrustOrderStatusEnum.JCZ.getCode());
                    feedEntrustOrderMapper.updateOpFeedEntrustOrder(opFeedEntrustOrder);
                }


            }


        } else if(EntrustOrderTypeEnum.BLOOD.getCode().equals(type)){

            if(dto.getSampleIds().length>0){
                Set<String> orderIdList = new HashSet<>();
                //更新样品表接收人
                for (String sampleId : dto.getSampleIds()) {
                    OpBloodEntrustOrderSample opBloodEntrustOrderSample = bloodEntrustOrderSampleMapper.
                            selectById(sampleId);
                    String sampleNo = codeGeneratorUtil.generateBloodSampleNo();
                    opBloodEntrustOrderSample.setSampleNo(sampleNo);
                    opBloodEntrustOrderSample.setReceiverId(receiverId);
                    opBloodEntrustOrderSample.setReceiveTime(new Date());
                    opBloodEntrustOrderSample.fillUpdateInfo();
                    opBloodEntrustOrderSample.setIsReceive(YesNo2Enum.YES.getCode());
                    bloodEntrustOrderSampleMapper.updateOpBloodEntrustOrderSample(opBloodEntrustOrderSample);

                    String orderId = bloodEntrustOrderSampleMapper.selectOrderIdById(sampleId);
                    orderIdList.add(orderId);
                }
                //更新主表状态和接收人
                for (String orderId : orderIdList) {
                    OpBloodEntrustOrder opBloodEntrustOrder = new OpBloodEntrustOrder();
                    opBloodEntrustOrder.setOpBloodEntrustOrderId(orderId);
                    opBloodEntrustOrder.setReceiverId(receiverId);
                    opBloodEntrustOrder.setReceiveTime(new Date());
                    opBloodEntrustOrder.setStatus(EntrustOrderStatusEnum.JCZ.getCode());
                    bloodEntrustOrderMapper.updateOpBloodEntrustOrder(opBloodEntrustOrder);
                }

            }
        }else if(EntrustOrderTypeEnum.PCR.getCode().equals(type)){

            if(dto.getSampleIds().length>0){
                Set<String> orderIdList = new HashSet<>();
                //更新样品表接收人
                for (String sampleId : dto.getSampleIds()) {
                    OpPcrEntrustOrderSample opPcrEntrustOrderSample = pcrEntrustOrderSampleMapper.
                            selectOpPcrEntrustOrderSampleByOpPcrEntrustOrderSampleId(sampleId);
                    String sampleNo = codeGeneratorUtil.generatePcrSampleNo();
                    opPcrEntrustOrderSample.setSampleNo(sampleNo);
                    opPcrEntrustOrderSample.setReceiverId(receiverId);
                    opPcrEntrustOrderSample.setReceiveTime(new Date());
                    opPcrEntrustOrderSample.fillUpdateInfo();
                    opPcrEntrustOrderSample.setIsReceive(YesNo2Enum.YES.getCode());
                    pcrEntrustOrderSampleMapper.updateOpPcrEntrustOrderSample(opPcrEntrustOrderSample);

                    String orderId = pcrEntrustOrderSampleMapper.selectOrderIdById(sampleId);
                    orderIdList.add(orderId);
                }
                //更新主表状态和接收人
                for (String orderId : orderIdList) {
                    OpPcrEntrustOrder opPcrEntrustOrder = new OpPcrEntrustOrder();
                    opPcrEntrustOrder.setOpPcrEntrustOrderId(orderId);
                    opPcrEntrustOrder.setReceiverId(receiverId);
                    opPcrEntrustOrder.setReceiveTime(new Date());
                    opPcrEntrustOrder.setStatus(EntrustOrderStatusEnum.JCZ.getCode());
                    pcrEntrustOrderMapper.updateOpPcrEntrustOrder(opPcrEntrustOrder);
                }

            }
        }



    }

    @Override
    public void returnSample(ReturnDTO dto) {
        if(EntrustOrderTypeEnum.FEED.getCode().equals(dto.getType())){
            OpFeedEntrustOrder opFeedEntrustOrder = new OpFeedEntrustOrder();
            opFeedEntrustOrder.setOpFeedEntrustOrderId(dto.getOpEntrustOrderId());
            opFeedEntrustOrder.setIsReturn("1");
            opFeedEntrustOrder.setStatus("6");
            opFeedEntrustOrder.setReturnReason(dto.getReturnReason());
            feedEntrustOrderMapper.updateOpFeedEntrustOrder(opFeedEntrustOrder);
        } else if(EntrustOrderTypeEnum.BLOOD.getCode().equals(dto.getType())){
            OpBloodEntrustOrder opBloodEntrustOrder = new OpBloodEntrustOrder();
            opBloodEntrustOrder.setOpBloodEntrustOrderId(dto.getOpEntrustOrderId());
            opBloodEntrustOrder.setIsReturn("1");
            opBloodEntrustOrder.setStatus("6");
            opBloodEntrustOrder.setReturnReason(dto.getReturnReason());
            bloodEntrustOrderMapper.updateOpBloodEntrustOrder(opBloodEntrustOrder);
        }else if(EntrustOrderTypeEnum.PCR.getCode().equals(dto.getType())){
            OpPcrEntrustOrder opPcrEntrustOrder = new OpPcrEntrustOrder();
            opPcrEntrustOrder.setOpPcrEntrustOrderId(dto.getOpEntrustOrderId());
            opPcrEntrustOrder.setIsReturn("1");
            opPcrEntrustOrder.setStatus("6");
            opPcrEntrustOrder.setReturnReason(dto.getReturnReason());
            pcrEntrustOrderMapper.updateOpPcrEntrustOrder(opPcrEntrustOrder);
        }
    }


}
