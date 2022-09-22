package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.LoginUser;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.entity.User;
import com.sangeng.domain.vo.AdminUserInfoVo;
import com.sangeng.domain.vo.RoutersVo;
import com.sangeng.domain.vo.UserInfoVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.service.*;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.RedisCache;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.certpath.PKIXTimestampParameters;

import java.util.List;

@RestController
public class AdminLoginController {


    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;


    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){

        return loginService.logout();
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        System.out.println("1");
        LoginUser loginUser = SecurityUtils.getLoginUser();
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());

        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());


        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);

    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);

        return ResponseResult.okResult(new RoutersVo(menus));
    }



}
