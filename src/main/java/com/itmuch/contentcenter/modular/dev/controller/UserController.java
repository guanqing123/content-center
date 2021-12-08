package com.itmuch.contentcenter.modular.dev.controller;

import com.itmuch.contentcenter.modular.dev.model.HyUser;
import com.itmuch.contentcenter.modular.dev.model.dto.UserDTO;
import com.itmuch.contentcenter.modular.dev.service.HyUserService;
import com.itmuch.contentcenter.rocketmq.MySource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/tran/{id}")
    public HyUser modifyTran(@PathVariable Integer id,@RequestBody UserDTO userDTO){
        // TODO: 认证、授权
        return this.hyUserService.modifyTran(id, userDTO);
    }

    @Autowired
    private Source source;

    @GetMapping("/test-stream")
    public String testStream(){
        this.source.output()
                .send(
                    MessageBuilder
                        .withPayload("消息体")
                        .build()
                );
        return "success";
    }

    @Autowired
    private MySource mySource;

    @GetMapping("/test-my-stream")
    public String testMyStream(){
        this.mySource.output()
                .send(
                    MessageBuilder
                        .withPayload("消息体")
                        .build()
                );
        return "success";
    }
}
