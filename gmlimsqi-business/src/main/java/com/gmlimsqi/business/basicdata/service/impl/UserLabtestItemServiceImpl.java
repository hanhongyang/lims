package com.gmlimsqi.business.basicdata.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.github.pagehelper.Page;
import com.gmlimsqi.business.basicdata.domain.LabtestItems;
import com.gmlimsqi.business.basicdata.vo.UserLabtestItemVo;
import com.gmlimsqi.business.util.UserCacheService;
import com.gmlimsqi.business.util.UserInfoProcessor;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmlimsqi.business.basicdata.mapper.UserLabtestItemMapper;
import com.gmlimsqi.business.basicdata.domain.UserLabtestItem;
import com.gmlimsqi.business.basicdata.service.IUserLabtestItemService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 检测项目对应人员Service业务层处理
 * 
 * @author hhy
 * @date 2025-08-08
 */
@Service
public class UserLabtestItemServiceImpl implements IUserLabtestItemService 
{
    @Autowired
    private UserLabtestItemMapper userLabtestItemMapper;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private UserInfoProcessor userInfoProcessor;

    /**
     * 查询检测项目对应人员
     * 
     * @param UserLabtestItemId 检测项目对应人员主键
     * @return 检测项目对应人员
     */
    @Override
    public UserLabtestItem selectUserLabtestItemByUserLabtestItemId(String UserLabtestItemId)
    {
        UserLabtestItem items = userLabtestItemMapper.selectUserLabtestItemByUserLabtestItemId(UserLabtestItemId);
        // 处理用户名
        if (items != null) {
            Map<String, String> usernameMap = userCacheService.batchGetUsernames(Collections.singleton(items.getCreateBy()));

            items.setCreateBy(usernameMap.get(items.getCreateBy()));
        }
        return items;
    }
    /**
     * 查询指定用户的校对项目ID列表 (user_type = '2')
     */
    @Override
    public List<String> selectCheckItemIdsByUserId(String userId) {
        return userLabtestItemMapper.selectCheckItemIdsByUserId(userId);
    }
    /**
     * 查询检测项目对应人员列表
     * 
     * @param userLabtestItem 检测项目对应人员
     * @return 检测项目对应人员
     */
    @Override
    public List<UserLabtestItemVo> selectUserLabtestItemList(UserLabtestItem userLabtestItem)
    {
        // 1. 先分页查询用户ID（这里会受到startPage()的影响）
        List<String> userIds = userLabtestItemMapper.selectUserIdsPage(userLabtestItem);

        if (userIds.isEmpty()) {
            // 【关键】保持返回类型为 Page，否则前端可能报错或显示异常
            if (userIds instanceof Page) {
                Page<UserLabtestItemVo> emptyPage = new Page<>();
                emptyPage.setTotal(0);
                return emptyPage;
            }
            return new ArrayList<>();
        }

        // 2. 根据用户ID列表查询详细数据（这里不受分页影响）
        List<UserLabtestItem> items = userLabtestItemMapper.selectUserLabtestItemListByUserIds(userIds, userLabtestItem);

        // 3. 按用户分组组装数据
        Map<String, UserLabtestItemVo> userMap = new LinkedHashMap<>();

        for (UserLabtestItem item : items) {
            String userId = item.getUserId();

            if (!userMap.containsKey(userId)) {
                // 创建用户VO对象
                UserLabtestItemVo userVo = new UserLabtestItemVo();
                userVo.setUserId(userId);
                userVo.setUserName(item.getUserName());
                userVo.setNickName(item.getNickName());

                userVo.setUserLabtestItems(new ArrayList<>());
                userMap.put(userId, userVo);
            }

            // 创建项目明细对象
            UserLabtestItem detailItem = new UserLabtestItem();
            detailItem.setUserLabtestItemId(item.getUserLabtestItemId());
            detailItem.setUserId(item.getUserId());
            detailItem.setLabtestItemsId(item.getLabtestItemsId());
            detailItem.setItemName(item.getItemName());
            detailItem.setItemCode(item.getItemCode());
            detailItem.setUserName(item.getUserName());
            detailItem.setNickName(item.getNickName());
            detailItem.setCreateBy(item.getCreateBy());
            detailItem.setCreateTime(item.getCreateTime());
            detailItem.setUpdateBy(item.getUpdateBy());
            detailItem.setUpdateTime(item.getUpdateTime());
            detailItem.setRemark(item.getRemark());
// 【新增】设置人员类型，供列表页标签显示
            detailItem.setUserType(item.getUserType());
            // 添加到对应用户的项目列表中
            userMap.get(userId).getUserLabtestItems().add(detailItem);
        }

        // 4. 批量处理用户名（创建人和更新人）
        List<UserLabtestItemVo> resultList = new ArrayList<>(userMap.values());
        userInfoProcessor.processBaseEntityUserInfo(resultList);

        // 4. 【关键修改】将 userIds 的分页信息“过继”给最终结果
        if (userIds instanceof Page) {
            Page<String> idPage = (Page<String>) userIds;
            Page<UserLabtestItemVo> resultPage = new Page<>();
            // 复制分页元数据
            resultPage.setTotal(idPage.getTotal());
            resultPage.setPageNum(idPage.getPageNum());
            resultPage.setPageSize(idPage.getPageSize());
            resultPage.setPages(idPage.getPages());

            // 添加实际数据
            resultPage.addAll(resultList);
            return resultPage;
        }

        return resultList;

    }

