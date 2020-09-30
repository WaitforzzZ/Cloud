package com.waitfor.cloud.controller;

import com.waitfor.cloud.domain.entity.Document;
import com.waitfor.cloud.domain.entity.Folder;
import com.waitfor.cloud.service.DocumentService;
import com.waitfor.cloud.service.FolderService;
import com.waitfor.cloud.util.FileUtil;
import com.waitfor.cloud.util.Result;
import com.waitfor.cloud.util.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;

/**
 * @Author Waitfor
 * @Date 2020-9-22 下午 12:47
 * @Version 1.0
 */
@Slf4j
@RestController
public class UploadController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FolderService folderService;

    @PostMapping("/upload")
    public ResultResponse upload(MultipartFile file, HttpServletRequest request) {
        //创建session对象
        HttpSession session = request.getSession();
        Integer folderId = (Integer) session.getAttribute("folderId");
        Folder folder = null;
        try {
            folder = folderService.getFolderByID(folderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        String documentSize = FileUtil.getFileSize(fileSize);
        String destFileName = folder.getPath() + File.separator + folder.getTitle() + File.separator + fileName;
        log.info(destFileName);
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            return Result.resuleError("该目录下已存在相同的文件,请重新上传");
        }
        try {
            destFile.getParentFile().mkdirs();
            file.transferTo(destFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Result.resuleError("上传失败," + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.resuleError("上传失败," + e.getMessage());
        }
        documentService.insert(
                Document.builder()
                        .name(fileName)
                        .url(destFileName)
                        .size(documentSize)
                        .author(folder.getAuthor())
                        .folderId(folderId)
                        .build()
        );
        return Result.resuleSuccess();
    }

    @GetMapping(value = "/downLoad/{fileId}")
    public ResponseEntity<byte[]> download(@PathVariable("fileId") int fileId) throws UnsupportedEncodingException {
        Document document = documentService.getDocumentById(fileId);
        File file = new File(document.getUrl());
        byte[] body = null;
        try {
            InputStream is = new FileInputStream(file);
            body = new byte[is.available()];
            is.read(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode(document.getName(), "UTF-8"));
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, statusCode);
        return entity;
    }
}
