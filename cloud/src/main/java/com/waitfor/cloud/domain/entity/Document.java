package com.waitfor.cloud.domain.entity;

import java.util.Date;
import javax.persistence.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document")
public class Document {
    /**
     * Id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件路劲
     */
    private String url;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 作者
     */
    private String author;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 分享标识
     */
    @Column(name = "share_flag")
    private String shareFlag;

    /**
     * 文件夹ID
     */
    @Column(name = "folder_id")
    private Integer folderId;
}