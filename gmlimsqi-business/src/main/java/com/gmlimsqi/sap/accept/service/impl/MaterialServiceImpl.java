package com.gmlimsqi.sap.accept.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gmlimsqi.business.basicdata.domain.BusinessInvbill;
import com.gmlimsqi.business.basicdata.vo.BusinessInvbillVo;
import com.gmlimsqi.business.basicdata.service.IBusinessInvbillService;
import com.gmlimsqi.common.core.domain.entity.SysDept;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.sap.accept.domain.material.*;
import com.gmlimsqi.sap.accept.mapper.material.BaseSapupMapper;
import com.gmlimsqi.sap.accept.mapper.material.BsSapinfoMapper;
import com.gmlimsqi.sap.accept.service.MaterialService;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gmlimsqi.sap.util.HttpUtils.HttpPost;


@Service
public class MaterialServiceImpl implements MaterialService {
    private static final Logger log = LoggerFactory.getLogger(MaterialServiceImpl.class);
    @Autowired
    private BsSapinfoMapper sapinfoMapper;
    @Autowired
    private BaseSapupMapper sapupMapper;
    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private IBusinessInvbillService invbillService;
    
    @Override
    @Transactional
    public void syncMateical() throws ParseException {
//        格式化传参
        SimpleDateFormat sdfparam = new SimpleDateFormat("yyyyMMdd");
        
        List<String> sapCodeLsit = deptMapper.selectSapCodeList();
        
        //        1.获取光明sap地址密码等配置信息  bs_sapinfo 获取配置信息失败打印日志
        BsSapinfo bsSapInfo = sapinfoMapper.getSapConfig("InventoryTB");
        if (bsSapInfo == null) {
            throw new RuntimeException("获取光明配置信息失败");
        }
        if (!sapCodeLsit.isEmpty()) {
            for (String sapCode : sapCodeLsit) {
                List<SysDept> depts = deptMapper.selectBySapCode(sapCode);
                Long deptId = null;
                if(!CollectionUtils.isEmpty(depts)){
                    deptId = depts.get(0).getDeptId();
                }
//        2.Pro_Trans_GetSAPStartTime  获取base_sapup同步表获取上次同步时间，如果当前牧场不存在那么插入一条2023-06-01时间的数据作为初始值
                BaseSapup sapup = sapupMapper.selectSapup(sapCode);
                if (sapup == null) {
                    sapup = new BaseSapup();
                    String timeStr = "2023-06-01";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date time = sdf.parse(timeStr);
                    sapup.setId(IdUtils.fastUUID()).setDmodifytime(new Date()).setBsysdel("0").setCztcode(sapCode)
                            .setInv(time).setCus(time).setVen(time)
                            .setCreateTime(new Date());
                    sapupMapper.insertBaseSapup(sapup);
                }
                Date nowDate = new Date();
                String BEGTM = sdfparam.format(sapup.getInv());
                String ENDTM = sdfparam.format(nowDate);


//        3.后端发送请求光明数据传参 开始时间  结束时间（当前时间） 牧场sapCode
                Inv inv = new Inv();
                inv.setBEGTM(BEGTM).setENDTM(ENDTM).setWERKS(sapCode);
                Map<String, String> header = new HashMap<>();
                header.put("SRC_SYSTEM", "DB");
                header.put("DEST_SYSTEM", "SAP");
                String invJson = JSON.toJSONString(inv);
                String gmResult;
                try {
                    gmResult = HttpPost(bsSapInfo.getCusername(), bsSapInfo.getCpwd(), bsSapInfo.getCurl(), header, invJson);
                } catch (IOException e) {
                    throw new RuntimeException("调用光明接口发生异常" + e);
                }
//        4.获取到数据进行封装 批量导入本系统  bs_invbill_sap
                JSONObject jsonObject = JSONObject.parseObject(gmResult);
                String flag = jsonObject.getString("MSGTY");

                if ("S".equals(flag)) {
                    List<MATERIAL> material = JSONArray.parseArray(jsonObject.getString("MATERIAL"), MATERIAL.class);
                    HashSet<MATERIAL> insertList = new HashSet<>();
                    
                    if (ObjectUtils.isNotEmpty(material) && !material.isEmpty()) {
                        for (MATERIAL m : material) {

                            //不再补0，补0导致后面业务表的code不准确。
                            //String code = padStringTo18(m.getMATNR());
                            m.setMATNR(m.getMATNR());

                            BusinessInvbill ma2 =
                                    invbillService.selectInfoBySapCode(m.getMATNR());

                            if (ma2 == null) {
                                insertList.add(m);
                            }
                            log.info("物料档案同步，物料编码：{}", m.getMATNR());
                        }
                        
                        if (!insertList.isEmpty()) {
                            
                            Set<BusinessInvbillVo> noMatch =
                                    new HashSet<>();
                            for (MATERIAL m : insertList) {
                                BusinessInvbillVo invbillVo =
                                        new BusinessInvbillVo();
                                invbillVo
                                        .setCinvname(m.getMAKTX())
                                        .setCinvcode(m.getMATNR())
                                        .setCremark("")
                                        .setCclasscode(m.getMATKL())
                                        .setCclassname(m.getWGBEZ())
                                        .setIkz("否")
                                        .setIsSapType("0")
                                        //.setDeptId(m.getWERKS())
                                        //.setOwnDeptId(deptId.toString())
                                ;

                                
                                noMatch.add(invbillVo);
                                log.info("物料档案新增，物料编码：{}", m.getMATNR());
                            }
                            invbillService.insertBusinessInvbillBatch(noMatch.stream().toList());
                        }
                    }

                    //                修改sap同步表时间
                    sapupMapper.update("inv", nowDate, sapCode);
                } else if ("E".equals(flag)) {
                    throw new RuntimeException(jsonObject.getString("MESSAGE"));
                }
            }
        }
        
    }
    
    public static String padStringTo18(String input) {
        // 如果字符串长度已经达到或超过18位，则直接返回
        if (input.length() >= 18) {
            return input;
        }
        
        // 计算需要补0的数量
        int zerosToPad = 18 - input.length();
        
        // 使用StringBuilder构建结果字符串，前面补0
        StringBuilder paddedString = new StringBuilder();
        for (int i = 0; i < zerosToPad; i++) {
            paddedString.append('0');
        }
        
        // 拼接原始字符串
        paddedString.append(input);
        
        // 返回补完0后的字符串
        return paddedString.toString();
    }
    
}
