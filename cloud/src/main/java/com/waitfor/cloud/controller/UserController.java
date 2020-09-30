package com.waitfor.cloud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.waitfor.cloud.domain.entity.Folder;
import com.waitfor.cloud.domain.entity.User;
import com.waitfor.cloud.service.FolderService;
import com.waitfor.cloud.service.UserService;
import com.waitfor.cloud.util.FileUtil;
import com.waitfor.cloud.util.Result;
import com.waitfor.cloud.util.ResultResponse;
import com.waitfor.cloud.util.TableResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FolderService folderService;
    /**
     * @title login
     * @description 用户登录
     * @Author Waitfor
     * @Date 2020-9-21 下午 2:23
     */
    @PostMapping("/login")
    public ResultResponse login(HttpServletRequest request, String userName, String password){
        log.info("^^^^^^^^^^^^^^^^^^^^ waitfor 登录信息:用户名: " + userName + "密码: " + password);
        User user = userService.findUserByUserName(userName, password);
        if(user == null){
            return Result.resuleError("用户名或密码错误！");
        }
        //用戶第一次上传文件新建根据用户名新建目录
        File dir = new File("E://cloud/"+userName);
        if (!dir.exists()){
            boolean insertFolder = folderService.insert(
                    Folder.builder()
                            .title(userName)
                            .path("E:\\cloud")
                            .author(userName)
                            .build()
            );
        }
        FileUtil.judeDirExists(dir);
        //创建session对象
        HttpSession session = request.getSession();
        //把用户数据保存在session域对象中
        session.setAttribute("userName", userName);
        user.setPassword(null);
        return Result.resuleSuccess(user);
    }

    /**
     * @title 
     * @description
     * @Author Waitfor 
     * @Date 2020-9-23 上午 9:00  
     */
    @GetMapping("/loginout")
    public ModelAndView logout(HttpServletRequest request, ModelAndView mv) {
        HttpSession session = request.getSession();
        session.invalidate();
        mv.setViewName("redirect:/index");
        return mv;
    }
    
    /**
     * 云盘页面跳转
     */
    @GetMapping("/cloud")
    public ModelAndView userIndex(ModelAndView mv) {
        mv.setViewName("user/cloud");
        return mv;
    }

    /**
     * 云盘分享页面跳转
     */
    @GetMapping("/fileShare")
    public ModelAndView userFileshare(ModelAndView mv) {
        mv.setViewName("user/fileShare");
        return mv;
    }

    /**
     * 文件管理页面跳转
     */
    @GetMapping("/fileManage")
    public ModelAndView fileManage(ModelAndView mv) {
        mv.setViewName("user/fileManage");
        return mv;
    }

    /**
     * 文件移动页面跳转
     */
    @GetMapping("/move/{id}")
    public ModelAndView fileMove(HttpServletRequest request,ModelAndView mv,@PathVariable int id) {
        HttpSession session = request.getSession();
        session.setAttribute("documentId", id);
        mv.setViewName("user/move");
        return mv;
    }

    /**
     * 文件上传页面跳转
     */
    @GetMapping("/upload2")
    public ModelAndView upload2(HttpServletRequest request,ModelAndView mv) {
       /* HttpSession session = request.getSession();
        session.setAttribute("documentId", id);*/
        mv.setViewName("user/upload");
        return mv;
    }

    /**
     * 文件管理页面右侧跳转
     */
    @GetMapping("/file/{folderId}")
    public ModelAndView file(HttpServletRequest request,ModelAndView mv, @PathVariable int folderId) {
        //创建session对象
        HttpSession session = request.getSession();
        String userName = (String)session.getAttribute("userName");
        if(folderId == 0){
            folderId = folderService.getRootFolder(userName);
        }
        //把数据保存在session域对象中
        session.setAttribute("folderId", folderId);
        List<String> dir = folderService.selectByPrimaryKey(folderId, userName);
        mv.addObject("dir",dir);
        mv.setViewName("user/file");
        return mv;
    }

    /**
     * @title
     * @description 查询普通用户
     * @Author Waitfor
     * @Date 2020-9-23 下午 3:59
     */
    @GetMapping("user.do")
    public TableResultResponse userTables(int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        PageHelper.startPage(page,limit);
        List<User> userList = userService.selectAllUser();
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        int i = (page - 1) * limit + 1;
        for(User user : pageInfo.getList()){
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("index", i + "");
            resultMap.put("id", user.getId());
            resultMap.put("userName",user.getUserName());
            resultMap.put("iphone",user.getIphone());
            resultMap.put("email",user.getEmail());
            resultMap.put("createTime", user.getCreateTime() == null ? "" : user.getCreateTime().substring(0, 19));
            infoList.add(resultMap);
            i++;
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增用户
     *
     */
    @PostMapping("/user.do")
    public ResultResponse addUser(User user) {
        User checkUser = userService.getUserByUserName(user.getUserName());
        if (checkUser != null) {
            return Result.resuleError("用户名已存在");
        }
        boolean result = userService.insert(user);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除用户,批量删除,单个删除通用
     *
     */
    @DeleteMapping("/user.do")
    public ResultResponse delUser(String ids) {
        boolean result = userService.deleteByPrimaryKey(ids);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 管理员修改用户信息
     *
     */
    @PutMapping("/user.do")
    public ResultResponse editUser(User user) {
        boolean result = false;
        try {
            result = userService.updateByPrimaryKey(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result) {
            return Result.resuleError("修改失败,未知错误");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新增用户跳转界面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addUser.do")
    public ModelAndView addUserHouser(ModelAndView mv) {
        mv.setViewName("admin/addUser");
        return mv;
    }

    /**
     * 修改密码跳转界面
     *
     * @param mv
     * @return
     */
    @GetMapping("/password.do")
    public ModelAndView showPwd(ModelAndView mv, HttpServletRequest request) {
        //创建session对象
        HttpSession session = request.getSession();
        //取出用戶名
        String userName = (String)session.getAttribute("userName");
        User user = userService.getUserByUserName(userName);
        mv.addObject("user", user);
        mv.setViewName("user/changePwd");
        return mv;
    }

    /**
     * 用户修改密码
     *
     */
    @PutMapping("/password.do")
    public ResultResponse updatePassword(String newPas, String oldPas, String userName) {
        User user = userService.getUserByUserName(userName);
        if (oldPas.equals(user.getPassword())) {
            user.setPassword(newPas);
            boolean result = userService.updateByPrimaryKey(user);
            if (result) {
                return Result.resuleSuccess();
            } else {
                return Result.resuleError("修改失败;原密码错误");
            }
        }
        return Result.resuleError("原密码错误,请重新输入");


    }

    /**
     * 修改用户跳转界面
     *
     * @param mv
     * @return
     */
    @GetMapping("/editUser.do/{id}")
    public ModelAndView editUserHouser(ModelAndView mv,@PathVariable String id) {
        User user = userService.selectByPrimaryKey(id);
        mv.addObject("user", user);
        mv.setViewName("admin/editUser");
        return mv;
    }
}
