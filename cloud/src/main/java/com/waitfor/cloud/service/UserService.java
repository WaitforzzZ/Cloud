package com.waitfor.cloud.service;


import com.waitfor.cloud.domain.entity.User;

import java.util.List;

public interface UserService {

    User findUserByUserName(String userName, String password);

    /**
     * @title
     * @description 查询所有的普通用户
     * @Author Waitfor
     * @Date 2020-9-23 下午 1:03
     */
    List<User> selectAllUser();

    /**
     * 删除,单个删除批量删除通用
     *
     * @param ids
     * @return
     */
    boolean deleteByPrimaryKey(String ids);

    /**
     * 仅仅得到User对象
     *
     * @param userName
     * @return
     */
    User getUserByUserName(String userName);

    /**
     * 新增
     *
     * @param user
     * @return
     */
    boolean insert(User user);

    /**
     * 根据主键id查询
     *
     * @param id
     * @return
     */
    User selectByPrimaryKey(String id);

    /**
     * 修改
     *
     * @param user
     * @return
     */
    boolean updateByPrimaryKey(User user);
}
