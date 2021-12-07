package com.itmuch.contentcenter.modular.dev.service.impl;

import com.itmuch.contentcenter.modular.dev.mapper.HyUserMapper;
import com.itmuch.contentcenter.modular.dev.mapper.RocketTransactionLogMapper;
import com.itmuch.contentcenter.modular.dev.model.HyUser;
import com.itmuch.contentcenter.modular.dev.model.RocketTransactionLog;
import com.itmuch.contentcenter.modular.dev.model.dto.SysUserDTO;
import com.itmuch.contentcenter.modular.dev.model.dto.UserDTO;
import com.itmuch.contentcenter.modular.dev.service.HyUserService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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

    private final RocketTransactionLogMapper rocketTransactionLogMapper;

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
                "sync-user",
                SysUserDTO.builder()
                .userId(id)
                .sex(userDTO.getSex().toString())
                .build()
                );
        return hyUser;
    }

    @Override
    public HyUser modifyTran(Integer id, UserDTO userDTO) {
        /** 1.查找hy_user */
        HyUser hyUser = hyUserMapper.selectByPrimaryKey(id);
        if (hyUser == null) {
            throw new IllegalArgumentException("参数非法,用户不存在");
        }

        /** 发送半消息 */
        String transactionId = UUID.randomUUID().toString();
        this.rocketMQTemplate.sendMessageInTransaction(
            "tx-sync-user-group",
            "sync-user",
             MessageBuilder
             .withPayload(
                  SysUserDTO.builder()
                     .userId(id)
                     .sex(userDTO.getSex().toString())
                     .build()
             )
             /** header也有妙用 */
             .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
             .setHeader("user_id", id)
             .build(),
             /** arg有大用处 */
             userDTO
        );

        return hyUser;
    }

    @Transactional(rollbackFor = Exception.class)
    public void userByIdInDB(Integer id, UserDTO userDTO){
        HyUser hyUser = HyUser.builder()
                .openId(String.valueOf(id))
                .sex(userDTO.getSex().toString())
                .build();
        this.hyUserMapper.updateByPrimaryKeySelective(hyUser);

        /** 把user写到缓存 */
    }

    @Transactional(rollbackFor = Exception.class)
    public void userByIdWithRocketMqLog(Integer id, UserDTO userDTO, String transactionId){
        this.userByIdInDB(id, userDTO);

        this.rocketTransactionLogMapper.insertSelective(
            RocketTransactionLog.builder()
                .transactionId(transactionId)
                .log("用户同步")
                .build()
        );
    }
}
