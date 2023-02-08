package com.edukg.open.config;

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
public class LimitRequestForQaAspect {

    private static ConcurrentHashMap<String, ExpiringMap<String, Integer>> book = new ConcurrentHashMap<>();

    // 定义切点
    // 让所有有@LimitRequest注解的方法都执行切面方法
    @Pointcut("@annotation(limitRequestForQa)")
    public void excudeService(LimitRequestForQa limitRequestForQa) {
    }

    @Around("excudeService(limitRequestForQa)")
    public Object doAround(ProceedingJoinPoint pjp, LimitRequestForQa limitRequestForQa) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        //读取session中的用户
        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user == null) {
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

        if (uCount >= 1) {
            throw new BusinessException(-1, "请求频率太快啦，一秒后再试");
        }
        if (uCount == 0) { // 第一次请求时，设置有效时间
            uc.put(user.getPhone(), uCount + 1, ExpirationPolicy.CREATED, limitRequestForQa.time(), TimeUnit.MILLISECONDS);
        } else { // 未超过次数， 记录加一
            uc.put(user.getPhone(), uCount + 1);
        }
        book.put(user.getPhone(), uc);

        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();

        return result;
    }


}