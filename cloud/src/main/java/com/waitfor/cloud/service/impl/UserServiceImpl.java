package com.waitfor.cloud.service.impl;

import com.waitfor.cloud.dao.DocumentMapper;
import com.waitfor.cloud.dao.FolderMapper;
import com.waitfor.cloud.dao.UserMapper;
import com.waitfor.cloud.domain.entity.Document;
import com.waitfor.cloud.domain.entity.Folder;
import com.waitfor.cloud.domain.entity.User;
import com.waitfor.cloud.service.UserService;
import com.waitfor.cloud.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author Waitfor
 * @Date 2020-9-21 下午 2:32
 * @Version 1.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private DocumentMapper documentMapper;

    @Resource
    private FolderMapper folderMapper;

    @Override
    public User findUserByUserName(String userName, String password) {
        User user = userMapper.selectOne(
                User.builder()
                        .userName(userName)
                        .password(password)
                        .status("1")
                        .build()
        );
        return user;
    }

    @Override
    public List<User> selectAllUser() {
        Example example = Example.builder(User.class)
                .where(Sqls.custom().andEqualTo("roles", "0").andEqualTo("status", "1"))
                .orderByDesc("createTime")
                .build();
        List<User> userList = userMapper.selectByExample(example);
        return userList;
    }

    @Override
    @Transactional
    public boolean deleteByPrimaryKey(String ids) {
        String[] idList = ids.split(",");
        int result = 0;
        for (String s : idList) {
            // 删除用户 0表示删除 连带删除该用户的文件夹
            User user = userMapper.selectByPrimaryKey(s);
            user.setStatus("0");
            result = userMapper.updateByPrimaryKey(user);
            File targetFile = new File("E:\\cloud\\" + user.getUserName());
            if (targetFile.isDirectory()) {  //如果是文件夹
                try {
                    documentMapper.delete(
                            Document.builder()
                                    .author(user.getUserName())
                                    .build()
                    );
                    folderMapper.delete(
                            Folder.builder()
                                    .author(user.getUserName())
                                    .build()
                    );
                    FileUtils.deleteDirectory(targetFile);  //文件夹删除操作
                } catch (IOException e) {
                    log.info("删除文件失败",e);
                }
            }
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User getUserByUserName(String userName) {
        User searchUser = new User();
        searchUser.setUserName(userName);
        searchUser.setStatus("1");
        User user = userMapper.selectOne(searchUser);
        return user;
    }

    @Override
    public boolean insert(User user) {
        int result = userMapper.insertSelective(user);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User selectByPrimaryKey(String id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public boolean updateByPrimaryKey(User user) {
        int result = userMapper.updateByPrimaryKeySelective(user);

        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }


}
