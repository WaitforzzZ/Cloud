package com.waitfor.cloud.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * @Author Waitfor
 * @Date 2020-9-25 上午 11:09
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderDTO {
    /**
     * ID
     */
    private Integer id;

    /**
     * 文件夹名称
     */
    private String title;
    /**
     * 子级数据
     */
    private List<FolderDTO> children;
}
