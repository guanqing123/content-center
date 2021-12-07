package com.itmuch.contentcenter.modular.dev.service.impl;

import com.itmuch.contentcenter.modular.dev.mapper.HyUserMapper;
import com.itmuch.contentcenter.modular.dev.model.HyUser;
import com.itmuch.contentcenter.modular.dev.model.dto.SysUserDTO;
import com.itmuch.contentcenter.modular.dev.model.dto.UserDTO;
import com.itmuch.contentcenter.modular.dev.service.HyUserService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/7 19:44
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HyUserServiceImpl implements HyUserService {

    private final HyUserMapper hyUserMapper;

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public HyUser modifyUser(Integer id, UserDTO userDTO) {
        /** 1.查找hy_user */
        HyUser hyUser = hyUserMapper.selectByPrimaryKey(id);
        if (hyUser == null) {
            throw new IllegalArgumentException("参数非法,用户不存在");
        }

        /** 2.修改hy_user sex属性 */
        hyUser.setSex(userDTO.getSex().toString());
        hyUserMapper.updateByPrimaryKey(hyUser);

        /** 3.同步用户中心 sys_user sex属性
         *  发送消息给rocketmq,让用户中心去消费，同步修改 sys_user sex属性
         * */
        rocketMQTemplate.convertAndSend(
                "sync_user",
                SysUserDTO.builder()
                .userId(id)
                .sex(userDTO.getSex().toString())
                .build()
                );
        return hyUser;
    }
}
