package com.gmlimsqi.business.basicdata.vo;

import com.gmlimsqi.business.basicdata.domain.BsInvbillItemStandard;
import com.gmlimsqi.business.basicdata.domain.UserLabtestItem;
import com.gmlimsqi.common.annotation.Excel;
import com.gmlimsqi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserLabtestItemVo  extends BaseEntity {

    /** 用户id */
    private String userId;
    /** 用户信息 **/
    /** 用户账号 */
    private String userName;

    /** 用户昵称 */
    @Excel(name = "人员")
    private String nickName;
    /** 用户信息 **/

    private List<UserLabtestItem> userLabtestItems;
}
