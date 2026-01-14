package com.gmlimsqi.business.basicdata.mapper;

import java.util.Date;
import java.util.List;
import com.gmlimsqi.business.basicdata.domain.UserLabtestItem;
import com.gmlimsqi.business.basicdata.vo.UserLabtestItemVo;
import org.apache.ibatis.annotations.Param;

/**
 * 检测项目对应人员Mapper接口
 * 
 * @author hhy
 * @date 2025-08-08
 */
public interface UserLabtestItemMapper 
{
    /**
     * 查询检测项目对应人员
     * 
     * @param userLabtestItemId 检测项目对应人员主键
     * @return 检测项目对应人员
     */
    public UserLabtestItem selectUserLabtestItemByUserLabtestItemId(String userLabtestItemId);

    /**
     * 查询检测项目对应人员列表
     * 
     * @param userLabtestItem 检测项目对应人员
     * @return 检测项目对应人员集合
     */
    public List<UserLabtestItemVo> selectUserLabtestItemList(UserLabtestItem userLabtestItem);
    /**
     * 分页查询用户ID列表
     */
    List<String> selectUserIdsPage(UserLabtestItem userLabtestItem);

    /**
     * 根据用户ID列表查询用户检测项目详情
     */
    List<UserLabtestItem> selectUserLabtestItemListByUserIds(@Param("userIds") List<String> userIds,
                                                               @Param("userLabtestItem") UserLabtestItem userLabtestItem);
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
     * 用userId修改is_delete=0
     *
     * @param userId
     * @param updateTime
     * @param updateUserId
     */
    public void updateDeleteFlagByUserId(@Param("userId") String userId,@Param("updateTime") Date updateTime,@Param("updateUserId") String updateUserId);

    public int updateDeleteFlagById(@Param("userLabtestItemId") String userLabtestItemId,@Param("updateTime") Date updateTime,@Param("updateUserId") String updateUserId);


    /**
     * 批量插入用户检测项目关联关系
     * @param list 用户检测项目列表
     * @return 插入记录数
     */
    public  int batchInsertUserLabtestItem(List<UserLabtestItem> list);

    public List<UserLabtestItem> selectUserLabtestItemByUserId(String userId);
    public List<String> selectCheckItemIdsByUserId(String userId);
}
