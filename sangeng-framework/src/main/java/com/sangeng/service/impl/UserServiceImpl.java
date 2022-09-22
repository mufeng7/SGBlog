package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.StatusDto;
import com.sangeng.domain.dto.UserDto;
import com.sangeng.domain.dto.UserListDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.User;
import com.sangeng.domain.entity.UserRole;
import com.sangeng.domain.vo.*;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.exception.SystemException;
import com.sangeng.mapper.UserMapper;
import com.sangeng.mapper.UserRoleMapper;
import com.sangeng.service.RoleService;
import com.sangeng.service.UserRoleService;
import com.sangeng.service.UserService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-02-28 15:26:48
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserInfoVo vo= BeanCopyUtils.copyBean(user,UserInfoVo.class);

        return ResponseResult.okResult(vo);
    }

    /**
     * 判断输入是否合理
     * @param user
     */
    private void judgeUserInfo(User user) {
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (userNameExist(user.getUserName()) ){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        judgeUserInfo(user);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> pageUserList(Integer pageNum, Integer pageSize, UserListDto userListDto) {
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page);
        IPage<User> userIPage = userMapper.pageUserList(page,userListDto);
        PageVo pageVo = new PageVo(userIPage.getRecords(),userIPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(StatusDto statusDto) {
        User user = userMapper.selectById(statusDto.getUserId());
        user.setStatus(statusDto.getStatus());
        updateById(user);
        return ResponseResult.okResult();
    }

    /**
     * 注意，为什么会进入这里
     * @param userDto
     * @return
     */

    @Override
    @Transactional
    public ResponseResult updateUser(UserDto userDto) {
        List<Long> roleIds = userDto.getRoleIds();
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        userMapper.updateById(user);
        //userRoleService.deleteRoleIdByUserId(userDto.getId());
        userRoleMapper.deleteRoleIdByUserId(userDto.getId());
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> new UserRole(userDto.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addUser(UserDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);
        String encodePassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encodePassword);
        save(user);
        List<Long> roleIds = userDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    /**
     * 第二行为什么会出现空指针异常
     * @param id
     * @return
     */
    @Override
    public ResponseResult getUserAndRole(Long id) {
        User user = getById(id);
        List<Long> roleIds = userRoleMapper.getRoleIdByUserId(id);
        List<Role> roles = roleService.list();
        UserRoleVo userRoleVo = new UserRoleVo(roleIds,user,roles);
        return ResponseResult.okResult(userRoleVo);
    }




    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper)>0;

    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;

    }


}
