package com.waitfor.cloud.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Waitfor
 * @Date 2020-9-27 下午 3:18
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderSearch {

    private String folderName;

    private String status;
}
