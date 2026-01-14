package com.gmlimsqi.business.basicdata.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.gmlimsqi.business.basicdata.domain.BusinessSupplier;
import com.gmlimsqi.business.basicdata.service.IBusinessSupplierService;
import com.gmlimsqi.business.basicdata.vo.SupplierSapVO;
import com.gmlimsqi.business.basicdata.manager.SupplierManager;
import com.gmlimsqi.business.basicdata.mapper.BusinessSupplierMapper;
import com.gmlimsqi.business.util.UserInfoProcessor;
import com.gmlimsqi.common.annotation.DataSource;
import com.gmlimsqi.common.core.domain.BaseEntity;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.enums.DataSourceType;
import com.gmlimsqi.common.enums.RoleEnum;
import com.gmlimsqi.common.enums.UserTypeEnum;
import com.gmlimsqi.common.enums.YesNoEnum;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.CollectionUtils;
import com.gmlimsqi.common.utils.DateUtils;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.spring.SpringUtils;
import com.gmlimsqi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.gmlimsqi.common.utils.PageUtils.startPage;

/**
 * 供应商Service业务层处理
 *
 * @author gmlimsqi
 * @date 2023-04-13
 */
@Service
@Slf4j
public class BusinessSupplierServiceImpl implements IBusinessSupplierService {
    @Autowired
    private BusinessSupplierMapper businessSupplierMapper;
    @Autowired
    private SupplierManager supplierManager;
    @Autowired
    private UserInfoProcessor userInfoProcessor;
    
    /**
     * 查询供应商
     *
     * @param id 供应商主键
     * @return 供应商
     */
    @Override
    public BusinessSupplier selectBusinessSupplierById(String id) {
        return businessSupplierMapper.selectBusinessSupplierById(id);
    }
    
    /**
     * 查询供应商列表
     *
     * @param businessSupplier 供应商
     * @return 供应商
     */
    @Override
    public List<BusinessSupplier> selectBusinessSupplierList(
            BusinessSupplier businessSupplier) {

        List<BusinessSupplier> businessSuppliers = businessSupplierMapper.selectBusinessSupplierList(businessSupplier);
        // 批量处理用户名（创建人和更新人）
        userInfoProcessor.processBaseEntityUserInfo(businessSuppliers);
        return businessSuppliers;
    }


    /**
     * 新增供应商
     *
     * @param businessSupplier 供应商
     * @return 结果
     */
    @Override
    public int insertBusinessSupplier(BusinessSupplier businessSupplier) {
        businessSupplier.setCreateTime(DateUtils.getNowDate());
        return businessSupplierMapper.insertBusinessSupplier(businessSupplier);
    }
    
    /**
     * 修改供应商
     *
     * @param businessSupplier 供应商
     * @return 结果
     */
    @Override
    public int updateBusinessSupplier(BusinessSupplier businessSupplier) {
        businessSupplier.fillUpdateInfo();
        return businessSupplierMapper.updateBusinessSupplier(businessSupplier);
    }
    
    /**
     * 批量删除供应商
     *
     * @param ids 需要删除的供应商主键
     * @return 结果
     */
    @Override
    public int deleteBusinessSupplierByIds(String[] ids) {
        return businessSupplierMapper.deleteBusinessSupplierByIds(ids);
    }
    
    /**
     * 删除供应商信息
     *
     * @param id 供应商主键
     * @return 结果
     */
    @Override
    public int deleteBusinessSupplierById(Long id) {
        return businessSupplierMapper.deleteBusinessSupplierById(id);
    }
    
