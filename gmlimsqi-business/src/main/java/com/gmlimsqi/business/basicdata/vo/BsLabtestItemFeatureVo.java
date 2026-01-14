package com.gmlimsqi.business.basicdata.vo;

import com.gmlimsqi.business.basicdata.domain.BsLabtestItemFeature;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class BsLabtestItemFeatureVo  extends BaseEntity {

    /** id */
    private String bsLabtestItemFeatureId;

    /** 项目id */
    private String itemId;

    /** 项目名称 */
    private String itemName;

    private List<BsLabtestItemFeature> featureList;
}
