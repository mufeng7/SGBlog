package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.StatusDto;
import com.sangeng.domain.dto.UserDto;
import com.sangeng.domain.dto.UserListDto;
import com.sangeng.domain.entity.User;

import java.util.Map;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-02-28 15:26:47
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult pageUserList(Integer pageNum, Integer pageSize, UserListDto userListDto);


    ResponseResult changeStatus(StatusDto statusDto);

    ResponseResult updateUser(UserDto userDto);

    ResponseResult addUser(UserDto userDto);

    ResponseResult getUserAndRole(Long id);

}

