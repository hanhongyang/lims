package com.gmlimsqi.business.disinfection.controller.vo;

import com.gmlimsqi.business.disinfection.domain.DisinfectionPool;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消毒池管理保存请求参数，用于创建和修改 Req
 *
 * @author yangjw
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DisinfectionSaveReqVo extends DisinfectionPool {

}
