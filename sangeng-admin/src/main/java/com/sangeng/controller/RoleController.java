package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.RoleDto;
import com.sangeng.domain.dto.RoleListDto;
import com.sangeng.domain.dto.RoleStatusDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, RoleListDto roleListDto){
        return roleService.pageRoleList(pageNum,pageSize,roleListDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id") Long id){
        if (roleService.removeById(id)){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_FAIL);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleStatusDto roleStatusDto){
        return roleService.changeStatus(roleStatusDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable("id") Long id){
//        Role role = roleService.getById(id);
//        return ResponseResult.okResult(role);
        return roleService.roleMenuTreeselect(id);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody RoleDto roleDto){
        return roleService.addRole(roleDto);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }


}
