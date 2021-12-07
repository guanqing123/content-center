package com.itmuch.contentcenter.modular.dev.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hy_user")
public class HyUser {
    @Id
    @Column(name = "open_id")
    private String openId;

    private String name;

    private String telephone;

    private String sex;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}