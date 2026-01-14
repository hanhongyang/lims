package com.gmlimsqi.business.labtest.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测中心饲料检测结果基础对象 op_jczx_feed_result_base
 * 
 * @author hhy
 * @date 2025-09-25
 */
@Data
public class OpJczxFeedResultBase extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** 检测中心饲料检测结果基础表id */
    private String opJczxFeedResultBaseId;
    //父id（初水分化验单的原始单据id)
    private String parentId;
    //化验单号
    private String resultNo;

    /** 模板编号 */
    @Excel(name = "模板编号")
    private String modelNo;

    /** 模版名称 */
    @Excel(name = "模版名称")
    private String modelName;

    /** 项目id */
    @Excel(name = "项目id")
    private String itemId;
    private String itemName;
    /** 检测人id */
    @Excel(name = "检测人")
    private String testUser;
    //签字
    private String testUserSignature;
    /** 状态 */
    @Excel(name = "状态",readConverterExp = "1=开始化验,2=化验完成,3=校对完成,4=审核完成,5=作废,6=退回")
    private String status;

    /** 校对人id */
    @Excel(name = "校对人")
    private String checkUser;
    //签字
    private String checkUserSignature;
    /** 校对时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "校对时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkTime;

    /** 审核人id */
    @Excel(name = "审核人")
    private String examineUser;
    //签字
    private String examineUserSignature;
    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date examineTime;

    /** 结果录入方式 （1：结果式，2：过程式） */
    @Excel(name = "结果录入方式 ", readConverterExp = "1=：结果式，2：过程式")
    private String resultAddType;

    /** 检测日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date testDate;

    /** 检测完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检测完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date testEndTime;

    /** 温度 */
    @Excel(name = "温度")
    private String temperature;

    /** 湿度 */
    @Excel(name = "湿度")
    private String humidity;

    /** xx第一次时间 */
    @Excel(name = "xx第一次时间")
    private String xxFirstTime;

    /** xx第二次时间 */
    @Excel(name = "xx第二次时间")
    private String xxSecondTime;

    /** 仪器名称 */
    @Excel(name = "仪器名称")
    private String instrumentName;

    /** 仪器编号 */
    @Excel(name = "仪器编号")
    private String instrumentNo;

    /** 检测依据 */
    @Excel(name = "检测依据")
    private String testBasis;

    /** 检测地点 */
    @Excel(name = "检测地点")
    private String testLocation;

    /** 主要使用试剂/（配制）批号 */
    @Excel(name = "主要使用试剂")
    private String zysysj;

    /** 空白消耗盐酸标准溶液体积 */
    @Excel(name = "空白消耗盐酸标准溶液体积")
    private String kbxhys;

    /** 30-60石油醚批号 */
    @Excel(name = "30-60石油醚批号")
    private String symph;

    /** 处理前过滤质量 */
    @Excel(name = "处理前过滤质量")
    private String clqglzl;

    /** 处理后过滤质量 */
    @Excel(name = "处理后过滤质量")
    private String clhglzl;

    /** 校正因子 */
    @Excel(name = "校正因子")
    private String jzyz;

    /** xx溶液批号/浓度 */
    @Excel(name = "xx溶液批号/浓度")
    private String xxryph;

    /** 试样分解液的总体积 */
    @Excel(name = "试样分解液的总体积")
    private String syfjydztj;

    /** 移取试样液的体积 */
    @Excel(name = "移取试样液的体积")
    private String yqsyydtj;

    /** EDTA标准滴定溶液批号 */
    @Excel(name = "EDTA标准滴定溶液批号")
    private String EDTAbzddryph;

    /** 对钙的滴定度 */
    @Excel(name = "对钙的滴定度")
    private String dgdddd;

    /** 滴定管编号 */
    @Excel(name = "滴定管编号")
    private String ddgbh;

    /** 吸光度 */
    @Excel(name = "吸光度")
    private String xgd;

    /** 标准曲线方程及相关系数 */
    @Excel(name = "标准曲线方程及相关系数")
    private String bzqxfcjxgxs;

    /** 标准溶液配制批号 */
    @Excel(name = "标准溶液配制批号")
    private String bzrypzph;

    /** 饭钼酸铵溶液配制批号 */
    @Excel(name = "饭钼酸铵溶液配制批号")
    private String fmsarypzph;

    /** 处理前滤袋质量 */
    @Excel(name = "处理前滤袋质量")
    private String clqldzl;

    /** 干燥后滤袋坩埚质量 */
    @Excel(name = "干燥后滤袋坩埚质量")
    private String gzhldggzl;

    /** 灰化后滤袋坩埚质量 */
    @Excel(name = "灰化后滤袋坩埚质量")
    private String hhhldggzl;

    /** 试剂盒批号 */
    @Excel(name = "试剂盒批号")
    private String sjhph;

    /** xx/水配置批次 */
    @Excel(name = "xx/水配置批次")
    private String xxspzpc;

    /** 振荡器提取时间 */
    @Excel(name = "振荡器提取时间")
    private String zdqtqsj;

    /** 静置时间段 */
    @Excel(name = "静置时间段")
    private String jzsjd;

    /** 反应时间 */
    @Excel(name = "反应时间")
    private String fysj;

    /** 试样定容体积 */
    @Excel(name = "试样定容体积")
    private String sydrtj;

    /** 回归方程及相关系数 */
    @Excel(name = "回归方程及相关系数")
    private String hgfcjxgxs;

    /** v */
    @Excel(name = "萃取液总体积")
    private String v;
    /** v0 */
    private String v0;
    /** v1 */
    private String v1;
    /** 孵育时间 */
    @Excel(name = "孵育时间")
    private String fuyusj;

    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;
    private String sampleCount;
    private String a0;
}
