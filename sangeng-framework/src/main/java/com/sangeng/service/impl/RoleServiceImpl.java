package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.RoleDto;
import com.sangeng.domain.dto.RoleListDto;
import com.sangeng.domain.dto.RoleStatusDto;
import com.sangeng.domain.entity.Role;
import com.sangeng.domain.entity.RoleMenu;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.RoleVo;
import com.sangeng.mapper.RoleMapper;
import com.sangeng.mapper.RoleMenuMapper;
import com.sangeng.service.RoleMenuService;
import com.sangeng.service.RoleService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-08-10 16:19:58
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        if (id == 1L){
            List<String> roleKey = new ArrayList<>();
            roleKey.add("admin");
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public IPage<Role> listRole(Integer pageNum, Integer pageSize) {
        Page<Role> page = page(new Page<>(pageNum, pageSize), null);
        return page;
    }

    @Override
    public ResponseResult pageRoleList(Integer pageNum, Integer pageSize, RoleListDto roleListDto) {

        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page);
        IPage<Role> roleIPage = roleMapper.pageRoleList(page,roleListDto);
        PageVo pageVo = new PageVo(roleIPage.getRecords(),roleIPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(RoleStatusDto roleStatusDto) {
        Role role = getById(roleStatusDto.getRoleId());
        role.setStatus(roleStatusDto.getStatus());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        List<Role> roles = list();
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(roles, RoleVo.class);
        return ResponseResult.okResult(roleVos);
    }

    @Override
    @Transactional
    public ResponseResult addRole(RoleDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(role);
        List<Long> menuIds = roleDto.getMenuIds();
        List<RoleMenu> roleMenus = menuIds.stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        Role role = roleService.getById(id);
        RoleDto roleDto = toRoleDto(role);
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,role.getId());
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper);
        List<Long> list = roleMenus.stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());
        roleDto.setMenuIds(list);
        return ResponseResult.okResult(roleDto);
    }

    private RoleDto toRoleDto(Role role) {
        return new RoleDto(role.getRoleName(),role.getRoleKey(),role.getRoleSort(),role.getStatus(),role.getRemark());
    }
}
