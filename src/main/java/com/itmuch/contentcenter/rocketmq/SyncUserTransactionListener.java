package com.itmuch.contentcenter.rocketmq;

import com.itmuch.contentcenter.modular.dev.mapper.RocketTransactionLogMapper;
import com.itmuch.contentcenter.modular.dev.model.RocketTransactionLog;
import com.itmuch.contentcenter.modular.dev.model.dto.UserDTO;
import com.itmuch.contentcenter.modular.dev.service.HyUserService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * @description: TODO 类描述
 * @Author guanqing
 * @Date 2021/12/7 22:55
 **/
@RocketMQTransactionListener(txProducerGroup = "tx-sync-user-group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncUserTransactionListener implements RocketMQLocalTransactionListener {

    private final HyUserService hyUserService;

    private final RocketTransactionLogMapper rocketTransactionLogMapper;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer userId = Integer.valueOf((String) headers.get("user_id"));

        try {
            this.hyUserService.userByIdWithRocketMqLog(userId, (UserDTO) o, transactionId);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);

        /** select * from xxx where transaction_id = xxx */
        RocketTransactionLog rocketTransactionLog = this.rocketTransactionLogMapper.selectOne(
                RocketTransactionLog.builder()
                        .transactionId(transactionId)
                        .build()
        );
        if (transactionId != null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
