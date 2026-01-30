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

CREATE TABLE `op_pcr_entrust_order_change_log` (
                                                   `log_id` varchar(32) NOT NULL COMMENT '日志主键',
                                                   `business_id` varchar(32) NOT NULL COMMENT '业务ID(订单ID或样品ID)',
                                                   `business_type` varchar(20) NOT NULL COMMENT '业务类型(ORDER/SAMPLE)',
                                                   `field_key` varchar(50) DEFAULT NULL COMMENT '字段键值',
                                                   `field_name` varchar(50) DEFAULT NULL COMMENT '字段名称',
                                                   `old_value` text COMMENT '旧值',
                                                   `new_value` text COMMENT '新值',
                                                   `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
                                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                   PRIMARY KEY (`log_id`),
                                                   KEY `idx_biz_id` (`business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PCR委托单变更记录表';



CREATE TABLE `op_blood_entrust_order_change_log` (
                                                     `log_id` varchar(32) NOT NULL COMMENT '日志主键',
                                                     `business_id` varchar(32) NOT NULL COMMENT '业务ID(订单ID或样品ID)',
                                                     `business_type` varchar(20) NOT NULL COMMENT '业务类型(ORDER/SAMPLE)',
                                                     `field_key` varchar(50) DEFAULT NULL COMMENT '字段键值',
                                                     `field_name` varchar(50) DEFAULT NULL COMMENT '字段名称',
                                                     `old_value` text COMMENT '旧值',
                                                     `new_value` text COMMENT '新值',
                                                     `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
                                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                     PRIMARY KEY (`log_id`),
                                                     KEY `idx_blood_biz_id` (`business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='血样委托单变更记录表';

alter table op_blood_entrust_order
    add scypsl int default 0 null comment '删除样品数量';
alter table op_feed_entrust_order
    add scypsl int default 0 null comment '删除样品数量';

alter table op_pcr_entrust_order
    add scypsl int default 0 null comment '删除样品数量';




-- 1. 添加密码修改时间字段
ALTER TABLE sys_user ADD pwd_update_date datetime COMMENT '密码最后修改时间';

-- 2. 初始化现有数据（防止系统上线后老用户立刻全部被锁定）
-- 将现有用户的密码修改时间设为当前时间，或者设为他们的创建时间
UPDATE sys_user SET pwd_update_date = create_time WHERE pwd_update_date IS NULL;

drop table if exists bs_consumable_archive;
create table bs_consumable_archive (
                                       consumable_id       varchar(50)   not null comment '耗材ID' primary key,
                                       consumable_code     varchar(50)   not null comment '耗材编号',
                                       consumable_name     varchar(100)  not null comment '耗材名称',
                                       consumable_type     varchar(50)   null     comment '耗材分类(试剂/玻璃/耗材)',
                                       spec_model          varchar(100)  null     comment '规格型号',
                                       unit                varchar(20)   null     comment '计量单位',
                                       manufacturer        varchar(100)  null     comment '生产厂家',
                                       safety_stock        int           default 0 comment '安全库存预警值',
                                       storage_condition   varchar(100)  null     comment '存储条件(常温/冷藏)',
                                       remark              varchar(500)  null     comment '备注',

    -- 公共字段
                                       create_by           varchar(50)   null     comment '创建人',
                                       create_time         datetime      null     comment '创建时间',
                                       update_by           varchar(50)   null     comment '更新人',
                                       update_time         datetime      null     comment '更新时间',
                                       is_delete           char(1)       default '0' not null comment '是否删除'
) comment = '化验室耗材档案表';

drop table if exists bs_consumable_stock;
create table bs_consumable_stock (
                                     stock_id            varchar(50)   not null comment '库存ID' primary key,
                                     consumable_id       varchar(50)   not null comment '耗材ID',
                                     batch_no            varchar(50)   not null comment '批号',
                                     expiry_date         date          null     comment '有效期至',
                                     quantity            decimal(10,2) default 0.00 comment '当前库存数量',
                                     price               decimal(10,2) default 0.00 comment '入库单价(成本)',
                                     location            varchar(100)  null     comment '存放位置(柜号/层号)',

                                     create_time         datetime      null     comment '首次入库时间',
                                     update_time         datetime      null     comment '最后变动时间',

    -- 索引建议：查询某耗材总库存时用到
                                     index idx_stock_consumable (consumable_id)
) comment = '化验室耗材库存表';

drop table if exists bs_consumable_inbound;
create table bs_consumable_inbound (
                                       inbound_id          varchar(50)   not null comment '入库单ID' primary key,
                                       inbound_no          varchar(50)   not null comment '入库单号',
                                       inbound_date        date          not null comment '入库日期',
                                       inbound_type        varchar(20)   default '1' comment '入库类型(1:采购入库 2:赠送 3:盘盈)',
                                       supplier            varchar(100)  null     comment '供应商',
                                       operator            varchar(50)   null     comment '入库经办人',
                                       status              char(1)       default '0' comment '状态(0:草稿 1:已确认)',
                                       remark              varchar(500)  null     comment '备注',

                                       create_by           varchar(50)   null,
                                       create_time         datetime      null,
                                       update_by           varchar(50)   null,
                                       update_time         datetime      null,
                                       is_delete           char(1)       default '0'
) comment = '耗材入库单主表';
drop table if exists bs_consumable_inbound_detail;
create table bs_consumable_inbound_detail (
                                              detail_id           varchar(50)   not null comment '明细ID' primary key,
                                              inbound_id          varchar(50)   not null comment '入库单ID',
                                              consumable_id       varchar(50)   not null comment '耗材ID',
                                              batch_no            varchar(50)   null     comment '批号',
                                              expiry_date         date          null     comment '有效期',
                                              price               decimal(10,2) default 0.00 comment '单价',
                                              quantity            decimal(10,2) not null comment '入库数量',
                                              total_amount        decimal(10,2) default 0.00 comment '总金额',

                                              remark              varchar(200)  null
) comment = '耗材入库单明细表';

drop table if exists bs_consumable_outbound;
create table bs_consumable_outbound (
                                        outbound_id         varchar(50)   not null comment '出库单ID' primary key,
                                        outbound_no         varchar(50)   not null comment '出库单号',
                                        outbound_date       date          not null comment '出库日期',
                                        outbound_type       varchar(20)   default '1' comment '出库类型(1:领用出库 2:报损出库 3:盘亏)',
                                        dept_id             varchar(50)   null     comment '领用部门ID',
                                        receiver            varchar(50)   null     comment '领用人',
                                        status              char(1)       default '0' comment '状态(0:草稿 1:已确认)',
                                        remark              varchar(500)  null     comment '备注',

                                        create_by           varchar(50)   null,
                                        create_time         datetime      null,
                                        update_by           varchar(50)   null,
                                        update_time         datetime      null,
                                        is_delete           char(1)       default '0'
) comment = '耗材出库单主表';
drop table if exists bs_consumable_outbound_detail;
create table bs_consumable_outbound_detail (
                                               detail_id           varchar(50)   not null comment '明细ID' primary key,
                                               outbound_id         varchar(50)   not null comment '出库单ID',
                                               consumable_id       varchar(50)   not null comment '耗材ID',
                                               batch_no            varchar(50)   null     comment '出库批号(对应库存中的批号)',
                                               quantity            decimal(10,2) not null comment '出库数量',

                                               remark              varchar(200)  null
) comment = '耗材出库单明细表';

drop table if exists bs_consumable_return;
create table bs_consumable_return (
                                      return_id           varchar(50)   not null comment '退库单ID' primary key,
                                      return_no           varchar(50)   not null comment '退库单号',
                                      return_date         date          not null comment '退库日期',
                                      dept_id             varchar(50)   null     comment '退库部门ID',
                                      returner            varchar(50)   null     comment '退库人',
                                      status              char(1)       default '0' comment '状态(0:草稿 1:已确认)',
                                      remark              varchar(500)  null     comment '备注',

                                      create_by           varchar(50)   null,
                                      create_time         datetime      null,
                                      update_by           varchar(50)   null,
                                      update_time         datetime      null,
                                      is_delete           char(1)       default '0'
) comment = '耗材退库单主表';
drop table if exists bs_consumable_return_detail;
create table bs_consumable_return_detail (
                                             detail_id           varchar(50)   not null comment '明细ID' primary key,
                                             return_id           varchar(50)   not null comment '退库单ID',
                                             consumable_id       varchar(50)   not null comment '耗材ID',
                                             batch_no            varchar(50)   null     comment '批号(应与原出库批号一致或重新指定)',
                                             quantity            decimal(10,2) not null comment '退库数量',
                                             return_reason       varchar(200)  null     comment '退库原因',

                                             remark              varchar(200)  null
) comment = '耗材退库单明细表';
alter table sys_dept
    add pasture_code varchar(50) null comment '牧场简码';

-- -------------- 以上已执行 ------------------