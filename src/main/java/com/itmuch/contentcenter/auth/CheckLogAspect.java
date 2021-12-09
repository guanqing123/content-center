package com.itmuch.contentcenter.auth;

import com.itmuch.contentcenter.security.SecurityException;
import com.itmuch.contentcenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/9 13:15
 **/
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckLogAspect {

    private final JwtOperator jwtOperator;

    @Around("@annotation(com.itmuch.contentcenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) {
        try {
            /** 1. 从header里面获取token */
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = attributes.getRequest();

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

            return point.proceed();
        } catch (Throwable throwable) {
            throw new SecurityException("Token不合法!");
        }
    }
}
