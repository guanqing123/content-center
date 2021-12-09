package com.itmuch.contentcenter.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 鉴权注解
 * @Author guanqing
 * @Date 2021/12/9 20:49
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthorization {
    String value();
}
