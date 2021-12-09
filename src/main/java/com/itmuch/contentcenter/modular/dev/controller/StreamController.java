package com.itmuch.contentcenter.modular.dev.controller;

import com.itmuch.contentcenter.rocketmq.MySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: TODO stream
 * @Author guanqing
 * @Date 2021/12/8 11:20
 **/
@RestController
@RequestMapping("/content")
public class StreamController {

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
