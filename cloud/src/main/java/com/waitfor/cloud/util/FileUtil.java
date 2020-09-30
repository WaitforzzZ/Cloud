package com.waitfor.cloud.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Waitfor
 * @Date 2020-9-22 下午 1:12
 * @Version 1.0
 */
@Slf4j
public class FileUtil {

    /**
     * @title
     * @description 判断文件夹是否存在
     * @Author Waitfor 
     * @Date 2020-9-22 下午 1:14  
     */
    public static void judeDirExists(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                log.info("dir exists");
            } else {
                log.info("the same name file exists, can not create dir");
            }
        } else {
            log.info("dir not exists, create it ...");
            file.mkdir();
        }
    }

    /**
     * 获取文件大小
     *
     * @param size
     * @return
     */
    public static String getFileSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "字节";
        } else {
            size = size / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }

    /**
     * 判断文件类型
     *
     * @param
     * @return
     */
    public static String getFileType(String fileName) {
        String result=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        String type = "other";
        switch(result)
        {
            case "doc" :
                type = "/resource/image/doc.png";
                break;
            case "jpg" :
                type = "/resource/image/image.png";
                break;
            case "music" :
                type = "/resource/image/music.png";
                break;
            case "pdf" :
                type = "/resource/image/pdf.png";
                break;
            case "ppt" :
                type = "/resource/image/ppt.png";
                break;
            case "txt" :
                type = "/resource/image/txt.png";
                break;
            case "video" :
                type = "/resource/image/video.png";
                break;
            case "xls" :
                type = "/resource/image/xls.png";
                break;
            case "zip" :
                type = "/resource/image/zip.png";
                break;
            default :
                type = "/resource/image/other.png";
        }
        return type;
    }
}
