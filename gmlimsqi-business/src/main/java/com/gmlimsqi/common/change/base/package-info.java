/**
 * 通用变更基础包（base）
 *
 * <p>
 * 该包用于承载系统内“变更日志（Change Log）”相关的基础抽象能力，
 * 提供可复用的模板方法、通用字段比对能力与公共组件，避免业务模块中重复编写相同逻辑。
 * </p>
 *
 * <p>
 * 典型使用场景：
 * <ul>
 *     <li>奶样质检单、奶仓质检单、奶罐车质检单等业务表更新时记录变更日志</li>
 *     <li>多个业务模块需要记录 UPDATE/INSERT/DELETE 的差异字段变更日志</li>
 * </ul>
 * </p>
 *
 * <p>
 * 该包中的类一般不直接依赖具体业务对象（DO/Mapper），而是通过泛型抽象（T/LOG）实现复用。
 * 推荐业务模块通过继承 {@link com.gmlimsqi.common.change.base.BaseChangeLogService} 的方式使用。
 * </p>
 *
 * <pre>
 * 业务实现类示例：
 *
 * public class XxxChangeLogServiceImpl
 *         extends BaseChangeLogService&lt;BizDO, XxxChangeLogDO&gt; {
 *
 *     // 1. 实现字段比对 compare(oldData, newData)
 *     // 2. 实现业务主键 getBizId(data)
 *     // 3. 实现 buildLog(...)（设置业务主键字段，父类提供了buildBaseLog方法）
 *     // 4. 实现 doInsert(log)（写入数据库）
 * }
 * </pre>
 *
 * <p>
 * 该包通常包含：
 * <ul>
 *     <li>{@link com.gmlimsqi.common.change.base.BaseChangeLogService}：变更日志模板抽象类，封装校验、比对、JSON 转换、入库等流程</li>
 *     <li>通用字段比对方法：compareField</li>
 *     <li>{@link com.gmlimsqi.common.change.domain.ChangeCompareResult}：变更比对结果对象（old/new/diff Map 结构）</li>
 * </ul>
 * </p>
 *
 * <p>
 * 设计原则：
 * <ul>
 *     <li>通用能力下沉：base 只封装公共流程，不引入具体业务依赖</li>
 *     <li>业务实现上浮：业务模块只需关注“哪些字段参与变更日志”</li>
 *     <li>统一结构：日志统一输出 old_data / new_data / diff_data，便于前端展示与追溯</li>
 * </ul>
 * </p>
 */
package com.gmlimsqi.common.change.base;