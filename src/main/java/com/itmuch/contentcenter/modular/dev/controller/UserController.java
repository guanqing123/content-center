package com.itmuch.contentcenter.modular.dev.controller;

import com.itmuch.contentcenter.auth.CheckAuthorization;
import com.itmuch.contentcenter.modular.dev.model.HyUser;
import com.itmuch.contentcenter.modular.dev.model.dto.UserDTO;
import com.itmuch.contentcenter.modular.dev.service.HyUserService;
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
    @CheckAuthorization("stylefeng")
    public HyUser modifyUser(@PathVariable Integer id,@RequestBody UserDTO userDTO){
        // TODO: 认证、授权
        return this.hyUserService.modifyUser(id, userDTO);
    }

    @PutMapping("/tran/{id}")
    public HyUser modifyTran(@PathVariable Integer id,@RequestBody UserDTO userDTO){
        // TODO: 认证、授权
        return this.hyUserService.modifyTran(id, userDTO);
    }

    @PutMapping("/stream/{id}")
    public HyUser modifyTranByStream(@PathVariable Integer id,@RequestBody UserDTO userDTO){
        // TODO: 认证、授权
        return this.hyUserService.modifyTranByStream(id, userDTO);
    }
}
