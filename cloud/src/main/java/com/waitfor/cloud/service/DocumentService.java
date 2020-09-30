package com.waitfor.cloud.service;

import com.waitfor.cloud.domain.dto.FolderSearch;
import com.waitfor.cloud.domain.entity.Document;
import com.waitfor.cloud.domain.entity.Folder;

import java.util.List;

/**
 * @Author Waitfor
 * @Date 2020-9-22 下午 2:00
 * @Version 1.0
 */
public interface DocumentService {
    boolean insert(Document document);

    List<Document> selectAllDocument(String userName, String shareFlag);

    boolean updateshareFlag(int id, String shareFlag);

    List<Document> getFileByFolderId(int folderId, FolderSearch folderSearch, String userName);

    boolean documentMove(Folder folder, int documentId);

    Document getDocumentById(int fileId);

    boolean deleteByPrimaryKey(Integer id);
}
