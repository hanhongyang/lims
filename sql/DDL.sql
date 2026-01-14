ALTER TABLE `gmlimsqi`.`op_jczx_pcr_result_base`
    ADD COLUMN `bl_template_type` varchar(2) NULL COMMENT '8联模板类型 1中文2英文' AFTER `examine_pass_flag`;
ALTER TABLE `gmlimsqi`.`op_test_result_info`
    ADD COLUMN `sjt_file_url` varchar(255) NULL COMMENT '试剂条文件url' AFTER `delete_id`;
ALTER TABLE `gmlimsqi`.`bs_invbill_item_standard`
    MODIFY COLUMN `invbill_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '物料id（废弃）' AFTER `bs_invbill_item_standard_id`,
    MODIFY COLUMN `invbill_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物料编码' AFTER `invbill_id`;

ALTER TABLE `gmlimsqi`.`op_feed_entrust_order_sample`
    CHANGE COLUMN `invill_id` `invbill_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物料编码' AFTER `feed_entrust_order_id`;

ALTER TABLE `gmlimsqi`.`op_pcr_entrust_order_sample`
    CHANGE COLUMN `invill_id` `invbill_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物料id' AFTER `pcr_entrust_order_id`;


ALTER TABLE `gmlimsqi`.`op_jczx_test_model`
    ADD COLUMN `test_basis` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '检测依据' AFTER `delete_id`;

ALTER TABLE `gmlimsqi`.`op_test_result_info`
    MODIFY COLUMN `check_result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '判断结果 1合格2不合格3让步接收' AFTER `sjt_file_id`;

ALTER TABLE `gmlimsqi`.`op_test_result_info`
    ADD COLUMN `retest_flag` varchar(1) NULL COMMENT '复检标志' AFTER `sjt_file_url`;
ALTER TABLE `gmlimsqi`.`op_test_result_info`
    MODIFY COLUMN `retest_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '复检标志' AFTER `sjt_file_url`;

ALTER TABLE `gmlimsqi`.`op_sampling_plan`
    ADD COLUMN `sign_in_id` varchar(50) NULL COMMENT '签到id' AFTER `car_file_id`;
ALTER TABLE `gmlimsqi`.`op_sampling_plan`
    ADD COLUMN `is_release` varchar(1) NULL DEFAULT '0' COMMENT '是否放行' AFTER `sign_in_id`;

ALTER TABLE `gmlimsqi`.`bs_labtest_items`
    ADD COLUMN `zxbz` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行标准' AFTER `lab_location`,
    ADD COLUMN `test_model_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '化验单模板编号' AFTER `zxbz`,
    ADD COLUMN `test_model_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '化验单模板名称' AFTER `test_model_no`;




ALTER TABLE `gmlimsqi`.`op_jczx_feed_result_info`
    DROP INDEX `uk_order_item_id`;
alter table bs_contact_info
    add is_default_send_sample_user varchar(1) default '0' null comment '是否默认送样人';

ALTER TABLE `gmlimsqi`.`op_feed_entrust_order`
    ADD COLUMN `is_return` varchar(10) NULL DEFAULT '0' AFTER `execution_period`,
ADD COLUMN `return_reason` varchar(1000) NULL AFTER `is_return`,
ADD COLUMN `return_time` datetime NULL AFTER `return_reason`;

ALTER TABLE `gmlimsqi`.`op_pcr_entrust_order`
    ADD COLUMN `is_return` varchar(10) NULL DEFAULT '0',
ADD COLUMN `return_reason` varchar(1000) NULL AFTER `is_return`,
ADD COLUMN `return_time` datetime NULL AFTER `return_reason`;

ALTER TABLE `gmlimsqi`.`op_blood_entrust_order`
    ADD COLUMN `is_return` varchar(10) NULL DEFAULT '0' ,
ADD COLUMN `return_reason` varchar(1000) NULL AFTER `is_return`,
ADD COLUMN `return_time` datetime NULL AFTER `return_reason`;

ALTER TABLE `gmlimsqi`.`op_feed_entrust_order_sample`
    ADD COLUMN `producer_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生产企业' AFTER `report_id`;
ALTER TABLE `gmlimsqi`.`op_feed_entrust_order_sample`
    ADD COLUMN `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注' AFTER `producer_unit`;
