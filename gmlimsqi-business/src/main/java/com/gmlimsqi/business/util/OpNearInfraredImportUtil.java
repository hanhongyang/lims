package com.gmlimsqi.business.util;

import com.gmlimsqi.business.labtest.domain.OpNearInfraredSummary;
import com.gmlimsqi.common.annotation.Excel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 近红外汇总数据导入工具类（修复表头匹配和解析错误）
 * 核心：增加调试日志，优化表头匹配和列索引逻辑，表头仅保留字母和数字（去特殊字符、空格、下划线，数字移至末尾）
 */
public class OpNearInfraredImportUtil {
    private static final Logger log = LoggerFactory.getLogger(OpNearInfraredImportUtil.class);

    // 支持的日期格式
    private static final List<String> DATE_FORMATS = Arrays.asList(
            "yyyy-MM-dd", "yyyy/MM/dd", 
            "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss",
            "MM/dd/yyyy", "dd/MM/yyyy", "yyyy年MM月dd日"
    );

    // 数值类型非法字符过滤正则（仅保留数字和小数点）
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[^0-9.]");

    /**
     * 导入MultipartFile文件（带详细日志）
     */
    public static List<OpNearInfraredSummary> importToEntity(MultipartFile file, String importBy, int errorHandleStrategy) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空！");
        }

        List<OpNearInfraredSummary> entities = new ArrayList<>();
        try {
            log.info("开始解析文件：{}", file.getOriginalFilename());
            Map<String, Object> fileData = readFileData(file);
            List<String> headers = (List<String>) fileData.get("headers");
            List<List<String>> dataRows = (List<List<String>>) fileData.get("dataRows");

            // 打印读取到的原始表头和清洗后表头（关键调试信息）
            List<String> cleanedHeaders = headers.stream().map(OpNearInfraredImportUtil::cleanHeader).collect(Collectors.toList());
            log.info("读取到原始表头（共{}列）：{}", headers.size(), headers);
            log.info("清洗后表头（共{}列）：{}", cleanedHeaders.size(), cleanedHeaders);

            // 构建表头-字段映射（带日志）
            Map<String, Field> headerFieldMap = buildHeaderFieldMap(headers);

            // 打印表头与字段的映射关系（关键调试信息）
            log.info("表头-字段映射关系：");
            headerFieldMap.forEach((originalHeader, field) -> {
                String cleanedHeader = cleanHeader(originalHeader);
                log.info("  原始表头【{}】→ 清洗后【{}】→ 字段【{}】（类型：{}）", 
                        originalHeader, cleanedHeader, field.getName(), field.getType().getSimpleName());
            });

            // 解析数据行
            log.info("开始解析数据行（共{}行）", dataRows.size());
            for (int i = 0; i < dataRows.size(); i++) {
                List<String> rowData = dataRows.get(i);
                int rowNum = i + 2; // 行号从2开始（表头为1行）
                // 【修复核心】：在解析前判断是否为空行
                if (isRowEmpty(rowData)) {
                    log.info("第{}行所有列均为空，视为无效空行，跳过", rowNum);
                    continue;
                }
                log.info("解析第{}行数据：{}", rowNum, rowData); // 打印原始行数据

                try {
                    OpNearInfraredSummary entity = parseRowToEntity(rowData, headerFieldMap, headers, rowNum, importBy, errorHandleStrategy);
                    entities.add(entity);
                    log.info("第{}行解析成功：{}", rowNum, entity.toString()); // 打印解析后的实体
                } catch (Exception e) {
                    if (errorHandleStrategy == 3) {
                        log.warn("第{}行数据错误，已跳过：{}", rowNum, e.getMessage());
                    } else {
                        throw new RuntimeException("第" + rowNum + "行导入失败", e);
                    }
                }
            }
            log.info("文件解析完成，共成功解析{}条数据", entities.size());
        } catch (Exception e) {
            log.error("文件导入失败", e);
            throw new RuntimeException("文件导入失败：" + e.getMessage(), e);
        }
        return entities;
    }
    /**
     * 【新增方法】判断行数据是否为空
     * 只要有一个单元格包含非空字符，即视为有效行
     */
    private static boolean isRowEmpty(List<String> rowData) {
        if (rowData == null || rowData.isEmpty()) {
            return true;
        }
        for (String cell : rowData) {
            if (cell != null && !cell.trim().isEmpty()) {
                return false; // 发现有效数据，不是空行
            }
        }
        return true; // 所有列都是空的
    }
    /**
     * 解析单行数据（修复列索引逻辑：用原始表头顺序获取索引）
     * @param headers 原始表头列表（用于精准获取列索引）
     */
    private static OpNearInfraredSummary parseRowToEntity(List<String> rowData, Map<String, Field> headerFieldMap,
                                                         List<String> headers, int rowNum, String importBy, int errorHandleStrategy) throws Exception {
        OpNearInfraredSummary entity = new OpNearInfraredSummary();
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setCreateBy(importBy);
        entity.setUpdateBy(importBy);
        entity.setImportTime(now);
        entity.setImportBy(importBy);

        for (Map.Entry<String, Field> entry : headerFieldMap.entrySet()) {
            String originalHeader = entry.getKey();
            Field field = entry.getValue();
            // 关键修复：通过原始表头列表获取列索引（保证顺序一致）
            int colIndex = headers.indexOf(originalHeader); 
            if (colIndex == -1) {
                log.warn("第{}行，原始表头【{}】在原始表头列表中未找到，跳过", rowNum, originalHeader);
                continue;
            }

            // 获取单元格值（处理列索引超出的情况）
            String cellValue = colIndex < rowData.size() ? rowData.get(colIndex) : "";
            String cleanedHeader = cleanHeader(originalHeader);
            log.debug("第{}行，原始表头【{}】（清洗后【{}】，索引{}）原始值：{}", 
                    rowNum, originalHeader, cleanedHeader, colIndex, cellValue);

            if (cellValue.isEmpty()) {
                log.debug("第{}行，原始表头【{}】（清洗后【{}】）值为空，跳过", rowNum, originalHeader, cleanedHeader);
                continue;
            }

            try {
                Object value = handleCellValueByType(cellValue, field.getType(), errorHandleStrategy, field.getName());
                field.set(entity, value);
                log.debug("第{}行，原始表头【{}】（清洗后【{}】）→ 字段【{}】赋值成功：{}", 
                        rowNum, originalHeader, cleanedHeader, field.getName(), value);
            } catch (Exception e) {
                String errorMsg = String.format("原始表头【%s】（清洗后【%s】）值【%s】转换失败", 
                        originalHeader, cleanedHeader, cellValue);
                if (errorHandleStrategy == 1 || errorHandleStrategy == 2) {
                    log.warn("第{}行{}，已按策略处理：{}", rowNum, errorMsg, e.getMessage());
                    field.set(entity, getDefaultValue(field.getType()));
                } else {
                    throw new RuntimeException(errorMsg, e);
                }
            }
        }
        return entity;
    }

    /**
     * 构建表头-字段映射（使用统一清洗规则匹配，保留原始表头用于索引定位）
     */
    private static Map<String, Field> buildHeaderFieldMap(List<String> headers) throws Exception {
        Map<String, Field> map = new HashMap<>();
        Class<OpNearInfraredSummary> clazz = OpNearInfraredSummary.class;
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldCleanedName = getFieldMatchName(field); // 字段的清洗后匹配名
            boolean matched = false;

            // 遍历原始表头，对每个表头先清洗再匹配
            for (String originalHeader : headers) {
                String headerCleanedName = cleanHeader(originalHeader); // 表头的清洗后名称
                // 用清洗后的名称匹配（仅字母+数字，无下划线/空格/特殊字符）
                if (fieldCleanedName.equals(headerCleanedName)) {
                    map.put(originalHeader, field);
                    log.info("表头匹配成功：原始表头【{}】→ 清洗后【{}】→ 字段【{}】", 
                            originalHeader, headerCleanedName, field.getName());
                    matched = true;
                    break;
                }
            }

            // 未匹配到的字段打印日志（含清洗后名称，方便调试）
            if (!matched) {
                log.warn("字段【{}】（清洗后匹配名：{}）未找到对应的表头，可能导致数据丢失", 
                        field.getName(), fieldCleanedName);
            }
        }
        return map;
    }

    /**
     * 表头/字段名统一清洗规则：
     * 1. 去除所有非字母、数字的字符（含特殊字符、空格、下划线）
     * 2. 若以数字开头，将开头所有数字移至末尾（例：123Name_456 → Name456123，@Test 789 → Test789）
     * 3. 转小写，确保大小写不敏感匹配
     */
    private static String cleanHeader(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        // 步骤1：仅保留字母和数字，去除其他所有字符（含空格、下划线、特殊字符）
        String cleaned = input.replaceAll("[^a-zA-Z0-9]", "").trim();
        // 步骤2：处理数字开头（提取开头所有数字，移至末尾）
        Matcher numMatcher = Pattern.compile("^\\d+").matcher(cleaned);
        if (numMatcher.find()) {
            String prefixNum = numMatcher.group(); // 开头的数字序列（例："123"）
            String suffix = cleaned.substring(prefixNum.length()); // 数字后的字母内容（例："Name"）
            cleaned = suffix + prefixNum; // 重组（例："Name123"）
        }
        // 步骤3：转小写，消除大小写差异
        return cleaned.toLowerCase();
    }

    /**
     * 获取字段的匹配名（使用统一清洗规则，与表头清洗逻辑一致）
     */
    private static String getFieldMatchName(Field field) {
        Excel excelAnno = field.getAnnotation(Excel.class);
        String rawName;
        // 优先使用Excel注解的name，无则使用字段名
        if (excelAnno != null && !excelAnno.name().isEmpty() && !"${comment}".equals(excelAnno.name())) {
            rawName = excelAnno.name();
        } else {
            rawName = field.getName();
        }
        // 使用统一的清洗规则处理，确保与表头清洗逻辑一致
        return cleanHeader(rawName);
    }

    /**
     * 读取文件数据（Excel/CSV），返回原始表头和数据行
     */
    private static Map<String, Object> readFileData(MultipartFile file) throws IOException, CsvException {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("文件名称为空！");
        }
        fileName = fileName.toLowerCase();
        log.info("开始读取文件：{}（类型：{}）", fileName, 
                fileName.endsWith(".csv") ? "CSV" : (fileName.endsWith(".xls") ? "Excel 97-2003" : "Excel 2007+"));

        if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            return readExcelData(file);
        } else if (fileName.endsWith(".csv")) {
            return readCsvData(file);
        } else {
            throw new IllegalArgumentException("不支持的文件格式！仅支持xls、xlsx、csv");
        }
    }

    /**
     * 读取Excel文件数据，返回原始表头和数据行
     */
    private static Map<String, Object> readExcelData(MultipartFile file) throws IOException {
        Map<String, Object> result = new HashMap<>(2);
        List<String> headers = new ArrayList<>();
        List<List<String>> dataRows = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = file.getOriginalFilename().toLowerCase().endsWith(".xls") 
                     ? new HSSFWorkbook(is) 
                     : new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            log.info("读取Excel工作表：{}（共{}行）", sheet.getSheetName(), sheet.getLastRowNum() + 1);

            if (sheet == null) {
                result.put("headers", headers);
                result.put("dataRows", dataRows);
                return result;
            }

            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                headers = getRowValues(headerRow);
                // 打印Excel原始表头和清洗后表头
                List<String> cleanedHeaders = headers.stream().map(OpNearInfraredImportUtil::cleanHeader).collect(Collectors.toList());
                log.info("Excel原始表头（行号：{}）：{}", headerRow.getRowNum() + 1, headers);
                log.info("Excel清洗后表头（行号：{}）：{}", headerRow.getRowNum() + 1, cleanedHeaders);
            }

            while (rowIterator.hasNext()) {
                Row dataRow = rowIterator.next();
                List<String> rowValues = getRowValues(dataRow);
                dataRows.add(rowValues);
                log.debug("读取Excel数据行（行号：{}）：{}", dataRow.getRowNum() + 1, rowValues);
            }
        }

        result.put("headers", headers);
        result.put("dataRows", dataRows);
        return result;
    }

    /**
     * 读取CSV文件数据，返回原始表头和数据行
     */
    private static Map<String, Object> readCsvData(MultipartFile file) throws IOException, CsvException {
        Map<String, Object> result = new HashMap<>(2);
        List<String> headers = new ArrayList<>();
        List<List<String>> dataRows = new ArrayList<>();

        // 注意：若CSV文件是GBK编码，需改为"GBK"
        try (InputStreamReader isr = new InputStreamReader(file.getInputStream(), "UTF-8");
             CSVReader reader = new CSVReader(isr)) {

            List<String[]> allRows = reader.readAll();
            log.info("读取CSV文件，共{}行", allRows.size());

            if (allRows.isEmpty()) {
                result.put("headers", headers);
                result.put("dataRows", dataRows);
                return result;
            }

            // 处理CSV表头
            String[] headerArr = allRows.get(0);
            for (String header : headerArr) {
                headers.add(header == null ? "" : header);
            }
            // 打印CSV原始表头和清洗后表头
            List<String> cleanedHeaders = headers.stream().map(OpNearInfraredImportUtil::cleanHeader).collect(Collectors.toList());
            log.info("CSV原始表头（共{}列）：{}", headers.size(), headers);
            log.info("CSV清洗后表头（共{}列）：{}", cleanedHeaders.size(), cleanedHeaders);

            // 处理CSV数据行
            for (int i = 1; i < allRows.size(); i++) {
                String[] dataArr = allRows.get(i);
                List<String> rowValues = new ArrayList<>();
                for (String data : dataArr) {
                    rowValues.add(data == null ? "" : data);
                }
                dataRows.add(rowValues);
                log.debug("读取CSV数据行（行号：{}）：{}", i + 1, rowValues);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("CSV编码错误（当前使用UTF-8，若文件是GBK请修改编码）", e);
            throw e;
        }

        result.put("headers", headers);
        result.put("dataRows", dataRows);
        return result;
    }

    /**
     * 按字段类型处理单元格值（含非法字符过滤）
     */
    private static Object handleCellValueByType(String cellValue, Class<?> fieldType, int strategy, String fieldName) throws ParseException {
        log.debug("处理字段【{}】（类型：{}），原始值：{}", fieldName, fieldType.getSimpleName(), cellValue);

        if (fieldType == String.class) {
            return cellValue;
        }

        if (fieldType == BigDecimal.class || fieldType == Long.class || fieldType == long.class) {
            if (strategy == 1) {
                cellValue = filterNumberIllegalChars(cellValue);
                log.debug("字段【{}】过滤后的值：{}", fieldName, cellValue);
                if (cellValue.isEmpty() || cellValue.equals(".")) {
                    throw new RuntimeException("过滤后无有效数值");
                }
            }

            if (fieldType == BigDecimal.class) {
                return new BigDecimal(cellValue);
            } else {
                return Long.parseLong(cellValue);
            }
        }

        if (fieldType == Date.class) {
            if (strategy == 1) {
                // 日期值过滤：仅保留数字、-、/、年、:、空格
                cellValue = cellValue.replaceAll("[^0-9\\-\\/年:\\s]", "");
                log.debug("字段【{}】（日期）过滤后的值：{}", fieldName, cellValue);
            }
            return parseDate(cellValue);
        }

        throw new UnsupportedOperationException("不支持的字段类型：" + fieldType.getName());
    }

    /**
     * 过滤数值类型的非法字符（仅保留数字和小数点）
     */
    private static String filterNumberIllegalChars(String value) {
        Matcher matcher = NUMBER_PATTERN.matcher(value);
        String filtered = matcher.replaceAll("");
        // 处理多个小数点的情况（仅保留第一个）
        if (filtered.indexOf('.') != filtered.lastIndexOf('.')) {
            filtered = filtered.substring(0, filtered.indexOf('.') + 1) 
                    + filtered.substring(filtered.indexOf('.') + 1).replace(".", "");
        }
        return filtered;
    }

    /**
     * 获取字段类型的默认值（用于错误处理策略1/2）
     */
    private static Object getDefaultValue(Class<?> fieldType) {
        if (fieldType == String.class) return "";
        if (fieldType == Long.class || fieldType == long.class) return null;
        if (fieldType == BigDecimal.class) return null;
        if (fieldType == Date.class) return null;
        return null;
    }

    /**
     * 解析日期字符串（支持多格式）
     */
    private static Date parseDate(String dateStr) throws ParseException {
        for (String format : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(format).parse(dateStr);
            } catch (ParseException e) {
                continue;
            }
        }
        throw new ParseException("日期格式不支持：" + dateStr + "，支持格式：" + DATE_FORMATS, 0);
    }

    /**
     * 获取Excel行的所有单元格值（转为字符串）
     */
    private static List<String> getRowValues(Row row) {
        List<String> values = new ArrayList<>();
        if (row == null) return values;

        // 遍历所有单元格（包括空单元格，避免数据错位）
        for (int i = 0; i <= row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            values.add(getCellStringValue(cell));
        }
        return values;
    }

    /**
     * 将Excel单元格值转为字符串（处理不同单元格类型）
     */
    private static String getCellStringValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
                } else {
                    // 处理数字型单元格（避免科学计数法，去掉末尾的.0）
                    String numStr = String.valueOf(cell.getNumericCellValue());
                    return numStr.endsWith(".0") ? numStr.replace(".0", "") : numStr.trim();
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // 处理公式单元格（获取计算结果）
                return getCellStringValue(cell.getSheet().getWorkbook().getCreationHelper()
                        .createFormulaEvaluator().evaluateInCell(cell));
            default:
                return "";
        }
    }
}