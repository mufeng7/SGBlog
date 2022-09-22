package com.sangeng.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PathUtils {

    public static String generateFilePath(String fileName){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        String datePath=sdf.format(new Date());

        String uuid= UUID.randomUUID().toString().replaceAll("-","");
        int index=fileName.lastIndexOf(".");
        String fileType=fileName.substring(index);
        return new StringBuilder().append(datePath).append(uuid).append(fileType).toString();
    }
}
