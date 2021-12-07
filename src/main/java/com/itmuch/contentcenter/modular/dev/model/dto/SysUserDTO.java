package com.itmuch.contentcenter.modular.dev.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/7 21:42
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUserDTO {
    /** 同步谁 */
    private Integer userId;
    /** 性别 */
    private String sex;
}
