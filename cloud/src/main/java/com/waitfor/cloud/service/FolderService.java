package com.waitfor.cloud.service;

import com.waitfor.cloud.domain.dto.FolderDTO;
import com.waitfor.cloud.domain.entity.Folder;

import java.util.List;

/**
 * @Author Waitfor
 * @Date 2020-9-25 上午 10:24
 * @Version 1.0
 */
public interface FolderService {

    /**
     * 新增
     *
     * @param folder
     * @return
     */
    boolean insert(Folder folder);

    /**
     * 查询登录用户所有的文件夹
     *
     * @param userName
     * @return
     */
    Folder getAllFolder(String userName);

    /**
     * @title
     * @description 用户创建文件夹
     * @Author Waitfor
     * @Date 2020-9-26 下午 10:17
     */
    Boolean createFolder(Folder folder);

    /**
     * @title
     * @description 查询当前所在目录
     * @Author Waitfor
     * @Date 2020-9-27 下午 12:48
     */
    List<String> selectByPrimaryKey(int folderId, String userName);

    /**
     * @title
     * @description 查询用户的根目录
     * @Author Waitfor
     * @Date 2020-9-28 上午 9:31
     */
    int getRootFolder(String userName);

    Folder getFolderByID(Integer folderId);

    /**
     * @title
     * @description 删除文件夹
     * @Author Waitfor
     * @Date 2020-9-29 下午 8:23
     */
    Boolean deleteFolder(Integer id);
    /**
     * @title
     * @description 修改文件夹
     * @Author Waitfor
     * @Date 2020-9-29 下午 8:23
     */
    Boolean updateFolder(Folder folder);
}
