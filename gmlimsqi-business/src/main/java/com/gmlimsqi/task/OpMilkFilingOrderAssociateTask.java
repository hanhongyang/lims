package com.gmlimsqi.task;

import com.gmlimsqi.business.milkfillingorder.domain.OpMilkFillingOrder;
import com.gmlimsqi.business.milkfillingorder.mapper.OpMilkFillingOrderMapper;
import com.gmlimsqi.business.milkfillingorder.service.IOpMilkFillingOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 装奶单关联磅单定时任务
 */
@Slf4j
@Component("OpMilkFilingOrderAssociateTask")
public class OpMilkFilingOrderAssociateTask {

    @Autowired
    private IOpMilkFillingOrderService opMilkFilingOrderService;

    @Autowired
    private OpMilkFillingOrderMapper opMilkFillingOrderMapper;

    /**
     * 装奶单关联磅单定时任务
     * @throws ParseException
     * @throws IOException
     */
    public void syncOpMilkFilingOrderAssociate(){
        log.info("定时任务：装奶单与磅单关联");
        OpMilkFillingOrder opMilkFillingOrder = new OpMilkFillingOrder();
        opMilkFilingOrderService.associate(opMilkFillingOrder);
        log.info("定时任务结束：装奶单与磅单关联");
    }

    /**
     * 装奶单推送奶源定时任务
     */
    public void syncOpMilkFilingOrderPushMilkSource() {
        log.info("定时任务：装奶单推送奶源");
        // 查询所有已审核，已关联磅单，有净重，并且未推送的装奶单
        OpMilkFillingOrder opMilkFillingOrder = new OpMilkFillingOrder();
        List<OpMilkFillingOrder> opMilkFillingOrders = opMilkFillingOrderMapper.selectUnPushedMilkSourceOpMilkFillingOrderList(opMilkFillingOrder);
        if (opMilkFillingOrders != null &&!opMilkFillingOrders.isEmpty()){
            for (OpMilkFillingOrder order : opMilkFillingOrders) {
                opMilkFilingOrderService.pushMilkSource(order.getOpMilkFillingOrderId());
            }
        }
        log.info("定时任务结束：装奶单推送奶源");
    }

}
