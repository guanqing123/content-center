package com.itmuch.contentcenter.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/7 16:04
 **/
/** 新版sentinel已自动适配Restful */
// @Component
public class MyUrlCleaner implements UrlCleaner {
    @Override
    public String clean(String originUrl) {
        /** 让 /getRestFul/1 与 /getRestFull/2 返回相同
         *  返回 /getRestFul/{number}
         * */

        String[] split = originUrl.split("/");

       return Arrays.stream(split)
            .map(string->{
                if (NumberUtils.isNumber(string)){
                    return "{number}";
                }
                return string;
            })
            .reduce((a,b)-> a+"/"+b)
            .orElse("");
    }
}
