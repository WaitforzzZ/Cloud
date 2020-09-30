package com.waitfor.cloud.service.impl;

import com.waitfor.cloud.dao.DocumentMapper;
import com.waitfor.cloud.dao.FolderMapper;
import com.waitfor.cloud.domain.dto.FolderSearch;
import com.waitfor.cloud.domain.entity.Document;
import com.waitfor.cloud.domain.entity.Folder;
import com.waitfor.cloud.service.DocumentService;
import com.waitfor.cloud.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Waitfor
 * @Date 2020-9-22 下午 2:06
 * @Version 1.0
 */
@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    @Resource
    private DocumentMapper documentMapper;

    @Resource
    private FolderMapper folderMapper;

    @Override
    public boolean insert(Document document) {
        document.setShareFlag("0");
        document.setCreateTime(DateUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        int result = documentMapper.insertSelective(document);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Document> selectAllDocument(String userName, String shareFlag) {
        if("1".equals(shareFlag)){
            List<Document> documentList = documentMapper.select(
                    Document.builder()
                            .shareFlag(shareFlag)
                            .build()
            );
            return documentList;
        }
        List<Document> documentList = documentMapper.select(
                Document.builder()
                        .author(userName)
                        .build()
        );
        return documentList;
    }

    @Override
    public boolean updateshareFlag(int id, String shareFlag) {
        int result = documentMapper.updateByPrimaryKeySelective(
                Document.builder()
                        .id(id)
                        .shareFlag(shareFlag)
                        .build()
        );
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Document> getFileByFolderId(int folderId, FolderSearch folderSearch, String userName) {
        if("1".equals(folderSearch.getStatus())){
            List<Folder> selectList = folderMapper.select(
                    Folder.builder()
                            .author(userName)
                            .build()
            );
            Folder folder = selectList.stream()
                    .filter(instance -> instance.getId() == folderId)
                    .findFirst()
                    .orElse(null);
            List<Integer> folderIdList = new ArrayList<>();
            folderIdList.add(folderId);
            getChild(folder.getId(),selectList,folderIdList);
            List<Document> result = new ArrayList<>();
            for(int folId : folderIdList){
                Example example = Example.builder(Document.class)
                        .where(Sqls.custom().andEqualTo("folderId", folId).andLike("name", "%"+folderSearch.getFolderName() +"%"))
                        .orderByDesc("createTime")
                        .build();
                List<Document> documents = documentMapper.selectByExample(example);
                for(Document document : documents){
                    result.add(document);
                }
            }
            return result;
        }
        if(folderSearch.getFolderName() == null || folderSearch.getFolderName().length() <= 0){
            Example example = Example.builder(Document.class)
                    .where(Sqls.custom().andEqualTo("folderId", folderId))
                    .orderByDesc("createTime")
                    .build();
            List<Document> documentList = documentMapper.selectByExample(example);
            return documentList;
        }
        Example example = Example.builder(Document.class)
                .where(Sqls.custom().andEqualTo("folderId", folderId).andLike("name", "%"+folderSearch.getFolderName() +"%"))
                .orderByDesc("createTime")
                .build();
        List<Document> documentList = documentMapper.selectByExample(example);
        return documentList;
    }

    @Override
    @Transactional
    public boolean documentMove(Folder folder, int documentId) {
        Document document = documentMapper.selectByPrimaryKey(documentId);
        // 源文件路径
        File startFile=new File(document.getUrl());
        // 目的目录路径
        File endDirection=new File(folder.getPath()+"\\"+folder.getTitle());
        // 目的文件路径=目的目录路径+源文件名称
        File endFile=new File(endDirection+ File.separator+ document.getName());
        document.setFolderId(folder.getId());
        document.setUrl(endDirection+ File.separator+ document.getName());
        documentMapper.updateByPrimaryKeySelective(document);
        if (startFile.renameTo(endFile)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Document getDocumentById(int fileId) {
        Document document = documentMapper.selectByPrimaryKey(fileId);
        return document;
    }

    @Transactional
    @Override
    public boolean deleteByPrimaryKey(Integer id) {
        Document document = documentMapper.selectByPrimaryKey(id);
        File file = new File(document.getUrl());
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.info("删除文件" + document.getUrl() + "成功！");
                int result = documentMapper.deleteByPrimaryKey(id);
                if(result <= 0){
                    return false;
                }
                return true;
            } else {
                log.info("删除文件" + document.getUrl() + "失败！");
                return false;
            }
        } else {
            log.info("删除文件失败：" + document.getUrl() + "不存在！");
            return false;
        }
    }

    private void getChild(int parentId, List<Folder> rootList, List<Integer> folderIdList){
        List<Folder> childList = rootList.stream()
                .filter(instance -> instance.getParentId() == parentId)
                .collect(Collectors.toList());
        if(childList != null){
            // 把子文件的子文件再循环一遍
            for(Folder folder : childList){
                folderIdList.add(folder.getId());
                getChild(folder.getId(),rootList,folderIdList);
            }
        }
    }
}