    /**
     * 获取sap系统数据
     *
     * @return 数据
     */
    @Override
    public Integer info() {
        // 使用原子布尔值 CREATE 作为同步锁，防止重复执行数据同步操作
        if (!SupplierManager.CREATE.get()) {
            long l = System.currentTimeMillis(); // 记录开始时间，用于性能监控

            // 使用Spring AOP代理调用getCustomer方法，确保@DataSource注解生效（切换到从库数据源）
            List<SupplierSapVO> customer = SpringUtils.getAopProxy(this).getCustomer();

            // 设置同步标志为true，防止其他线程同时执行同步操作
            SupplierManager.CREATE.set(true);

            // 保存当前安全上下文，用于后续异步操作中的权限验证
            SecurityContext context = SecurityContextHolder.getContext();

            // 创建查询条件对象，查询主库中现有的所有供应商数据
            BusinessSupplier supplier = new BusinessSupplier();
            List<BusinessSupplier> businessSuppliers = businessSupplierMapper.selectBusinessSupplierList(supplier);

            // 将现有供应商数据转换为Map，key为"sapCode+type"，便于快速查找
            Map<String, BusinessSupplier> stringBusinessSupplierMap =
                    CollectionUtils.convertMap(businessSuppliers,
                            item -> (item.getSapCode() + item.getType()));

            // 创建需要新增的供应商列表
            List<BusinessSupplier> businessSupplierList = new ArrayList<>();

            // 遍历从从库获取的所有供应商数据
            for (SupplierSapVO supplierSapVO : customer) {
                // 生成当前从库数据的唯一标识key
                String key = supplierSapVO.getCcustomercode() + supplierSapVO.getType();

                // 检查条件：主库中不存在该供应商，或者类型不匹配（需要更新）
                if (!stringBusinessSupplierMap.containsKey(key) ||
                        !supplierSapVO.getType().equals(stringBusinessSupplierMap.get(key).getType())) {

                    // 创建新的供应商对象并填充数据
                    BusinessSupplier businessSupplier = new BusinessSupplier();
                    businessSupplier.setSapCode(supplierSapVO.getCcustomercode()); // SAP编码
                    businessSupplier.setName(supplierSapVO.getCcustomername());    // 供应商名称
                    businessSupplier.setType(supplierSapVO.getType());             // 类型（客户/供应商）
                    businessSupplier.setCifcarrier(supplierSapVO.getCifcarrier()); // 承运商标识

                    businessSupplierList.add(businessSupplier); // 添加到新增列表
                }
            }

            // 调用Manager层异步批量插入数据，传入安全上下文保持权限
            supplierManager.createSupplier(businessSupplierList, context);

            // 计算并记录数据获取耗时
            long l1 = System.currentTimeMillis();
            log.info("=================================数据获取时间" + (l1 - l));

            // 返回新增的数据数量
            return businessSupplierList.size();
        } else {
            // 如果同步操作正在进行中，抛出异常防止重复执行
            throw new ServiceException("数据获取中,请勿重复获取");
        }
    }


    @DataSource(value = DataSourceType.SLAVE)
    public List<SupplierSapVO> getCustomer() {
        List<SupplierSapVO> all = new ArrayList<>();
        List<SupplierSapVO> supplierSapVOS =
                businessSupplierMapper.selectSupplierListBySqlServer(null);
        if (CollUtil.isNotEmpty(supplierSapVOS)) {
            all.addAll(supplierSapVOS);
        }
        List<SupplierSapVO> clientSapVOS =
                businessSupplierMapper.selectClientListBySqlServer(null);
        if (CollUtil.isNotEmpty(clientSapVOS)) {
            all.addAll(clientSapVOS);
        }
        return all;
    }



    @Override
    @Transactional
    public void saveBatch(List<BusinessSupplier> businessSuppliers) {
        if (businessSuppliers == null || businessSuppliers.isEmpty()) {
            return;
        }
        // 可以添加分批次插入的逻辑，避免SQL过长
        int batchSize = 1000; // 每批插入的数量
        for (int i = 0; i < businessSuppliers.size(); i += batchSize) {
            List<BusinessSupplier> batchList = businessSuppliers.subList(i, Math.min(i + batchSize, businessSuppliers.size()));
            businessSupplierMapper.insertBusinessSupplierBatch(batchList);
        }
    }

}
