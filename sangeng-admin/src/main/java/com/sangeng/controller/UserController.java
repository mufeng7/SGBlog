package com.sangeng.controller;

import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.StatusDto;
import com.sangeng.domain.dto.UserDto;
import com.sangeng.domain.dto.UserListDto;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.entity.User;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, UserListDto userListDto){
        return userService.pageUserList(pageNum,pageSize,userListDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getUserAndRole(@PathVariable("id") Long id){
        return userService.getUserAndRole(id);
    }

//    @PutMapping("")
//    public ResponseResult updateCategory(@RequestBody Category category){
//        return userService.updateCategory(category);
//    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody StatusDto statusDto){
        return userService.changeStatus(statusDto);
    }

    @PutMapping("")
    public ResponseResult updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }

    /**
     * 添加用户
     */
    @PostMapping
    public ResponseResult addUser(@RequestBody UserDto userDto){
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Long id){
        if (userService.removeById(id)){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_FAIL);
    }
}
