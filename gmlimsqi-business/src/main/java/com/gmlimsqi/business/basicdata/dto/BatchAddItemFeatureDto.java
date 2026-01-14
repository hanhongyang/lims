package com.gmlimsqi.business.basicdata.dto;

import com.gmlimsqi.business.basicdata.domain.BsLabtestItemFeature;
import lombok.Data;

import java.util.List;

@Data
public class BatchAddItemFeatureDto {

    /** 项目id */
    private String itemId;
    /** 备注 */
    private String remark;
    private List<String> featureIds;
}
