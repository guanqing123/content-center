package com.itmuch.contentcenter.auth;

import com.itmuch.contentcenter.security.SecurityException;
import com.itmuch.contentcenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/9 13:15
 **/
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckAuthAspect {

    private final JwtOperator jwtOperator;

    @Around("@annotation(com.itmuch.contentcenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) throws Throwable {
        checkToken();
        return point.proceed();
    }

    private void checkToken() {
        try {
            /** 1. 从header里面获取token */
            HttpServletRequest request = getHttpServletRequest();

            String token = request.getHeader("Authorization");

            /** 2.校验token是否合法&是否过期,如果不合法或已过期,直接抛异常;如果合法放行 */
            Boolean isValid = jwtOperator.validateToken(token);
            if (!isValid) {
                throw new SecurityException("Token不合法!");
            }

            /** 3.如何校验成功,那么就将用户的信息设置到requeset的attribute里面 */
            Claims claims = jwtOperator.getClaimsFromToken(token);

            request.setAttribute("id", claims.get("userId"));
            request.setAttribute("name", claims.get("name"));
        } catch (Throwable throwable) {
            throw new SecurityException("Token不合法!");
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return attributes.getRequest();
    }

    @Around("@annotation(com.itmuch.contentcenter.auth.CheckAuthorization)")
    public Object checkAuthorization(ProceedingJoinPoint point) throws Throwable {
        try {
            /** 1. 验证token是否合法 */
            this.checkToken();

            /** 2. 验证用户角色是否匹配 */
            HttpServletRequest request = getHttpServletRequest();
            String  name = (String) request.getAttribute("name");

            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);

            String value = annotation.value();

            if (!Objects.equals(name, value)) {
                throw new SecurityException("用户无权访问！");
            }

        } catch (Throwable throwable) {
            throw new SecurityException("用户无权访问！");
        }
        return point.proceed();
    }
}
