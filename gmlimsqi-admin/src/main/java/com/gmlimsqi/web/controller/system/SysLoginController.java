package com.gmlimsqi.web.controller.system;

import java.util.*;

import com.gmlimsqi.common.annotation.Anonymous;
import com.gmlimsqi.common.exception.ServiceException;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.framework.config.dingtalk.config.DingTalkConfig;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.gmlimsqi.common.constant.Constants;
import com.gmlimsqi.common.core.domain.AjaxResult;
import com.gmlimsqi.common.core.domain.entity.SysMenu;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.core.domain.model.LoginBody;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.framework.web.service.SysLoginService;
import com.gmlimsqi.framework.web.service.SysPermissionService;
import com.gmlimsqi.system.service.ISysMenuService;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private DingTalkConfig dingTalkConfig;

    @Autowired
    private DdConfigSign ddConfigSign;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)  throws Exception
    {
        AjaxResult ajax = AjaxResult.success();

        // 生成令牌
        String token = "";

        if (StringUtils.isNotEmpty(loginBody.getPlatform()) &&
                loginBody.getPlatform().equals("DDMINI")) {
            if (ObjectUtils.isEmpty(loginBody.getCode())) {
                throw new ServiceException("授权码不能为空");
            }
            token = loginService.dingTalkLoginFree(loginBody.getCode());
        } else if (StringUtils.isEmpty(loginBody.getPlatform())) {
            // 生成令牌，传递true表示需要解密
            token = loginService.login(loginBody.getUsername(),
                    loginBody.getPassword(), loginBody.getCode(),
                    loginBody.getUuid(), true);  // 添加true参数
        } else {
            throw new ServiceException("登录类型不匹配!");
        }
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * h5微应用鉴权
     */
    @Anonymous
    @PostMapping("/h5Login")
    public AjaxResult h5Login(@RequestBody LoginBody loginBody) throws Exception {
        Map<String, String> map = new HashMap<>();
        String jsapiTicket = ddConfigSign.getJsapiTicket();
        long time = new Date().getTime();
        String nonceStr = loginBody.getNonceStr();
        String url = loginBody.getUrl();
        if (StringUtils.isEmpty(nonceStr)
                || StringUtils.isEmpty(url)) {
            throw new ServiceException("参数不能为空");
        }

        String sign = ddConfigSign.dingSign(jsapiTicket, nonceStr, time, url);

        map.put("agentId", String.valueOf(dingTalkConfig.getAgentId()));// 必填，微应用ID，后端返回
        map.put("corpId", dingTalkConfig.getCropId());//必填，企业ID，后端返回
        map.put("timeStamp", String.valueOf(time));// 必填，生成签名的时间戳
        map.put("nonceStr", nonceStr);// 必填，自定义固定字符串。前后端统一
        map.put("signature", sign);// 必填，签名，后端返回
        map.put("url", url);
        return AjaxResult.success(map);
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取钉钉配置
     */
    @Anonymous
    @GetMapping("/getDingConfig")
    public AjaxResult getDingConfig() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("corpId", dingTalkConfig.getCropId());
        map.put("clientId", dingTalkConfig.getAppKey());
        System.out.println("corpId"+dingTalkConfig.getCropId());
        return AjaxResult.success(map);
    }
}
