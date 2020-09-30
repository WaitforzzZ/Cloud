package com.waitfor.cloud.service.impl;

import com.waitfor.cloud.dao.DocumentMapper;
import com.waitfor.cloud.dao.FolderMapper;
import com.waitfor.cloud.domain.entity.Document;
import com.waitfor.cloud.domain.entity.Folder;
import com.waitfor.cloud.service.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Waitfor
 * @Date 2020-9-25 上午 10:24
 * @Version 1.0
 */
@Slf4j
@Service
public class FolderServiceImpl implements FolderService {

    @Resource
    private FolderMapper folderMapper;

    @Resource
    private DocumentMapper documentMapper;

    @Override
    public boolean insert(Folder folder) {
        int result = folderMapper.insertSelective(folder);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Folder getAllFolder(String userName) {
        List<Folder> selectList = folderMapper.select(
                Folder.builder()
                        .author(userName)
                        .build()
        );
        Folder folder = selectList.stream()
                .filter(instance -> instance.getParentId() == 0)
                .findFirst()
                .orElse(null);
        folder.setChildren(getChild(folder.getId(), selectList));
        return folder;
    }

    private List<Folder> getChild(int parentId, List<Folder> rootList) {
        List<Folder> childList = rootList.stream()
                .filter(instance -> instance.getParentId() == parentId)
                .collect(Collectors.toList());
        // 把子文件的子文件再循环一遍
        for (Folder folder : childList) {
            folder.setChildren(getChild(folder.getId(), rootList));
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    @Override
    @Transactional
    public Boolean createFolder(Folder folder) {
        // 父节点的数据
        Folder parentData = folderMapper.selectByPrimaryKey(folder.getId());
        List<Folder> selectAll = folderMapper.select(
                Folder.builder()
                        .author(parentData.getAuthor())
                        .build()
        );
        List<Folder> repeatList = selectAll.stream()
                .filter(instance -> instance.getParentId() == folder.getId()&&"新建文件夹".equals(instance.getTitle()))
                .collect(Collectors.toList());
        if(!repeatList.isEmpty()){
            return false;
        }
        int result = folderMapper.insertSelective(
                Folder.builder()
                        .title("新建文件夹")
                        .author(parentData.getAuthor())
                        .path(parentData.getPath() + "\\" + parentData.getTitle())
                        .parentId(parentData.getId())
                        .build()
        );
        String createFolderPath = parentData.getPath() + "\\" + parentData.getTitle() + "\\" + "新建文件夹";
        if(result > 0){
            File dir = new File(createFolderPath);
            if (!dir.exists()) {
                dir.mkdir();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> selectByPrimaryKey(int folderId, String userName) {
        if (folderId == 0) {
            Folder select = folderMapper.selectOne(
                    Folder.builder()
                            .author(userName)
                            .parentId(0)
                            .build()
            );
            folderId = select.getId();
        }
        Folder folder = folderMapper.selectByPrimaryKey(folderId);
        String path = folder.getPath() + "\\" + folder.getTitle();
        String[] split = path.substring(8).split("\\\\");
        return Arrays.asList(split);
    }

    @Override
    public int getRootFolder(String userName) {
        Folder folder = folderMapper.selectOne(
                Folder.builder()
                        .author(userName)
                        .parentId(0)
                        .build()
        );
        return folder.getId();
    }

    @Override
    public Folder getFolderByID(Integer folderId) {
        Folder folder = folderMapper.selectByPrimaryKey(folderId);
        return folder;
    }

    @Override
    @Transactional
    public Boolean deleteFolder(Integer id) {
        // 查询用户所选的文件夹
        Folder folderSelect = folderMapper.selectByPrimaryKey(id);
        // 判断文件夹是否为根目录，是的话不允许删除
        if(folderSelect.getParentId() == 0){
            return false;
        }
        List<Folder> folderAll = folderMapper.select(
                Folder.builder()
                        .author(folderSelect.getAuthor())
                        .build()
        );
        // 存储用户所选文件夹的id及子文件夹id
        List<Integer> folderIdList = new ArrayList<>();
        folderIdList.add(id);
        // 递归向folderIdList存储子文件夹id
        getChildFolderId(folderSelect.getId(), folderAll, folderIdList);
        // 删除文件夹下所有文件
        for (int folderId : folderIdList) {
            folderMapper.delete(
                    Folder.builder()
                            .id(folderId)
                            .build()
            );
            documentMapper.delete(
                    Document.builder()
                            .folderId(folderId)
                            .build()
            );
        }
        File targetFile = new File(folderSelect.getPath() + File.separator + folderSelect.getTitle());
        if (targetFile.isDirectory()) {  //如果是文件夹
            try{
                FileUtils.deleteDirectory(targetFile);  //文件夹删除操作
            }catch (IOException e){
                log.error("删除文件失败",e);
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean updateFolder(Folder folder) {
        List<Folder> selectAll = folderMapper.select(
                Folder.builder()
                        .author(folder.getAuthor())
                        .build()
        );
        Folder folderOld = selectAll.stream()
                .filter(instance -> instance.getId() == folder.getId())
                .findFirst()
                .orElse(null);
        // 查询子目录下是否存在相同的文件名
        List<Folder> repeatList = selectAll.stream()
                .filter(instance -> instance.getParentId() == folder.getParentId()&&folder.getTitle().equals(instance.getTitle()))
                .collect(Collectors.toList());
        if(!repeatList.isEmpty()){
            return false;
        }
        int result = folderMapper.updateByPrimaryKeySelective(folder);
        // 存储用户所选文件夹的id及子文件夹id
        List<Integer> folderIdList = new ArrayList<>();
        folderIdList.add(folder.getId());
        String oldPath = folder.getPath() + File.separator + folderOld.getTitle();
        String newPath = folder.getPath() + File.separator + folder.getTitle();
        // 递归向folderIdList存储子文件夹id
        getChildFolderIdUpdatePath(folder.getId(), selectAll, folderIdList, oldPath, newPath);
        // 修改文件夹下所有文件的url
        for (int folderId : folderIdList) {
            List<Document> documentList = documentMapper.select(
                    Document.builder()
                            .folderId(folderId)
                            .build()
            );
            for (Document document : documentList)
            {
                String replacePath = document.getUrl().replace(oldPath, newPath);
                documentMapper.updateByPrimaryKeySelective(
                        Document.builder()
                                .id(document.getId())
                                .url(replacePath)
                                .build()
                );
            }
        }
        //想命名的原文件夹的路径
        File old = new File(oldPath);
        //将原文件夹更改为A，其中路径是必要的。
        old.renameTo(new File(newPath));
        return true;
    }

    // 查询文件夹下子文件的Id
    private void getChildFolderId(int parentId, List<Folder> rootList, List<Integer> folderIdList) {
        List<Folder> childList = rootList.stream()
                .filter(instance -> instance.getParentId() == parentId)
                .collect(Collectors.toList());
        if (childList != null) {
            // 把子文件的子文件再循环一遍
            for (Folder folder : childList) {
                folderIdList.add(folder.getId());
                getChildFolderId(folder.getId(), rootList, folderIdList);
            }
        }
    }

    // 查询文件夹下子文件的Id且更新文件路径
    private void getChildFolderIdUpdatePath(int parentId, List<Folder> rootList, List<Integer> folderIdList, String oldPath, String newPath) {
        List<Folder> childList = rootList.stream()
                .filter(instance -> instance.getParentId() == parentId)
                .collect(Collectors.toList());
        if (childList != null) {
            // 把子文件的子文件再循环一遍
            for (Folder folder : childList) {
                folderIdList.add(folder.getId());
                String replacePath = folder.getPath().replace(oldPath, newPath);
                folderMapper.updateByPrimaryKeySelective(
                        Folder.builder()
                                .id(folder.getId())
                                .path(replacePath)
                                .build()
                );
                getChildFolderId(folder.getId(), rootList, folderIdList);
            }
        }
    }
}
