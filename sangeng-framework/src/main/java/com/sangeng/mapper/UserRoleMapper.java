package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.UserRole;

import java.util.List;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-13 10:54:22
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<Long> getRoleIdByUserId(Long id);

    void deleteRoleIdByUserId(Long id);
}
