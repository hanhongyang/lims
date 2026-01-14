# LIMS 实验室信息管理系统 (后端 API)

## 📖 项目简介

本项目是 LIMS 系统的后端服务，基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 框架开发。系统专注于农牧行业的质量检测领域，实现了从**样品接收、任务分配、实验录入、报告生成**到**数据分析**的全链路闭环管理。

核心业务涵盖：**中心化验室检测（血样/饲料/PCR）、牧场检测计划、奶车/奶仓管理、以及与 SAP 和钉钉的深度集成**。

## 🛠 技术栈

| 技术组件 | 说明 | 版本/备注 |
| :--- | :--- | :--- |
| **JDK** | 开发环境 | 1.8+ |
| **Spring Boot** | 核心框架 | 2.5.x |
| **MyBatis** | ORM 框架 | 数据持久层 |
| **MySQL** | 数据库 | 5.7 / 8.0 |
| **Redis** | 缓存数据库 | 缓存 & 分布式锁 |
| **Quartz** | 任务调度 | 定时任务 (如 SAP 同步) |
| **Spring Security** | 安全框架 | JWT 认证授权 |
| **Druid** | 数据库连接池 | 阿里数据库连接池 |
| **Fastjson** | JSON 解析 | 阿里巴巴 JSON 库 |

## 📦 模块结构

项目采用多模块 Maven 架构：

```text
lims
├── gmlimsqi-admin       # [启动模块] Web 服务入口，Controller 层
├── gmlimsqi-business    # [核心模块] 核心业务逻辑 (LIMS 业务代码)
│   ├── basicdata        # 基础数据 (供应商、物料、检测项目)
│   ├── labtest          # 化验室业务 (血样、饲料、PCR、任务分配)
│   ├── ranch            # 牧场业务 (检测计划、结果录入)
│   ├── sap              # SAP 集成 (接口对接、日志)
│   ├── instrument       # 仪器设备管理
│   ├── report           # 报表生成模块
│   └── ...
├── gmlimsqi-common      # 通用模块 (工具类、枚举、注解)
├── gmlimsqi-framework   # 框架核心 (配置、拦截器、安全配置)
├── gmlimsqi-generator   # 代码生成模块
├── gmlimsqi-quartz      # 定时任务模块
├── gmlimsqi-system      # 系统模块 (用户、角色、菜单、部门)
└── sql                  # 数据库脚本

✨ 核心功能特性
1. 🧪 实验室全流程管理 (Lab Test)
委托单管理：支持饲料、血样、PCR、早孕等多种类型的检测委托创建。

任务调度：自动或手动将检测任务分配给具体的实验员。

结果录入：支持批量录入、Excel 导入以及部分设备数据的自动采集。

报告生成：基于检测结果自动生成 PDF/Excel 报告，支持多级审核流程。

2. 🔄 外部系统集成
SAP 对接：

自动同步 SAP 的供应商、物料、客户数据。

检测结果（质检数据）自动回传 SAP 系统。

钉钉集成 (DingTalkService)：

实现钉钉扫码登录。

消息通知与待办推送。

3. 🐄 牧场与奶源管理
牧场检测：制定牧场采样计划，跟踪检测进度。

奶车/奶仓：管理奶罐车清洗消毒记录、铅封记录及奶仓监控。

不合格品管理：针对检测不合格的样品触发异常处理流程。

4. ⚙️ 自动化与监控
定时任务：利用 Quartz 定时从外部系统拉取数据或生成日报。

设备监控：监控实验室温湿度及设备运行状态。

🚀 快速开始
1. 环境准备
确保本地已安装：

JDK 1.8

Maven 3.x

MySQL 5.7+

Redis 3.0+

2. 数据库初始化
创建数据库 ry-vue (或其他名称)。

依次执行 sql/ 目录下的 SQL 脚本：

ry_2024xxxx.sql (主结构数据)

quartz.sql (定时任务表)

DDL.sql / DML.sql (业务增量脚本)

3. 修改配置
修改 gmlimsqi-admin/src/main/resources/application-druid.yml 中的数据库连接信息：

YAML

spring:
  datasource:
    druid:
      master:
        url: jdbc:mysql://localhost:3306/your_db_name?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
        username: root
        password: your_password
4. 启动项目
运行 gmlimsqi-admin 模块下的 RuoYiApplication.java。 启动成功后，后端服务默认运行在 http://localhost:8080。

🔧 部署说明
使用 Maven 打包：

Bash

bin/package.bat
# 或者
mvn clean package -Dmaven.test.skip=true
打包完成后，在 gmlimsqi-admin/target 目录下会生成可执行的 jar 包。

Bash

java -jar gmlimsqi-admin.jar
