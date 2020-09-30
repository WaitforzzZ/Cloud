package com.waitfor.cloud.domain.entity;

import java.util.Date;
import javax.persistence.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    /**
     * Id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色
     */
    private String roles;

    private String iphone;

    private String email;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * 状态
     */
    private String status;
}