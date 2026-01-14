package com.gmlimsqi.framework.aspectj;

import com.alibaba.fastjson2.JSON;
import com.gmlimsqi.common.annotation.FunLog;
import com.gmlimsqi.common.core.domain.entity.SysUser;
import com.gmlimsqi.common.core.domain.model.LoginUser;
import com.gmlimsqi.common.enums.BusinessStatus;
import com.gmlimsqi.common.enums.HttpMethod;
import com.gmlimsqi.common.utils.SecurityUtils;
import com.gmlimsqi.common.utils.ServletUtils;
import com.gmlimsqi.common.utils.StringUtils;
import com.gmlimsqi.common.utils.ip.IpUtils;
import com.gmlimsqi.framework.manager.AsyncManager;
import com.gmlimsqi.framework.manager.factory.AsyncFactory;
import com.gmlimsqi.system.domain.OpFunLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 * @author EGP
 */
@Aspect
@Component
public class FunLogAspect {
    private static final Logger log = LoggerFactory.getLogger(FunLogAspect.class);
    
    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, FunLog controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }
    
    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, FunLog controllerLog, Exception e) {
        if (e != null) {
            handleLog(joinPoint, controllerLog, e, null);
        }
    }
    
    protected void handleLog(final JoinPoint joinPoint, FunLog controllerLog,
                             final Exception e, Object jsonResult) {
        try {
            // *========数据库日志=========*//
            OpFunLog funLog = new OpFunLog();
            
            if (controllerLog.register()) {
                // 获取当前的用户
//                LoginUser loginUser = SecurityUtils.getLoginUser();
                /*if (loginUser != null) {
                    funLog.setOperName(loginUser.getUsername());
                    SysUser currentUser = loginUser.getUser();
                    if (StringUtils.isNotNull(currentUser) && StringUtils.isNotNull(currentUser.getDept())) {
                        funLog.setDeptName(currentUser.getDept().getDeptName());
                        funLog.setOperId(String.valueOf(currentUser.getUserId()));
                        funLog.setDeptId(String.valueOf(currentUser.getDept().getDeptId()));
//                        funLog.setCompanyId(String.valueOf(currentUser.getCompanyId()));
//                        funLog.setCompanyName(currentUser.getCompanyName());
                    }
                }*/
            }
            
            funLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            funLog.setOperIp(ip);
            funLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            
            if (e != null) {
                funLog.setStatus(BusinessStatus.FAIL.ordinal());
                funLog.setErrorMsg(e.getMessage());
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            funLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            funLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, funLog, jsonResult);
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordOper(funLog));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }
    
    /**
     * 获取注解中对方法的描述信息
     *
     * @param log     日志
     * @param funLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, FunLog log, OpFunLog funLog, Object jsonResult) throws Exception {
        // 设置action动作
        funLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        funLog.setTitle(log.title());
//        设置描述
        funLog.setDescription(log.description());
//        系统名称
        funLog.setSystemName(log.systemName().name());
        
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, funLog);
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtils.isNotNull(jsonResult)) {
            funLog.setJsonResult(JSON.toJSONString(jsonResult));
        }
    }
    
    /**
     * 获取请求的参数，放到log中
     *
     * @param funLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, OpFunLog funLog) throws Exception {
        String requestMethod = funLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            funLog.setOperParam(params);
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            funLog.setOperParam(paramsMap.toString());
        }
    }
    
    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object o : paramsArray) {
                if (StringUtils.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        Object jsonObj = JSON.toJSON(o);
                        params.append(jsonObj.toString()).append(" ");
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return params.toString().trim();
    }
    
    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
