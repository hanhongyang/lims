package com.gmlimsqi.business.labtest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.business.labtest.domain.OpJczxFeedResultInfo;
import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OpJczxFeedResultBaseDto {


    private static final long serialVersionUID = 1L;


    /** 检测中心饲料检测结果基础表id */
    private String opJczxFeedResultBaseId;
    //父id（初水分化验单的原始单据id)
    private String parentId;
    //化验单号
    private String resultNo;
    private String sampleNo;

    private String entrustOrderItemId;
    // 添加日期范围查询参数
    private String beginTestEndTime;
    private String endTestEndTime;
    /** 检测人id */
    private String testUser;
    private String permissionType;
    private String currentUserId;
    private String currentUserNickName;
    //签字
    private String testUserSignature;
    /** 状态 */
    private String status;
    private String statusList;
    private String itemId;
    private String itemName;
    //是否开始化验
    private String isTest;
    /** 校对人id */
    private String checkUser;
    //签字
    private String checkUserSignature;
    /** 模板编号 */
    private String modelNo;

    /** 模版名称 */
    private String modelName;

    /** 审核人id */
    private String examineUser;
    //签字
    private String examineUserSignature;

    /** 结果录入方式 （1：结果式，2：过程式） */
    private String resultAddType;

    private String a0;



    /** 温度 */
    private String temperature;

    /** 湿度 */
    private String humidity;

    /** xx第一次时间 */
    private String xxFirstTime;

    /** xx第二次时间 */
    private String xxSecondTime;

    /** 仪器名称 */
    private String instrumentName;

    /** 仪器编号 */
    private String instrumentNo;

    /** 检测依据 */
    private String testBasis;

    /** 检测地点 */
    private String testLocation;

    /** 主要使用试剂/（配制）批号 */
    private String zysysj;

    /** 空白消耗盐酸标准溶液体积 */
    private String kbxhys;

    /** 30-60石油醚批号 */
    private String symph;

    /** 处理前过滤质量 */
    private String clqglzl;

    /** 处理后过滤质量 */
    private String clhglzl;

    /** 校正因子 */
    private String jzyz;

    /** xx溶液批号/浓度 */
    private String xxryph;

    /** 试样分解液的总体积 */
    private String syfjydztj;

    /** 移取试样液的体积 */
    private String yqsyydtj;

    /** EDTA标准滴定溶液批号 */
    private String EDTAbzddryph;

    /** 对钙的滴定度 */
    private String dgdddd;

    /** 滴定管编号 */
    private String ddgbh;

    /** 吸光度 */
    private String xgd;

    /** 标准曲线方程及相关系数 */
    private String bzqxfcjxgxs;

    /** 标准溶液配制批号 */
    private String bzrypzph;

    /** 饭钼酸铵溶液配制批号 */
    private String fmsarypzph;

    /** 处理前滤袋质量 */
    private String clqldzl;

    /** 干燥后滤袋坩埚质量 */
    private String gzhldggzl;

    /** 灰化后滤袋坩埚质量 */
    private String hhhldggzl;

    /** 试剂盒批号 */
    private String sjhph;

    /** xx/水配置批次 */
    private String xxspzpc;

    /** 振荡器提取时间 */
    private String zdqtqsj;

    /** 静置时间段 */
    private String jzsjd;

    /** 反应时间 */
    private String fysj;

    /** 试样定容体积 */
    private String sydrtj;

    /** 回归方程及相关系数 */
    private String hgfcjxgxs;

    /** v */
    private String v;
    /** v0 */
    private String v0;
    /** v1 */
    private String v1;
    /** 孵育时间 */
    private String fuyusj;


    private List<OpJczxFeedResultInfoDto> infoList;


}
