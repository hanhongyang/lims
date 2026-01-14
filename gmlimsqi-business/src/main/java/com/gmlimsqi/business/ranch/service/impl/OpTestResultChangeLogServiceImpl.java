package com.gmlimsqi.business.ranch.service.impl;

import com.gmlimsqi.business.ranch.domain.OpTestResultChangeLog;
import com.gmlimsqi.business.ranch.dto.changelog.ResultChangeLogQueryDTO;
import com.gmlimsqi.business.ranch.mapper.OpTestResultChangeLogMapper;
import com.gmlimsqi.business.ranch.service.IOpTestResultChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpTestResultChangeLogServiceImpl implements IOpTestResultChangeLogService {

    private final OpTestResultChangeLogMapper opTestResultChangeLogMapper;

    /**
     * 根据id查询
     *
     * @param id 日志id
     * @return 日志
     */
    @Override
    public OpTestResultChangeLog getById(Long id) {
        return opTestResultChangeLogMapper.selectById(id);
    }

    /**
     * 查询列表
     *
     * @return 日志列表
     */
    @Override
    public List<OpTestResultChangeLog> list(ResultChangeLogQueryDTO query) {
        return opTestResultChangeLogMapper.selectList(query);
    }

    /**
     * 保存
     *
     * @param opTestResultChangeLog 保存日志
     */
    @Override
    public void save(OpTestResultChangeLog opTestResultChangeLog) {
        // 自动填充创建信息
        opTestResultChangeLog.fillCreateInfo();
        // 保存
        opTestResultChangeLogMapper.insertOpTestResultChangeLog(opTestResultChangeLog);
    }
}
