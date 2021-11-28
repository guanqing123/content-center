package com.itmuch.contentcenter.modular.dev.service.impl;

import com.itmuch.contentcenter.modular.dev.mapper.HyFansMapper;
import com.itmuch.contentcenter.modular.dev.model.HyFans;
import com.itmuch.contentcenter.modular.dev.service.HyFansService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/11/28 19:59
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HyFansServieImpl implements HyFansService {

    private final HyFansMapper hyFansMapper;

    @Override
    public List<HyFans> getFans() {
        return hyFansMapper.selectAll();
    }

    @Override
    public HyFans getFan() {
        return hyFansMapper.selectByPrimaryKey("oZIooxJ_MT0M1ApB_4caa_gvXgWc");
    }
}
