package com.itmuch.contentcenter.modular.dev.controller;

import com.itmuch.contentcenter.modular.dev.model.HyUser;
import com.itmuch.contentcenter.modular.dev.model.param.UserDTO;
import com.itmuch.contentcenter.modular.dev.service.HyUserService;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/7 19:43
 **/
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final HyUserService hyUserService;

    @PutMapping("/user/{id}")
    public HyUser modifyUser(@PathVariable Integer id,@RequestBody UserDTO userDTO){
        // TODO: 认证、授权
        return this.hyUserService.modifyUser(id, userDTO);
    }
}
