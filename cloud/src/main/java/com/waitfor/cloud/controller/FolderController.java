package com.waitfor.cloud.controller;

import com.waitfor.cloud.domain.entity.Folder;
import com.waitfor.cloud.service.FolderService;
import com.waitfor.cloud.util.Result;
import com.waitfor.cloud.util.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author Waitfor
 * @Date 2020-9-25 上午 10:49
 * @Version 1.0
 */
@Slf4j
@RestController
public class FolderController {

    @Autowired
    private FolderService folderService;

    /**
     * @title
     * @description 递归查找当前用户文件夹
     * @Author Waitfor
     * @Date 2020-9-25 下午 2:19
     */
    @GetMapping("/folder.do")
    public ResultResponse getAllFolder(HttpServletRequest request) {
        //创建session对象
        HttpSession session = request.getSession();
        //取出用戶名
        String userName = (String)session.getAttribute("userName");
        Folder folder = folderService.getAllFolder(userName);
        return Result.resuleSuccess(folder);
    }

    /**
     * @title
     * @description 用户新建文件夹
     * @Author Waitfor
     * @Date 2020-9-25 下午 2:19
     */
    @PostMapping("/folder.do")
    public ResultResponse createFolder(Folder folder) {
        Boolean result = folderService.createFolder(folder);
        if(!result){
            return Result.resuleError("创建失败，该文件夹已存在，请重新命名");
        }
        return Result.resuleSuccess();
    }

    /**
     * @title
     * @description 用户修改文件夹
     * @Author Waitfor
     * @Date 2020-9-25 下午 2:19
     */
    @PutMapping("/folder.do")
    public ResultResponse updateFolder(Folder folder) {
        if(folder.getTitle() == null || folder.getTitle().length() <= 0){
            return Result.resuleError("修改失败，该文件夹不能为空，请重新命名");
        }
        Boolean result = folderService.updateFolder(folder);
        if(!result){
            return Result.resuleError("修改失败，该文件夹已存在，请重新命名");
        }
        return Result.resuleSuccess();
    }

    /**
     * @title
     * @description 用户删除文件夹
     * @Author Waitfor
     * @Date 2020-9-25 下午 2:19
     */
    @DeleteMapping("/folder.do")
    public ResultResponse deleteFolder(Integer id) {
        Boolean result = folderService.deleteFolder(id);
        if(!result){
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}
