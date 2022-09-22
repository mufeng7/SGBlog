package com.sangeng.service.impl;

import com.sangeng.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否具有permission
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission){
        System.out.println("4");
        if (SecurityUtils.isAdmin()){
            return true;
        }
        List<String> permissions = SecurityUtils.getLoginUser().getPremission();
        return permissions.contains(permission);
    }
}
