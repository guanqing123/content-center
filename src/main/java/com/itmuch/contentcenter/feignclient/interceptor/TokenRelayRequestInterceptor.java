package com.itmuch.contentcenter.feignclient.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/9 15:42
 **/
public class TokenRelayRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        /** 1.获取到token */
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes)requestAttributes;
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");

        /** 2.将token传递 */
        if (StringUtils.isNotBlank(token)) {
            template.header("Authorization", token);
        }
    }
}