    /**
     * 新增检测项目对应人员
     * 
     * @param userLabtestItem 检测项目对应人员
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUserLabtestItem(UserLabtestItem userLabtestItem)
    {

        Date now = DateUtils.getNowDate();
        List<UserLabtestItem> batch = new ArrayList<>();

        // 【修改】优先使用 labtestItemsList (对象列表) 以获取 userType
        if (!CollectionUtils.isEmpty(userLabtestItem.getLabtestItemsList())) {
            for (LabtestItems item : userLabtestItem.getLabtestItemsList()) {
                UserLabtestItem ul = new UserLabtestItem();
                ul.setUserLabtestItemId(IdUtils.simpleUUID());
                ul.setUserId(userLabtestItem.getUserId());
                ul.setLabtestItemsId(item.getLabtestItemsId());
                ul.setRemark(userLabtestItem.getRemark());

                // 设置人员类型，如果前端没传则默认为 1 (检测人)
                String type = StringUtils.isNotEmpty(item.getUserType()) ? item.getUserType() : "1";
                ul.setUserType(type);

                ul.fillCreateInfo();
                ul.setCreateTime(now);
                batch.add(ul);
            }
        }
        // 兼容旧逻辑：如果只传了ID字符串
        else if (StringUtils.isNotEmpty(userLabtestItem.getLabtestItemsIdList())) {
            List<String> list = Arrays.asList(userLabtestItem.getLabtestItemsIdList().split(","));
            for (String s : list) {
                UserLabtestItem ul = new UserLabtestItem();
                ul.setUserLabtestItemId(IdUtils.simpleUUID());
                ul.setUserId(userLabtestItem.getUserId());
                ul.setLabtestItemsId(s);
                ul.setRemark(userLabtestItem.getRemark());
                ul.setUserType("1"); // 默认为检测人
                ul.fillCreateInfo();
                ul.setCreateTime(now);
                batch.add(ul);
            }
        }

        if (!batch.isEmpty()) {
            userLabtestItemMapper.batchInsertUserLabtestItem(batch);
        }
        return 1;
    }

    /**
     * 修改检测项目对应人员
     * 
     * @param userLabtestItem 检测项目对应人员
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUserLabtestItem(UserLabtestItem userLabtestItem)
    {
        // 1. 逻辑删除旧数据
        Date now = DateUtils.getNowDate();
        String updateUserId = String.valueOf(SecurityUtils.getUserId());
        userLabtestItemMapper.updateDeleteFlagByUserId(userLabtestItem.getUserId(), now, updateUserId);

        // 2. 新增数据 (逻辑同 insertUserLabtestItem)
        List<UserLabtestItem> batch = new ArrayList<>();

        // 【修改】优先使用 labtestItemsList
        if (!CollectionUtils.isEmpty(userLabtestItem.getLabtestItemsList())) {
            for (LabtestItems item : userLabtestItem.getLabtestItemsList()) {
                UserLabtestItem ul = new UserLabtestItem();
                ul.setUserLabtestItemId(IdUtils.simpleUUID());
                ul.setUserId(userLabtestItem.getUserId());
                ul.setLabtestItemsId(item.getLabtestItemsId());
                ul.setRemark(userLabtestItem.getRemark());

                String type = StringUtils.isNotEmpty(item.getUserType()) ? item.getUserType() : "1";
                ul.setUserType(type);

                ul.fillCreateInfo();
                ul.setCreateTime(now);
                batch.add(ul);
            }
        }
        else if (StringUtils.isNotEmpty(userLabtestItem.getLabtestItemsIdList())) {
            List<String> list = Arrays.asList(userLabtestItem.getLabtestItemsIdList().split(","));
            for (String s : list) {
                UserLabtestItem ul = new UserLabtestItem();
                ul.setUserLabtestItemId(IdUtils.simpleUUID());
                ul.setUserId(userLabtestItem.getUserId());
                ul.setLabtestItemsId(s);
                ul.setRemark(userLabtestItem.getRemark());
                ul.setUserType("1");
                ul.fillCreateInfo();
                ul.setCreateTime(now);
                batch.add(ul);
            }
        }

        if (!batch.isEmpty()) {
            userLabtestItemMapper.batchInsertUserLabtestItem(batch);
        }
        return 1;
    }


    /**
     * 查询检测项目对应人员
     *
     * @param userId
     * @return 检测项目对应人员
     */
    @Override
    public UserLabtestItem selectUserLabtestItemByUserId(String userId)
    {
        List<UserLabtestItem> items = userLabtestItemMapper.selectUserLabtestItemByUserId(userId);

        if (!items.isEmpty()) {
            // 批量处理用户名
            Set<String> userIds = items.stream()
                    .map(UserLabtestItem::getCreateBy)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Map<String, String> usernameMap = userCacheService.batchGetUsernames(userIds);

            UserLabtestItem result = new UserLabtestItem();
            result.setUserId(items.get(0).getUserId());
            result.setUserName(items.get(0).getUserName());
            result.setRemark(items.get(0).getRemark());
            result.setNickName(items.get(0).getNickName());
            result.setCreateTime(items.get(0).getCreateTime());
            result.setCreateBy(usernameMap.get(items.get(0).getCreateBy()));

            List<LabtestItems> labtestItemsList = new ArrayList<>();
            for (UserLabtestItem item : items) {
                LabtestItems labtestItems = new LabtestItems();
                labtestItems.setLabtestItemsId(item.getLabtestItemsId());
                labtestItems.setItemCode(item.getItemCode());
                labtestItems.setItemName(item.getItemName());
                // 【新增】回显人员类型
                labtestItems.setUserType(item.getUserType());

                labtestItemsList.add(labtestItems);
            }
            result.setLabtestItemsList(labtestItemsList);
            result.setUserLabtestItemId(items.get(0).getUserLabtestItemId());
            return result;
        } else {
            return null;
        }

    }

    @Override
    public int updateDeleteFlagById(String userLabtestItemId) {
        return userLabtestItemMapper.updateDeleteFlagById(userLabtestItemId,new Date(),SecurityUtils.getUserId().toString());
    }


    @Override
    public void updateDeleteFlagByUserId(String userId) {
        Date now = DateUtils.getNowDate();
        String updateUserId = String.valueOf(SecurityUtils.getUserId());
         userLabtestItemMapper.updateDeleteFlagByUserId(userId,now,updateUserId);
    }
}
