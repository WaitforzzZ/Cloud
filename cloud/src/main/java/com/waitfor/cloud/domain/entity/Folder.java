package com.waitfor.cloud.domain.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.waitfor.cloud.domain.dto.FolderDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "folder")
public class Folder {
    /**
     * ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 文件夹名称
     */
    private String title;

    /**
     * 文件夹名路径
     */
    private String path;

    /**
     * 创建人
     */
    private String author;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 状态(0:删除，1:正常)
     */
    private String status;

    /**
     * 子级数据
     */
    private List<Folder> children;
}