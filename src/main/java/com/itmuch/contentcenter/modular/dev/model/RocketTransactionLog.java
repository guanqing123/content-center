package com.itmuch.contentcenter.modular.dev.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@Table(name = "rocket_transaction_log")
public class RocketTransactionLog {
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 事务id
     */
    @Column(name = "transaction_id")
    private String transactionId;

    /**
     * 日志
     */
    private String log;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取事务id
     *
     * @return transaction_id - 事务id
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * 设置事务id
     *
     * @param transactionId 事务id
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * 获取日志
     *
     * @return log - 日志
     */
    public String getLog() {
        return log;
    }

    /**
     * 设置日志
     *
     * @param log 日志
     */
    public void setLog(String log) {
        this.log = log;
    }
}