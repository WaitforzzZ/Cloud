package com.waitfor.cloud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.waitfor.cloud.domain.dto.FolderSearch;
import com.waitfor.cloud.domain.entity.Document;
import com.waitfor.cloud.domain.entity.Folder;
import com.waitfor.cloud.service.DocumentService;
import com.waitfor.cloud.util.FileUtil;
import com.waitfor.cloud.util.Result;
import com.waitfor.cloud.util.ResultResponse;
import com.waitfor.cloud.util.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Waitfor
 * @Date 2020-9-22 下午 3:48
 * @Version 1.0
 */
@RestController
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("document")
    public TableResultResponse userTables(int page, int limit, String shareFlag, HttpServletRequest request) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        HttpSession session = request.getSession();
        //取出用戶名
        String userName = (String)session.getAttribute("userName");
        PageHelper.startPage(page,limit);
        List<Document> documentList = documentService.selectAllDocument(userName, shareFlag);
        PageInfo<Document> pageInfo = new PageInfo<Document>(documentList);
        int i = (page - 1) * limit + 1;
        for(Document document : pageInfo.getList()){
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("index", i + "");
            resultMap.put("id", document.getId());
            String link = "<a href=\"/downLoad/"+ document.getId()+"\" style=\"color:#2ab7ff;cursor:pointer\">"+document.getName()+"</a>";
            resultMap.put("name",link);
            resultMap.put("size", document.getSize());
            resultMap.put("author", document.getAuthor());
            resultMap.put("createTime", document.getCreateTime() == null ? "" : document.getCreateTime().substring(0, 19));
            if ("1".equals(document.getShareFlag())) {
                resultMap.put("shareFlag", "是");
            } else if ("0".equals(document.getShareFlag())) {
                resultMap.put("shareFlag", "否");
            }
            infoList.add(resultMap);
            i++;
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 更改文件分享状态
     *
     */
    @PutMapping("shareFlag.do")
    public ResultResponse updateUserStatus(int id, String shareFlag) {
        boolean result = documentService.updateshareFlag(id, shareFlag);
        if (!result) {
            return Result.resuleError("更改失败");
        }
        return Result.resuleSuccess();
    }

    @GetMapping("document.do")
    public TableResultResponse getFileByFolderId(int page, int limit, HttpServletRequest request, FolderSearch folderSearch) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        HttpSession session = request.getSession();
        int folderId = (int)session.getAttribute("folderId");
        String userName = (String)session.getAttribute("userName");
        PageHelper.startPage(page,limit);
        List<Document> documentList = documentService.getFileByFolderId(folderId, folderSearch, userName);
        PageInfo<Document> pageInfo = new PageInfo<Document>(documentList);
        int i = (page - 1) * limit + 1;
        for(Document document : pageInfo.getList()){
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("index", i + "");
            resultMap.put("id", document.getId());
            resultMap.put("type", FileUtil.getFileType(document.getName()));
            String link = "<a href=\"/downLoad/"+document.getId()+"\" style=\"color:#2ab7ff;cursor:pointer\">"+document.getName()+"</a>";
            resultMap.put("name",link);
            resultMap.put("size", document.getSize());
            resultMap.put("author", document.getAuthor());
            resultMap.put("createTime", document.getCreateTime() == null ? "" : document.getCreateTime().substring(0, 19));
            if ("1".equals(document.getShareFlag())) {
                resultMap.put("shareFlag", "是");
            } else if ("0".equals(document.getShareFlag())) {
                resultMap.put("shareFlag", "否");
            }
            infoList.add(resultMap);
            i++;
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 文件移动
     *
     */
    @PostMapping("move.do")
    public ResultResponse documentMove(Folder folder, HttpServletRequest request) {
        HttpSession session = request.getSession();
        //取出用戶名
        int documentId = (int)session.getAttribute("documentId");
        boolean result = documentService.documentMove(folder, documentId);
        if (!result) {
            return Result.resuleError("移动失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 文件移动
     *
     */
    @DeleteMapping("document.do")
    public ResultResponse documentDelete(Integer id) {
        boolean result = false;
        try {
            result = documentService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.resuleError("删除失败");
        }
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}
