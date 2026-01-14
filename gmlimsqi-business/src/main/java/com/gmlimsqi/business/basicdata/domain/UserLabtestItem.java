package com.gmlimsqi.business.basicdata.domain;

import lombok.Data;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 检测项目对应人员对象 bs_user_labtest_item
 * 
 * @author hhy
 * @date 2025-08-08
 */
@Data
public class UserLabtestItem extends BaseEntity implements BaseEntity.CreateAware, BaseEntity.UpdateAware
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String userLabtestItemId;

    /** 用户id */
    private String userId;

    /** 检测项目id */
    private String labtestItemsId;

    private String deleteId;
    private String userType;

    //检测项目id数组
    private String labtestItemsIdList;

    //检测项目数组
    private List<LabtestItems> labtestItemsList;

    /** 项目信息 **/
    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 项目编码 */
    private String itemCode;
    /** 项目信息 **/

    /** 用户信息 **/
    /** 用户账号 */
    private String userName;

    /** 用户昵称 */
    @Excel(name = "人员")
    private String nickName;
    /** 用户信息 **/

    private String deptId;
}
