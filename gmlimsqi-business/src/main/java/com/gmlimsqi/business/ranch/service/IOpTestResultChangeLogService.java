package com.gmlimsqi.business.ranch.service;

import com.gmlimsqi.business.ranch.domain.OpTestResultChangeLog;
import com.gmlimsqi.business.ranch.dto.changelog.ResultChangeLogQueryDTO;

import java.util.List;

public interface IOpTestResultChangeLogService {

    public OpTestResultChangeLog getById(Long id);

    public List<OpTestResultChangeLog> list(ResultChangeLogQueryDTO query);

    public void save(OpTestResultChangeLog opTestResultChangeLog);

}
