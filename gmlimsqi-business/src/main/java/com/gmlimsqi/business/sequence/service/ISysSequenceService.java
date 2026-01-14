package com.gmlimsqi.business.sequence.service;


import com.gmlimsqi.business.sequence.domain.SysSequence;

/**
 * 编号生成器
 * @author niu
 * @date 2021/07/26
 */
public interface ISysSequenceService {
    /**
     * 编号生成 乐观锁实现
     * @param sequence
     * @return
     */
    String genCodeBySequenceWithVersion(SysSequence sequence);

    /**
     * 编号生成 悲观锁实现
     * @param sequence
     * @return
     */
    String genCodeBySequence(SysSequence sequence);

    String genCodeBySequenceByStarchstir(SysSequence sequence);

}
