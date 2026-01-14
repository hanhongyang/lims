package com.gmlimsqi.business.labtest.vo;


import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

//饲料化验单初始化信息
@Data
public class OpJczxFeedResultInitVo {

    private static final long serialVersionUID = 1L;

    /** 模板编号 */
    private String modelNo;

    /** 模版名称 */
    private String modelName;
    /** 结果录入方式 （1：结果式，2：过程式） */
    private String resultAddType;

    /** 温度 */
    private String temperature ;

    /** 湿度 */
    private String humidity ;
    /** 仪器名称 */
    private String instrumentName;

    /** 仪器编号 */
    private String instrumentNo;

    /** 检测依据 */
    private String testBasis;

    /** 检测地点 */
    private String testLocation ;
    private String labLocation ;
    /** 主要使用试剂/（配制）批号 */
    private String zysysj ;
    /** 30-60石油醚批号 */
    private String symph;
    /** xx溶液批号/浓度 */
    private String xxryph;
    /** EDTA标准滴定溶液批号 */
    private String EDTAbzddryph;
    /** 保留小数位 */
    private String decimalPlaces;

    /** 有效数字位 */
    private String significantDigits;
    /** 标准溶液配制批号 */
    private String bzrypzph;

    /** 饭钼酸铵溶液配制批号 */
    private String fmsarypzph;
    /** 试剂盒批号 */
    private String sjhph;
    /** xx/水配置批次 */
    private String xxspzpc;
    /** 滴定管编号 */
    private String ddgbh;
    /** xx第一次时间 */
    private String xxFirstTime;

    /** xx第二次时间 */
    private String xxSecondTime;




}
