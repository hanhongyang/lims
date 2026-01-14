package com.gmlimsqi.business.basicdata.service;

import java.util.List;
import com.gmlimsqi.business.basicdata.domain.UserLabtestItem;
import com.gmlimsqi.business.basicdata.vo.UserLabtestItemVo;

/**
 * 检测项目对应人员Service接口
 * 
 * @author hhy
 * @date 2025-08-08
 */
public interface IUserLabtestItemService 
{
    /**
     * 查询检测项目对应人员
     * 
     * @param UserLabtestItemId 检测项目对应人员主键
     * @return 检测项目对应人员
     */
    public UserLabtestItem selectUserLabtestItemByUserLabtestItemId(String UserLabtestItemId);

    /**
     * 查询检测项目对应人员列表
     * 
     * @param userLabtestItem 检测项目对应人员
     * @return 检测项目对应人员集合
     */
    public List<UserLabtestItemVo> selectUserLabtestItemList(UserLabtestItem userLabtestItem);
    /**
     * 查询指定用户的校对项目ID列表
     * @param userId 用户ID
     * @return 项目ID集合
     */
    List<String> selectCheckItemIdsByUserId(String userId);
    /**
     * 新增检测项目对应人员
     * 
     * @param userLabtestItem 检测项目对应人员
     * @return 结果
     */
    public int insertUserLabtestItem(UserLabtestItem userLabtestItem);

    /**
     * 修改检测项目对应人员
     * 
     * @param userLabtestItem 检测项目对应人员
     * @return 结果
     */
    public int updateUserLabtestItem(UserLabtestItem userLabtestItem);

    /**
     * 根据用户id查询对应
     * @param userId
     * @return
     */
    public UserLabtestItem selectUserLabtestItemByUserId(String userId);

    int updateDeleteFlagById(String userLabtestItemId);

    void updateDeleteFlagByUserId(String userId);
}
