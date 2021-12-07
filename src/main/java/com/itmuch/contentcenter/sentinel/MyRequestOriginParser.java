package com.itmuch.contentcenter.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/7 15:04
 **/
/** 为了避免对后面测试有影响,先注释掉 */
// @Component
public class MyRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest request) {
        /** 从请求参数中获取名为 origin 的参数并返回
         *  如何获取不到origin参数,那么就抛异常
         */
        String origin = request.getParameter("origin");
        if (StringUtils.isBlank(origin)) {
            throw new IllegalArgumentException("origin must be specified");
        }
        return origin;
    }
}
