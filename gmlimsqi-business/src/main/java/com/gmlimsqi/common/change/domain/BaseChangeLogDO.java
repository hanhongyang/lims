package com.gmlimsqi.common.change.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gmlimsqi.common.change.constant.ChangeLogConstant;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 通用变更表 - 变更日志 DO
 */
@Data
public class BaseChangeLogDO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 业务ID
     */
    private String bizId;

    /**
     * 操作类型：INSERT/UPDATE/DELETE
     * {@link ChangeLogConstant}
     */
    private String opType;

    /**
     * 变更原因
     */
    private String changeReason;

    /**
     * 变更前数据（仅存变更字段，JSON字符串）
     * <p>
     * 说明：
     * <ul>
     *   <li>只保存【本次发生变化的字段】的旧值</li>
     *   <li>key 为字段名（建议使用数据库字段名）</li>
     *   <li>value 为该字段变更前的值</li>
     * </ul>
     * <p>
     * 示例（UPDATE时）：
     * <pre>
     * {
     *   "acidity": "15",
     *   "acidity_photo": "/file/milk/acidity_old.jpg",
     *   "alcohol_type": "A"
     * }
     * </pre>
     * <p>
     * 示例（INSERT时，可为空或不传）：
     * <pre>
     * null
     * </pre>
     */
    private String oldData;

    /**
     * 变更后数据（仅存变更字段，JSON字符串）
     * <p>
     * 说明：
     * <ul>
     *   <li>只保存【本次发生变化的字段】的新值</li>
     *   <li>key 为字段名（建议使用数据库字段名）</li>
     *   <li>value 为该字段变更后的值</li>
     * </ul>
     * <p>
     * 示例（UPDATE时）：
     * <pre>
     * {
     *   "acidity": "17",
     *   "acidity_photo": "/file/milk/acidity_new.jpg",
     *   "alcohol_type": "B"
     * }
     * </pre>
     * <p>
     * 示例（INSERT时）：
     * <pre>
     * {
     *   "fat_percent": "3.6",
     *   "protein_percent": "3.2",
     *   "acidity": "16"
     * }
     * </pre>
     */
    private String newData;

    /**
     * 变更字段diff（字段 -> old/new，JSON字符串）
     * <p>
     * 说明：
     * <ul>
     *   <li>用于前端展示“字段改动对比”最友好</li>
     *   <li>只保存【本次发生变化的字段】</li>
     *   <li>每个字段对应 old / new</li>
     * </ul>
     * <p>
     * 示例：
     * <pre>
     * {
     *   "acidity": {"old":"15","new":"17"},
     *   "alcohol_photo":{"old":"/a.jpg","new":"/b.jpg"}
     * }
     * </pre>
     */
    private String diffData;

    /**
     * 操作人ID
     */
    private String createBy;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 操作人名称
     */
    private String createByName;

    /**
     * 填充创建信息（插入数据时调用）
     * 自动设置：createTime, createBy, createByName
     */
    public void fillCreateInfo() {
        // 获取当前时间
        Date now = DateUtils.getNowDate();
        // 获取当前用户ID
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 填充创建信息
        this.setCreateBy(loginUser.getUserId().toString());
        this.setCreateByName(loginUser.getUser().getNickName());
        this.setCreateTime(now);
    }
}