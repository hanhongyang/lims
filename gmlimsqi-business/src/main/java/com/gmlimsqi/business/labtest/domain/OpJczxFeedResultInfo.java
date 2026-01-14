package com.gmlimsqi.business.labtest.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

/**
 * 检测中心饲料化验详情对象 op_jczx_feed_result_info
 * 
 * @author hhy
 * @date 2025-09-26
 */
@Data
public class OpJczxFeedResultInfo extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String opJczxFeedResultInfoId;
    private List<OpJczxFeedResultInfo> testItem;
    /** 基础表id */
    private String baseId;
    //是否复测
    private String isReset;
    /** 委托样品id */
    private String entrustOrderSampleId;
    /** 委托项目表id */
    private String entrustOrderItemId;
    private String opFeedEntrustOrderItemId;
    /** 委托单id */
    private String entrustOrderId;
    //样品编号
    private String sampleNo;
    //样品名称
    private String sampleName;
    private String itemName;
    private String itemCode;
    private String itemId;
    /** 称量皿号 */
    private String clmh;

    /** m0 */
    private String m0;

    /** m1 */
    private String m1;

    /** m2 */
    private String m2;

    /** m3 */
    private String m3;

    /** m4 */
    private String m4;

    /** x */
    private String x;

    /** x1 */
    private String x1;

    /** M1 */
    private String upperM1;

    /** m */
    private String m;

    /** cp */
    private String cp;

    /** v2 */
    private String v2;

    /** 消化管号 */
    private String xhgh;
    //签字
    private String testUserSignature;
    //签字
    private String checkUserSignature;
    /** w */
    private String w;

    /** 滤袋号 */
    private String ldh;

    /** ca */
    private String ca;

    /** ee */
    private String ee;

    /** 坩埚号 */
    private String ggh;

    /** p */
    private String p;

    /** a */
    private String a;

    /** 锥形瓶号 */
    private String zxph;

    /** 总碱量 */
    private String zjl;

    /** 管号 */
    private String gh;

    /** 对比样称量皿号 */
    private String compareClmh;

    /** 对比样m0 */
    private String compareM0;

    /** 对比样m1 */
    private String compareM1;

    /** 对比样m2 */
    private String compareM2;

    /** 对比样m3 */
    private String compareM3;

    /** 对比样m4 */
    private String compareM4;

    /** 对比样x */
    private String compareX;

    /** 对比样x1 */
    private String compareX1;

    /** 对比样M1 */
    private String compareUpperM1;

    /** 对比样消化管号 */
    private String compareXhgh;

    /** 对比样m */
    private String compareM;

    /** 对比样cp */
    private String compareCp;

    /** 对比样v2 */
    private String compareV2;

    /** 对比样w */
    private String compareW;

    /** 对比样ee */
    private String compareEe;

    /** 对比样滤袋号 */
    private String compareLdh;

    /** 对比样ca */
    private String compareCa;

    /** 对比样坩埚号 */
    private String compareGgh;

    /** 对比样a */
    private String compareA;

    /** 对比样锥形瓶号 */
    private String compareZxph;

    /** 对比样p */
    private String compareP;

    /** 对比样总碱量 */
    private String compareZjl;

    /** 对比样a0 */
    private String compareA0;

    /** 对比样a1 */
    private String compareA1;

    /** 对比样a2 */
    private String compareA2;

    /** 对比样试样吸光度A值 */
    private String compareSyxgdaz;

    /** 对比样样液号 */
    private String compareYyh;

    /** 对比样g */
    private String compareG;

    /** 对比样试样中wsc含量 */
    private String compareSyzwschl;

    /** 对比样淀粉含量 */
    private String compareDfhl;

    /** 检测日期 */
    private Date testDate;

    /** 平均值 */
    private String average;

    /** 绝对差值 */
    private String absoluteDifference;

    /** f */
    private String f;

    /** 烘干时间 */
    private String dryTime;

    /** 检测人 */
    private String testUser;

    /** 校对人 */
    private String checkUser;

    /** 鲜样中含量 */
    private String xyzhl;

    /** 甲醇水体积 */
    private String jcstj;

    /** 待测液体积 */
    private String dcytj;

    /** 检测结果 */
    private String jcjg;

    /** 检测结果范围 */
    private String jcjgfw;
    /** 试样吸光度A值 */
    private String syxgdaz;

    /** c */
    private String c;

    /** 样品中xx含量 */
    private String ypzxxhl;

    /** a0 */
    private String a0;

    /** a1 */
    private String a1;

    /** a2 */
    private String a2;

    /** 样液号 */
    private String yyh;

    /** n */
    private String n;

    /** g */
    private String g;

    /** 淀粉含量 */
    private String dfhl;

    /** 试样中wsc含量 */
    private String syzwschl;

    /** 乙醇水体积 */
    private String ycstj;

    /** 稀释液体积 */
    private String xsytj;

    /** 允许相差 */
    private String yxxc;

    /** 风干物质 */
    private String fgwz;

    /** 初水分 */
    private String csf;
    /** 删除id（0为未删除，删除则为id） */
    private String deleteId;
    private String v1;
    private String compareV1;
    private String compareC;
    private String v;
    private String v0;
    private String compareV;
    private String compareV0;

    private String remark;
    private String fileId;
    private String fileUrl;
}
