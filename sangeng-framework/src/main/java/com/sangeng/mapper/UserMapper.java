package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sangeng.domain.dto.UserListDto;
import com.sangeng.domain.entity.User;
import org.apache.ibatis.annotations.Param;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-02-13 14:12:03
 */
public interface UserMapper extends BaseMapper<User> {

    IPage<User> pageUserList(Page<User> page, @Param("userListDto") UserListDto userListDto);
}
