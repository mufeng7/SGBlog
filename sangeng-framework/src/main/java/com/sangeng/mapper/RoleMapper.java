package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sangeng.domain.dto.RoleListDto;
import com.sangeng.domain.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-08-10 16:16:11
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);

    IPage<Role> pageRoleList(Page<Role> page, @Param("roleListDto") RoleListDto roleListDto);
}
