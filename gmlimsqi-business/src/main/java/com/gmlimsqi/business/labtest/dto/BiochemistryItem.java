package com.gmlimsqi.business.labtest.dto;

import lombok.Data;

@Data
public class BiochemistryItem {
    //生化项目值
    private String biochemistryItemType;
    //名称
    private String name;
    //单位
    private String unit;
    //参考范围
    private String range;
}