ALTER TABLE `gmlimsqi`.`op_feed_entrust_order_sample`
    CHANGE COLUMN `remark` `sample_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注' AFTER `producer_unit`;

alter table op_sampling_plan_item
    add feature_name varchar(50) null comment '项目特性';

alter table op_test_result_info
    add feature_name varchar(50) null comment '项目特性';


-- ----------------------------
-- Table structure for bs_item_dept_config
-- 项目-部门-资源配置表
-- ----------------------------
DROP TABLE IF EXISTS `bs_item_dept_config`;
CREATE TABLE `bs_item_dept_config`  (
                                        `config_id` varchar(50) NOT NULL COMMENT '主键ID',
                                        `item_id` varchar(50) NOT NULL COMMENT '检测项目ID',
                                        `dept_id` bigint(20) NOT NULL COMMENT '部门ID',


                                        `instrument_id` varchar(50) DEFAULT NULL COMMENT '关联设备ID (若是地点配置则为空)',
                                        `location_id` varchar(50) DEFAULT NULL COMMENT '关联地点ID (若是设备配置则为空)',

                                        `is_delete` char(1) DEFAULT '0' COMMENT '是否删除',
                                        `create_by` varchar(50) DEFAULT '' COMMENT '创建人',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_by` varchar(50) DEFAULT '' COMMENT '更新人',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                        PRIMARY KEY (`config_id`),
                                        KEY `idx_idc_item_dept` (`item_id`, `dept_id`) USING BTREE COMMENT '加速查询某项目某部门下的配置'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '项目部门资源配置表';


ALTER TABLE `gmlimsqi`.`bs_labtest_feature`
    ADD COLUMN `allow_error` float(10) NULL COMMENT '允许误差' AFTER `qualitative_type`;

ALTER TABLE `gmlimsqi`.`bs_labtest_feature`
    ADD COLUMN `error_info` varchar(50) NULL COMMENT '误差判断值' AFTER `allow_error`;


ALTER TABLE `gmlimsqi`.`op_pcr_entrust_order_sample`
    ADD COLUMN `is_send` varchar(2) NULL DEFAULT '0' COMMENT '是否发送' AFTER `examine_time`;



ALTER TABLE `gmlimsqi`.`bs_item_dept_config`
    ADD COLUMN `instrument_name` varchar(255) NULL AFTER `update_time`,
    ADD COLUMN `instrument_code` varchar(255) NULL AFTER `instrument_name`,
    ADD COLUMN `location_name` varchar(255) NULL AFTER `instrument_code`;
ALTER TABLE `gmlimsqi`.`op_jczx_feed_report_base`
    ADD COLUMN `send_email_time` datetime NULL COMMENT '发送时间' AFTER `address`,
    ADD COLUMN `send_email_user_id` varchar(50) NULL COMMENT '发送人' AFTER `send_email_time`;


alter table bs_labtest_items
    add is_jhw varchar(1) default '0' null comment '是否近红外';

alter table op_jczx_feed_result_info
    add remark varchar(1000) null comment '备注';




