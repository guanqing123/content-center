package com.itmuch.contentcenter.modular.dev.model.dto;

import com.itmuch.contentcenter.core.enums.SexEnum;
import lombok.Data;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/7 18:52
 **/
@Data
public class UserDTO {
    /** 账号 */
    private String account;

    /** 性别 */
    private SexEnum sex;
}
