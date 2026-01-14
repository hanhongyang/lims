package com.gmlimsqi.business.labtest.mapper;

import java.util.List;

import com.gmlimsqi.business.labtest.domain.OpJczxBloodResultBase;
import com.gmlimsqi.business.labtest.domain.OpJczxFeedResultBase;
import com.gmlimsqi.business.labtest.dto.OpJczxFeedResultBaseDto;
import com.gmlimsqi.business.labtest.vo.OpJczxFeedResultInitVo;
import com.gmlimsqi.business.labtest.vo.OpJczxFeedResultVo;
import org.apache.ibatis.annotations.Param;

/**
 * 检测中心饲料检测结果基础Mapper接口
 * 
 * @author hhy
 * @date 2025-09-25
 */
public interface OpJczxFeedResultBaseMapper 
{
    /**
     * 查询检测中心饲料检测结果基础
     * 
     * @param opJczxFeedResultBaseId 检测中心饲料检测结果基础主键
     * @return 检测中心饲料检测结果基础
     */
    public OpJczxFeedResultBase selectOpJczxFeedResultBaseByOpJczxFeedResultBaseId(String opJczxFeedResultBaseId);

    /**
     * 查询检测中心饲料检测结果基础列表
     * 
     * @param opJczxFeedResultBase 检测中心饲料检测结果基础
     * @return 检测中心饲料检测结果基础集合
     */
    public List<OpJczxFeedResultBase> selectOpJczxFeedResultBaseList(OpJczxFeedResultBaseDto dto);

    /**
     * 新增检测中心饲料检测结果基础
     * 
     * @param opJczxFeedResultBase 检测中心饲料检测结果基础
     * @return 结果
     */
    public int insertOpJczxFeedResultBase(OpJczxFeedResultBase opJczxFeedResultBase);

    /**
     * 修改检测中心饲料检测结果基础
     * 
     * @param opJczxFeedResultBase 检测中心饲料检测结果基础
     * @return 结果
     */
    public int updateOpJczxFeedResultBase(OpJczxFeedResultBase opJczxFeedResultBase);

    /**
     * 通过检测中心饲料检测结果基础主键更新删除标志
     *
     * @param opJczxFeedResultBaseId 检测中心饲料检测结果基础ID
     * @return 结果
     */
    public int updateDeleteFlagById(@Param("opJczxFeedResultBaseId")String opJczxFeedResultBaseId,@Param("updateUser")String updateUser);

    public OpJczxFeedResultInitVo getInitInfo(@Param("invbillCode") String invbillCode,@Param("itemId") String itemId,@Param("deptId") String deptId);
    public OpJczxFeedResultInitVo getInitInfo(@Param("itemId") String itemId,@Param("deptId") String deptId);

    public int saveCheck(OpJczxFeedResultBase base);

    public int saveExamine(OpJczxFeedResultBase base);
    public int backToSubmit(OpJczxFeedResultBase base);

    public OpJczxFeedResultBase selectCsfByParentId(@Param("parentId")String parentId);

    OpJczxFeedResultBase selectLastBaseByModelNo(@Param("modelNo")String modelNo);

    List<OpJczxFeedResultBase> selectJhwListList(OpJczxFeedResultBaseDto dto);

    OpJczxFeedResultVo getResultBySampleNo(OpJczxFeedResultBaseDto dto);

    /**
     * 根据状态和检测人查询数量
     * @param status 状态
     * @param userId 检测人id
     * @return 结果
     */
    int countFeedByStatusAndTestUser(@Param("status") String status,
                          @Param("testUser") String userId);

    /**
     * 根据状态列表和检测人查询数量
     * @param statusList 状态列表
     * @param uerId 检测人id
     * @return 结果
     */
    int countFeedByStatusListAndTestUser(@Param("statusList") List<String> statusList,
                              @Param("testUser") String uerId);
}
