package com.sangeng.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.RoleDto;
import com.sangeng.domain.dto.RoleListDto;
import com.sangeng.domain.dto.RoleStatusDto;
import com.sangeng.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-08-10 16:16:11
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    IPage<Role> listRole(Integer pageNum, Integer pageSize);

    ResponseResult pageRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto);

    ResponseResult changeStatus(RoleStatusDto roleStatusDto);

    ResponseResult listAllRole();

    ResponseResult addRole(RoleDto roleDto);

    ResponseResult roleMenuTreeselect(Long id);
}

