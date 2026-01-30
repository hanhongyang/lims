package com.gmlimsqi.business.labtest.bo;

import com.gmlimsqi.common.annotation.Excel;
import lombok.Data;

/**
 * PCR检测结果导入BO
 */
@Data
public class PcrResultImportBo {
    
    @Excel(name = "提取试剂盒批号")
    private String tqsjh;
    
    @Excel(name = "扩增试剂盒批号")
    private String kzsjh;
    
    @Excel(name = "序号")
    private String sequence;
    
    @Excel(name = "所属牧场")
    private String deptName;
    
    @Excel(name = "样品名称")
    private String sampleName;
    
    @Excel(name = "样品编号")
    private String sampleNo;
    
    @Excel(name = "委托备注")
    private String remark;

    
    @Excel(name = "金黄色葡萄球菌")
    private String item1;
    
    // 可以根据需要添加更多检测项目字段
    @Excel(name = "牛轮状病毒")
    private String item2;

    @Excel(name = "牛冠状病毒")
    private String item3;
    
    @Excel(name = "隐孢子虫")
    private String item4;
    
    @Excel(name = "肠毒素型细菌-大肠杆菌")
    private String item5;
    
    @Excel(name = "绿脓杆菌")
    private String item6;
    
    @Excel(name = "β-内酰胺酶抗性基因")
    private String item7;
    
    @Excel(name = "停乳链球菌")
    private String item8;

    @Excel(name = "克雷伯氏菌属")
    private String item9;

    @Excel(name = "牛支原体")
    private String item10;

    @Excel(name = "大肠杆菌")
    private String item11;


    @Excel(name = "牛病毒性腹泻病毒")
    private String item12;

    @Excel(name = "牛呼吸道合胞体病毒")
    private String item13;

    @Excel(name = "牛副流感病毒3型")
    private String item14;

    @Excel(name = "溶血性曼氏杆菌")
    private String item15;

    @Excel(name = "多杀巴斯德杆菌")
    private String item16;
    @Excel(name = "无乳链球菌")
    private String item17;
    @Excel(name = "产气荚膜梭菌")
    private String item18;
}