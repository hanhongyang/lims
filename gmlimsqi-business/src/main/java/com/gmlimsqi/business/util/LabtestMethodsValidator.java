package com.gmlimsqi.business.util;

import com.gmlimsqi.business.basicdata.domain.LabtestMethods;
import com.gmlimsqi.common.utils.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * 检测方法校验工具类
 */
public class LabtestMethodsValidator {
    /**
     * 校验温度湿度数据
     * @param dto 包含温度湿度数据的DTO
     * @return 校验错误信息，空字符串表示校验通过
     */
    public static String validate(LabtestMethods dto) {
        StringBuilder errorMsg = new StringBuilder();

        // 校验必输
        if (StringUtils.isBlank(dto.getTemperatureMax())) {
            errorMsg.append("温度最大值不能为空; ");
        }
        if (StringUtils.isBlank(dto.getTemperatureMin())) {
            errorMsg.append("温度最小值不能为空; ");
        }
        if (StringUtils.isBlank(dto.getHumidityMax())) {
            errorMsg.append("湿度最大值不能为空; ");
        }
        if (StringUtils.isBlank(dto.getHumidityMin())) {
            errorMsg.append("湿度最小值不能为空; ");
        }
        if (StringUtils.isEmpty(dto.getMethodName())) {
            errorMsg.append("方法名不能为空");
        }
        if (StringUtils.isEmpty(dto.getBsLabtestItemsId())) {
            errorMsg.append("检测项目不能为空");
        }
        if (CollectionUtils.isEmpty(dto.getLabtestMethodsFormulaList())) {
            errorMsg.append("检测公式不能为空");
        }
        if (CollectionUtils.isEmpty(dto.getLabtestMethodsAttributeList())) {
            errorMsg.append("检测属性不能为空");
        }

        // 如果任何字段为空，直接返回
        if (errorMsg.length() > 0) {
            return errorMsg.toString();
        }

        try {
            // 转换为数字并比较大小
            double tempMax = Double.parseDouble(dto.getTemperatureMax());
            double tempMin = Double.parseDouble(dto.getTemperatureMin());
            double humiMax = Double.parseDouble(dto.getHumidityMax());
            double humiMin = Double.parseDouble(dto.getHumidityMin());

            if (tempMin > tempMax) {
                errorMsg.append("温度最小值不能大于最大值; ");
            }
            if (humiMin > humiMax) {
                errorMsg.append("湿度最小值不能大于最大值; ");
            }

        } catch (NumberFormatException e) {
            errorMsg.append("温度湿度输入必须为有效数字; ");
        }

        return errorMsg.toString();
    }
}
