package com.gmlimsqi.business.ranch.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 饲料进厂库存检测结果DTO（映射存储过程返回的tem_info表字段）
 */
@Data // Lombok注解，自动生成getter/setter/toString，需引入lombok依赖
public class JckcTestVO implements Serializable {
    // 对应tem_info表字段
    private String op_sampling_plan_sample_id; // 样品ID
    private String sample_no;                  // 样品编号
    private String sampling_type;              // 取样类型
    private String invbill_name;               // 物料名称
    private String car_in_time;                // 车辆进厂时间
    private String driver_code;                // 司机编号
    private String test_time;                  // 检测时间
    private Integer iday;                      // 时间间隔（检测-进厂）
    private String hqm;                        // 黄曲霉毒素
    private String ots;                        // 呕吐毒素
    private String cmxt;                       // 玉米赤霉烯酮
    private String sjqa;                       // 三聚氰胺
    private String whether_qualified;          // 是否合格
    private String qyr;                        // 取样人（create_by）
    private String jcr;                        // 检测人
}