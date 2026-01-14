package com.gmlimsqi.common.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import lombok.Data;

/**
 * Entity基类
 * 
 * @author ruoyi
 */
@Data
public class BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 搜索值 */
    @JsonIgnore
    private String searchValue;

    /** 创建者 */
    @Excel(sort = 100,name = "制单人")
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(sort = 110,name = "制单时间",dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    @Excel(sort = 120,name = "修改人")
    private String updateBy;

    /** 更新时间 */
    @Excel(sort = 130,name = "修改时间",dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    private String remark;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public String getSearchValue()
    {
        return searchValue;
    }

    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

    /**
     * 填充创建信息（插入数据时调用）
     * 自动设置：createTime, createBy, updateTime, updateBy
     */
    public void fillCreateInfo() {
        String userId = String.valueOf(SecurityUtils.getUserId());
        Date now = DateUtils.getNowDate();

        // 检查并设置字段（通过接口确保安全）
        if (this instanceof CreateAware) {
            CreateAware entity = (CreateAware) this;
            entity.setCreateTime(now);
            entity.setCreateBy(userId);
        }
    }

    /**
     * 填充更新信息（更新数据时调用）
     * 自动设置：updateTime, updateBy
     */
    public void fillUpdateInfo() {
        String userId = String.valueOf(SecurityUtils.getUserId());
        Date now = DateUtils.getNowDate();

        if (this instanceof UpdateAware) {
            UpdateAware entity = (UpdateAware) this;
            entity.setUpdateTime(now);
            entity.setUpdateBy(userId);
        }
    }

    /**
     * 创建信息标记接口
     * 需要支持创建信息自动填充的实体必须实现此接口
     */
    public interface CreateAware {
        void setCreateTime(Date time);
        void setCreateBy(String userId);
        void setUpdateTime(Date time);
        void setUpdateBy(String userId);
    }

    /**
     * 更新信息标记接口
     * 需要支持更新信息自动填充的实体必须实现此接口
     */
    public interface UpdateAware {
        void setUpdateTime(Date time);
        void setUpdateBy(String userId);
    }
}
