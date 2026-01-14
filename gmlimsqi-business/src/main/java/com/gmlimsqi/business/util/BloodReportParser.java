package com.gmlimsqi.business.util;

import com.gmlimsqi.business.labtest.bo.BloodResultImportBo;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.utils.StringUtils;
import org.apache.poi.ss.usermodel.*;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 生化报告专用解析器
 */
public class BloodReportParser {

    public static List<BloodResultImportBo> parse(InputStream is) throws Exception {
        Workbook wb = WorkbookFactory.create(is);
        Sheet sheet = wb.getSheetAt(0);
        List<BloodResultImportBo> list = new ArrayList<>();

        // 1. 解析第一行获取委托单号 (例如："委托单号：25123001")
        Row metaRow = sheet.getRow(0);
        String entrustOrderNo = "";
        if (metaRow != null) {
            // 遍历前几个单元格找单号，防止格式微调
            for (int i = 0; i < 5; i++) {
                Cell cell = metaRow.getCell(i);
                String val = getCellValue(cell);
                if (val.contains("委托单号")) {
                    // 提取数字或字母：委托单号：25123001 -> 25123001
                    // 假设单号由数字字母组成
                    String reg = "[:：]\\s*([A-Za-z0-9]+)";
                    Pattern pattern = Pattern.compile(reg);
                    Matcher matcher = pattern.matcher(val);
                    if (matcher.find()) {
                        entrustOrderNo = matcher.group(1);
                    } else {
                        // 尝试直接截取
                        entrustOrderNo = val.replace("委托单号", "").replace("：", "").replace(":", "").trim();
                    }
                    break;
                }
            }
        }
        
        if (StringUtils.isEmpty(entrustOrderNo)) {
            throw new RuntimeException("解析失败：无法在第一行找到委托单号");
        }

        // 2. 解析第2行（索引1）获取表头映射关系
        Row headerRow = sheet.getRow(1);
        Map<Integer, Field> fieldMap = new HashMap<>();
        // 获取 BO 中所有带 @Excel 注解的字段
        Map<String, Field> excelMap = new HashMap<>();
        Field[] fields = BloodResultImportBo.class.getDeclaredFields();
        for (Field field : fields) {
            Excel annotation = field.getAnnotation(Excel.class);
            if (annotation != null) {
                excelMap.put(annotation.name(), field);
            }
        }

        int lastCellNum = headerRow.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            String headerName = getCellValue(headerRow.getCell(i)).trim();
            if (excelMap.containsKey(headerName)) {
                Field field = excelMap.get(headerName);
                field.setAccessible(true);
                fieldMap.put(i, field);
            }
        }
        // 3. 获取全局备注（修改：固定取第三行的最后一列）
        String globalRemark = "";
        Row thirdRow = sheet.getRow(2); // 第3行（索引为2）
        if (thirdRow != null) {
            // getLastCellNum() 返回的是总列数，最后一个单元格索引是 总列数-1
            int lastCellIndex = thirdRow.getLastCellNum() - 1;
            if (lastCellIndex >= 0) {
                Cell cell = thirdRow.getCell(lastCellIndex);
                String val = getCellValue(cell);

                // 排除空值和默认提示语
                if (StringUtils.isNotEmpty(val) ) {
                    globalRemark = val;
                }
            }
        }

        // 3. 从第5行（索引4）开始读取数据（跳过单位、参考范围）
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 4; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // 检查空行（如果没有牛号，视为无效行）
            // 假设牛号在第2列（索引1），具体位置根据 map 判断更稳妥，或者遍历判断全空
            boolean isEmptyRow = true;
            for (int k = 0; k < lastCellNum; k++) {
                if (StringUtils.isNotEmpty(getCellValue(row.getCell(k)))) {
                    isEmptyRow = false;
                    break;
                }
            }
            if (isEmptyRow) continue;

            BloodResultImportBo bo = new BloodResultImportBo();
            // 填充委托单号
            bo.setEntrustOrderNo(entrustOrderNo);
            // 【重点】将解析到的全局备注赋值给对象
            bo.setTestRemark(globalRemark);
            // 动态填充列数据
            for (Map.Entry<Integer, Field> entry : fieldMap.entrySet()) {
                int colIndex = entry.getKey();
                Field field = entry.getValue();
                String val = getCellValue(row.getCell(colIndex));
                
                // 将值设置给对象
                if (StringUtils.isNotEmpty(val)) {
                    field.set(bo, val);
                }
            }
            
            // 只有牛号不为空才添加
            if (StringUtils.isNotEmpty(bo.getSampleName())) {
                list.add(bo);
            }
        }
        
        return list;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                // 防止数字变成科学计数法，转成字符串，去除 .0
                double val = cell.getNumericCellValue();
                if (val == (long) val) {
                    return String.valueOf((long) val);
                }
                return String.valueOf(val);
            }
        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cellType == CellType.FORMULA) {
             try {
                 return String.valueOf(cell.getNumericCellValue());
             } catch (IllegalStateException e) {
                 return cell.getStringCellValue();
             }
        }
        return "";
    }
}