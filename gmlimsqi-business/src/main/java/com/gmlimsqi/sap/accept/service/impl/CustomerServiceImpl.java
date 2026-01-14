package com.gmlimsqi.sap.accept.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.gmlimsqi.business.basicdata.domain.BusinessSupplier;
import com.gmlimsqi.business.basicdata.service.IBusinessSupplierService;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import com.gmlimsqi.sap.accept.domain.customer.Line;
import com.gmlimsqi.sap.accept.domain.customer.Nev;
import com.gmlimsqi.sap.accept.domain.material.BaseSapup;
import com.gmlimsqi.sap.accept.domain.material.BsSapinfo;
import com.gmlimsqi.sap.accept.mapper.material.BaseSapupMapper;
import com.gmlimsqi.sap.accept.mapper.material.BsSapinfoMapper;
import com.gmlimsqi.sap.accept.service.CustomerService;
import com.gmlimsqi.system.mapper.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.gmlimsqi.sap.util.HttpUtils.HttpPost;


@Service
public class CustomerServiceImpl implements CustomerService {
    private static final int BATCH_SIZE = 100;
    private static final int NUM_THREADS = 5; // 线程池大小
    
    @Autowired
    private BsSapinfoMapper sapinfoMapper;
    @Autowired
    private BaseSapupMapper sapupMapper;
    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private IBusinessSupplierService businessSupplierService;

    @Override
    @Transactional
    public void quartzSupplier() throws ParseException {
        //        1.获取光明配置  获取sap同步表时间  向光明发送请求获取物料信息 生成同步日志
        SimpleDateFormat sdfparam = new SimpleDateFormat("yyyyMMdd");

        List<String> sapCodeLsit = deptMapper.selectSapCodeList();

        BsSapinfo bsSapInfo = sapinfoMapper.getSapConfig("CustomerTB");
        if (bsSapInfo == null) {
            throw new RuntimeException("获取光明配置信息失败");
        }
        if (!sapCodeLsit.isEmpty()) {
            for (String sapCode : sapCodeLsit) {
                BaseSapup sapup = sapupMapper.selectSapup(sapCode);
                if (sapup == null) {
                    sapup = new BaseSapup();
                    String timeStr = "2023-06-01";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date time = sdf.parse(timeStr);
                    sapup.setId(IdUtils.fastUUID()).setDmodifytime(new Date()).setBsysdel("0").setCztcode(sapCode)
                            .setInv(time).setCus(time).setVen(time);
                    sapupMapper.insertBaseSapup(sapup);
                }
                Date nowDate = new Date();
                String BEGTM = sdfparam.format(sapup.getVen());
                String ENDTM = sdfparam.format(nowDate);

                Nev nev = new Nev();
                nev.setSRC_SYSTEM("DB")
                        .setDEST_SYSTEM("SAP")
                        .setWERKS(sapCode)
                        .setBEGTM(BEGTM)
                        .setENDTM(ENDTM)
                        .setBPROLE("S");

                Map<String, Object> mp = new HashMap<>();
                mp.put("Header", nev);

                String nevJson = JSON.toJSONString(mp);

                String gmResult;

                System.out.println("进入同步供应商接口");

                // 打印入参
                System.out.println("入参：" + nevJson);
                System.out.println("入参：" + bsSapInfo.getCusername());
                System.out.println("入参：" + bsSapInfo.getCpwd());
                System.out.println("入参：" + bsSapInfo.getCurl());

                try {
                    gmResult = HttpPost(bsSapInfo.getCusername(),
                            bsSapInfo.getCpwd(),
                            bsSapInfo.getCurl(), nevJson);
                } catch (IOException e) {
                    throw new RuntimeException("调用光明接口发生异常" + e);
                }
                JSONObject jsonObject = JSONObject.parseObject(gmResult);
                String flag = jsonObject.getString("MSGTY");

                System.out.println("返回值：" + gmResult);

//        2.bs_ven_sap  导入本系统
                if ("S".equals(flag)) {

                    List<Line> lineList = JSONArray.parseArray(jsonObject.getString("Line"), Line.class);

                    if (!lineList.isEmpty()) {


                        BusinessSupplier businessSupplier = new BusinessSupplier();
                        //business_supplier
                        List<BusinessSupplier> list =
                                businessSupplierService.selectBusinessSupplierList(businessSupplier);

                        // 将 list 转换为 Set，方便后续查找
                        Set<String> existingCodes = list.stream()
                                .map(BusinessSupplier::getSapCode)
                                .collect(Collectors.toSet());

                        List<Line> nonExistingData = lineList.stream()
                                .filter(a -> !existingCodes.contains(a.getPARTNER() )) // 通过 Set 快速查找
                                .collect(Collectors.toList());

                        if (!nonExistingData.isEmpty()) {
                            List<BusinessSupplier> suppliers =
                                    new ArrayList<>();
                            for (Line line : nonExistingData) {
                                BusinessSupplier supplier =
                                        new BusinessSupplier();
                                supplier.setId(IdUtils.fastUUID());
                                supplier.setSapCode(line.getPARTNER())
                                        .setName(line.getNAME_ORG1())
                                        .setDeptId(Long.valueOf(line.getBUKRS()));
                                supplier.setType("C".equals(line.getBPROLE())?"1":"0")
                                        .setCreateTime(new Date());

                                suppliers.add(supplier);

                            }
                            businessSupplierService.saveBatch(suppliers);
                        }

                    }
                    //                    修改sap同步时间
                    sapupMapper.update("ven", nowDate, sapCode);
                }
            }

        }
    }
 
    // 等待线程池任务完成并优雅关闭线程池
    private void shutdownAndAwaitTermination(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