-- 1. 创建序列表 (如果不存在)
CREATE TABLE IF NOT EXISTS `op_jhw_no` (
                                           `seq_name` varchar(50) NOT NULL COMMENT '序列名称',
    `current_val` int(11) NOT NULL COMMENT '当前值',
    `_increment` int(11) NOT NULL DEFAULT '1' COMMENT '步长',
    PRIMARY KEY (`seq_name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公共序列表';

-- 2. 插入对应的业务序列数据
-- 解释：将当前值设为 16999，下一次获取时 +1 变成 17000
INSERT INTO `op_jhw_no` (`seq_name`, `current_val`, `_increment`)
VALUES ('feed_jhw_no', 16999, 1)
    ON DUPLICATE KEY UPDATE `current_val` = 16999;

-- 3. 创建获取下一个序列值的函数 (nextval)
DELIMITER $$
DROP FUNCTION IF EXISTS `nextval`$$
CREATE FUNCTION `nextval`(seqName varchar(50)) RETURNS int(11)
                                                          DETERMINISTIC
BEGIN
    DECLARE value INTEGER;
    DECLARE inc INTEGER;

    -- 获取步长
SELECT _increment INTO inc FROM op_jhw_no WHERE seq_name = seqName;

-- 更新当前值 (原子操作)
UPDATE op_jhw_no SET current_val = current_val + inc WHERE seq_name = seqName;

-- 获取更新后的值
SELECT current_val INTO value FROM op_jhw_no WHERE seq_name = seqName;

-- 处理最大值逻辑 (超过999999则报错或重置，这里演示重置逻辑，可视情况修改)
IF value > 999999 THEN
UPDATE op_jhw_no SET current_val = 17000 WHERE seq_name = seqName;
SET value = 17000;
END IF;

RETURN value;
END$$
DELIMITER ;


ALTER TABLE bs_labtest_items
    ADD COLUMN sort_order int(11) DEFAULT 0 COMMENT '显示顺序';


alter table op_jczx_feed_result_info
    add file_url varchar(1000) null;
alter table op_jczx_feed_result_info
    add file_id varchar(50) null;

alter table op_feed_entrust_order_sample
    add is_retest varchar(1) default '0' null comment '复检';

-- 修改表结构，增加 user_type 字段
-- 1=检测人(默认), 2=校对人
ALTER TABLE bs_user_labtest_item
    ADD COLUMN user_type CHAR(1) DEFAULT '1' COMMENT '人员类型:1-检测人,2-校对人' AFTER bs_labtest_items_id;

-- (可选) 更新现有数据为检测人
UPDATE bs_user_labtest_item SET user_type = '1' WHERE user_type IS NULL;

alter table op_blood_entrust_order_sample
    add a_sjph varchar(50) null comment 'A型试剂批号';

alter table op_blood_entrust_order_sample
    add o_sjph varchar(50) null comment 'O型试剂批号';

alter table op_jczx_blood_result_info
    add a_sjph varchar(50) null comment 'A型试剂批号';

alter table op_jczx_blood_result_info
    add o_sjph varchar(50) null comment 'O型试剂批号';

alter table op_jczx_feed_result_info
    add jcjgfw varchar(50) null comment '检测结果范围';

alter table op_feed_entrust_order_sample
    add return_reason varchar(1000) null comment '退回原因';

alter table op_blood_entrust_order_sample
    add dnh varchar(50) null comment '犊牛号';

alter table op_blood_entrust_order_sample
    add bw varchar(50) null comment '部位';


alter table op_blood_entrust_order_sample
    add sn varchar(50) null comment 'S-N值';


alter table op_blood_entrust_order_sample
    add pdjg varchar(50) null comment '判定结果';

alter table op_jczx_blood_report_info
    add dnh varchar(50) null comment '犊牛号';

alter table op_jczx_blood_report_info
    add bw varchar(50) null comment '部位';


alter table op_jczx_blood_report_info
    add sn varchar(50) null comment 'S-N值';


alter table op_jczx_blood_report_info
    add pdjg varchar(50) null comment '判定结果';


alter table op_jczx_blood_result_info
    add dnh varchar(50) null comment '犊牛号';

alter table op_jczx_blood_result_info
    add bw varchar(50) null comment '部位';


alter table op_jczx_blood_result_info
    add sn varchar(50) null comment 'S-N值';


alter table op_jczx_blood_result_info
    add pdjg varchar(50) null comment '判定结果';
alter table op_jczx_feed_report_info
    add entrust_order_item_id varchar(50) null;

alter table op_jczx_feed_report_info
    add sample_no varchar(50) null;
alter table op_jczx_blood_report_info
    add sample_type varchar(50) null;

CREATE TABLE `op_feed_entrust_order_change_log` (
                                                    `log_id` varchar(32) NOT NULL COMMENT '主键',
                                                    `business_id` varchar(32) NOT NULL COMMENT '业务主键(关联委托单ID或新生成的样品ID)',
                                                    `business_type` varchar(20) NOT NULL COMMENT '类型(ORDER:委托单, SAMPLE:样品)',
                                                    `field_key` varchar(50) NOT NULL COMMENT '修改的字段名(如: materialName)',
                                                    `field_name` varchar(50) DEFAULT NULL COMMENT '字段中文名(如: 物料名称)',
                                                    `old_value` text COMMENT '旧值',
                                                    `new_value` text COMMENT '新值',
                                                    `change_type` varchar(10) DEFAULT 'UPDATE' COMMENT '类型(UPDATE, DELETE)',
                                                    `create_by` varchar(64) DEFAULT NULL,
                                                    `create_time` datetime DEFAULT NULL,
                                                    PRIMARY KEY (`log_id`),
                                                    KEY `idx_bus_id` (`business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务变更记录表';
alter table op_feed_entrust_order
    add producer_unit_id varchar(50) null;

alter table op_feed_entrust_order
    add producer_unit_name varchar(50) null;



alter table op_jczx_feed_report_base
    modify test_time varchar(100) null comment '检测时间';

-- -------------- 以上已执行 ------------------
alter table bs_labtest_feature
    add first_upper_limit DECIMAL null comment '第一上限';

alter table bs_labtest_feature
    add first_lower_limit DECIMAL null comment '第一下限';

alter table bs_labtest_feature
    modify upper_limit Decimal null comment '上限';

alter table bs_labtest_feature
    modify lower_limit Decimal null comment '下限';

alter table op_feed_entrust_order_sample
    add material_sample_no varchar(50) null comment '原料样品编号';

ALTER TABLE gmlimsqi.bs_labtest_feature
    MODIFY COLUMN upper_limit decimal(10, 3) NULL COMMENT '上限',
    MODIFY COLUMN lower_limit decimal(10, 3) NULL COMMENT '下限',
    MODIFY COLUMN first_upper_limit decimal(10, 3) NULL COMMENT '第一上限',
    MODIFY COLUMN first_lower_limit decimal(10, 3) NULL COMMENT '第一下限';
alter table op_blood_entrust_order_sample
    add mnh varchar(50) null comment '母牛号';

alter table op_jczx_blood_result_info
    add mnh varchar(50) null comment '母牛号';

alter table op_jczx_blood_report_info
    add mnh varchar(50) null comment '母牛号';