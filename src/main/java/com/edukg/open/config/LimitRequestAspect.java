package com.edukg.open.config;

import com.alibaba.fastjson.JSON;
import com.edukg.open.base.BusinessException;
import com.edukg.open.base.UserStatusEnum;
import com.edukg.open.user.entity.UserInfo;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LimitRequestAspect {

    private static ConcurrentHashMap<String, ExpiringMap<String, Integer>> book = new ConcurrentHashMap<>();

    // 定义切点
    // 让所有有@LimitRequest注解的方法都执行切面方法
    @Pointcut("@annotation(limitRequest)")
    public void excudeService(LimitRequest limitRequest) {
    }

    @Around("excudeService(limitRequest)")
    public Object doAround(ProceedingJoinPoint pjp, LimitRequest limitRequest) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
        UserInfo user = (UserInfo) session.getAttribute("user");
        System.out.println(JSON.toJSONString(user));
        if (user == null) {
            System.out.println("LimitRequestAspect!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            throw new BusinessException(-1, "请先登录");
        }

        if (user.getUserStatus().equals(UserStatusEnum.ADMIN.code)) {
            return "";
        }
        if (user.getUserStatus().equals(UserStatusEnum.LOCKED.code)) {
            throw new BusinessException(-1, "账户被锁，请联系管理员");
        }

        // 获得request对象
//        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
//        HttpServletRequest request = sra.getRequest();

        // 获取Map对象， 如果没有则返回默认值
        // 第一个参数是key， 第二个参数是默认值
        ExpiringMap<String, Integer> uc = book.getOrDefault(user.getPhone(), ExpiringMap.builder().variableExpiration().build());
        Integer uCount = uc.getOrDefault(user.getPhone(), 0);

        if (user.getUserStatus().equals(UserStatusEnum.SCOMMON.code)) {
            //已经审核过的用户，每天不超过一万次请求
            if (uCount >= 10000) {
                throw new BusinessException(-1, "接口请求超过次数");
            }
        } else {
            if (uCount >= limitRequest.count()) { // 超过次数，不执行目标方法
                throw new BusinessException(-1, "接口请求超过次数");
            }
        }
        if (uCount == 0) { // 第一次请求时，设置有效时间
//            /** Expires entries based on when they were last accessed */
//            ACCESSED,
//            /** Expires entries based on when they were created */
//            CREATED;
//            System.out.println(new Date() + "!!!!!!!!!!!!!!!!!");
            uc.put(user.getPhone(), uCount + 1, ExpirationPolicy.CREATED, limitRequest.time(), TimeUnit.MINUTES);
        } else { // 未超过次数， 记录加一
            uc.put(user.getPhone(), uCount + 1);
        }
        book.put(user.getPhone(), uc);

        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();

        return result;
    }


}