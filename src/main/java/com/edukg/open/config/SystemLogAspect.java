package com.edukg.open.config;


import com.alibaba.fastjson.JSON;

import com.edukg.open.user.entity.SysUserLog;
import com.edukg.open.user.entity.UserInfo;
import com.edukg.open.user.service.ISysUserLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

@Aspect
@Component
@SuppressWarnings("all")
public class SystemLogAspect {
    //    注入Service用于把日志保存数据库，实际项目入库采用队列做异步
    @Resource
    private ISysUserLogService sysUserLogService;
    //本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    //Controller层切点
    @Pointcut("@annotation(com.edukg.open.config.SystemControllerLog)")
    public void controllerAspect() {
    }

    /**
     * @Description 前置通知  用于拦截Controller层记录用户的操作
     * @date 2018年9月3日 10:38
     */

//    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
        UserInfo user = (UserInfo) session.getAttribute("user");
        String ip = getIP(request);
        try {
//            //*========控制台输出=========*//
//            System.out.println("==============前置通知开始==============");
//            System.out.println("请求方法" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
//            System.out.println("方法描述：" + getControllerMethodDescription(joinPoint));
//            System.out.println("请求人：" + user.getUserName());
//            System.out.println("请求ip：" + ip);

//            //*========数据库日志=========*//
            SysUserLog sysUserLog = new SysUserLog();
            sysUserLog.setApi(request.getRequestURL().toString());
            sysUserLog.setDes(getControllerMethodDescription(joinPoint));
            sysUserLog.setUserId(user.getId());
            sysUserLog.setIp(ip);
            sysUserLog.setRequest(JSON.toJSONString(request.getParameterMap()));
            //保存数据库
            sysUserLogService.save(sysUserLog);
        } catch (Exception e) {
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息：{}", e.getMessage());
        }
    }


    /**
     * @Description 获取注解中对方法的描述信息 用于Controller层注解
     * @date 2018年9月3日 上午12:01
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    public static String getIP(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
//        System.out.println("ip:" + ip);
        String headerIP = request.getHeader("x-real-ip");
        if (headerIP == null || "".equals(headerIP) || "null".equals(headerIP)) {
            headerIP = request.getHeader("x-forwarded-for");
        }
//        System.out.println("headerIP:" + headerIP);
        if (headerIP != null && !"".equals(headerIP) && !"null".equals(headerIP)) {
            ip = headerIP;
        }
        return ip;
    }

}